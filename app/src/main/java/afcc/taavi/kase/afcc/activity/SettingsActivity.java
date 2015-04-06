package afcc.taavi.kase.afcc.activity;

import afcc.taavi.kase.afcc.R;
import afcc.taavi.kase.afcc.database.SettingsTable;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.content.Loader;
import android.database.Cursor;
import android.app.LoaderManager;

/**
 * Created by Taavi Kase
 *
 * Settings activity
 */
public class SettingsActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int DISTANCE_LOADER = 0;
    private static final int UNIT_LOADER = 1;
    private static final int CONSUMPTION_LOADER = 2;
    private static final int SPEED_LOADER = 3;

    private Spinner mDistanceSpinner;
    private Spinner mUnitSpinner;
    private Spinner mConsumptionSpinner;
    private Spinner mSpeedSpinner;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState May contain data supplied by saveInstanceState or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initializeSpinners();
        startLoaders();
    }

    /** Initializes spinner values */
    private void initializeSpinners() {
        mDistanceSpinner = (Spinner) findViewById(R.id.distanceSpinner);
        mUnitSpinner = (Spinner) findViewById(R.id.unitSpinner);
        mConsumptionSpinner = (Spinner) findViewById(R.id.consumptionSpinner);
        mSpeedSpinner = (Spinner) findViewById(R.id.speedSpinner);
    }

    /** Starts CursorLoaders */
    private void startLoaders() {
        getLoaderManager().restartLoader(DISTANCE_LOADER, null, this);
        getLoaderManager().restartLoader(UNIT_LOADER, null, this);
        getLoaderManager().restartLoader(CONSUMPTION_LOADER, null, this);
        getLoaderManager().restartLoader(SPEED_LOADER, null, this);
    }

    /**
     * Called when a button is clicked
     *
     * @param clickedButton Button that was clicked
     */
    public void onClick(View clickedButton) {
        int id = clickedButton.getId();

        switch(id) {
            case R.id.saveButton:
                save();
                break;
            case R.id.restoreButton:
                restoreDefaults();
                break;
        }
    }

    /** Called when Save button is clicked. Saves all data to database */
    public void save() {
        int distance = (int) mDistanceSpinner.getSelectedItemId();
        int unit = (int) mUnitSpinner.getSelectedItemId();
        int consumption = (int) mConsumptionSpinner.getSelectedItemId();
        int speed = (int) mSpeedSpinner.getSelectedItemId();

        ContentValues values = new ContentValues();
        values.put(SettingsTable.COL_DISTANCE, distance);
        values.put(SettingsTable.COL_UNIT, unit);
        values.put(SettingsTable.COL_CONSUMPTION, consumption);
        values.put(SettingsTable.COL_SPEED, speed);

        ContentResolver content = getContentResolver();
        String selection = SettingsTable._ID + " = 0";
        content.update(SettingsTable.CONTENT_URI, values, selection, null);

        makeToast("Changes saved!");
        finish();
    }

    /** Restores spinners default settings */
    public void restoreDefaults() {
        ContentValues values = new ContentValues();
        values.put(SettingsTable.COL_DISTANCE, 0);
        values.put(SettingsTable.COL_UNIT, 0);
        values.put(SettingsTable.COL_CONSUMPTION, 0);
        values.put(SettingsTable.COL_SPEED, 0);

        ContentResolver content = getContentResolver();
        String selection = SettingsTable._ID + " = 0";
        content.update(SettingsTable.CONTENT_URI, values, selection, null);

        makeToast("Defaults restored!");
        resetSpinners();
    }

    /** Resets spinners to 0 position */
    private void resetSpinners() {
        mDistanceSpinner.setSelection(0);
        mUnitSpinner.setSelection(0);
        mConsumptionSpinner.setSelection(0);
        mSpeedSpinner.setSelection(0);
    }

    /**
     * Creates CursorLoaders
     *
     * @param id CursorLoader ID
     * @param bundle May hold various data needed for queries
     * @return CursorLoader object
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Uri settingsUri = SettingsTable.CONTENT_URI;

        switch(id) {
            case DISTANCE_LOADER:
                String[] distanceProjection = {SettingsTable.COL_DISTANCE};
                return new CursorLoader(this, settingsUri, distanceProjection, null, null, null);
            case UNIT_LOADER:
                String[] unitProjection = {SettingsTable.COL_UNIT};
                return new CursorLoader(this, settingsUri, unitProjection, null, null, null);
            case CONSUMPTION_LOADER:
                String[] consumptionProjection = {SettingsTable.COL_CONSUMPTION};
                return new CursorLoader(this, settingsUri, consumptionProjection, null, null, null);
            case SPEED_LOADER:
                String[] speedProjection = {SettingsTable.COL_SPEED};
                return new CursorLoader(this, settingsUri, speedProjection, null, null, null);
            default:
                return null;
        }
    }

    /**
     * Called after query is finished.
     *
     * @param cursorLoader CursorLoader object
     * @param cursor Holds data from database query
     */
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        int loaderId = cursorLoader.getId();
        int cursorCount = cursor.getCount();

        if(cursorCount > 0) {
            cursor.moveToPosition(0);
            setSelections(loaderId, cursor);
        } else {
            resetSpinners();
        }
    }

    /**
     * Sets spinner selections
     *
     * @param loaderId Loader ID
     * @param cursor Holds data from database query
     */
    private void setSelections(int loaderId, Cursor cursor) {
        switch (loaderId) {
            case DISTANCE_LOADER:
                int distance = cursor.getInt(cursor.getColumnIndex(SettingsTable.COL_DISTANCE));
                mDistanceSpinner.setSelection(distance);
                break;
            case UNIT_LOADER:
                int unit = cursor.getInt(cursor.getColumnIndex(SettingsTable.COL_UNIT));
                mUnitSpinner.setSelection(unit);
                break;
            case CONSUMPTION_LOADER:
                int consumption = cursor.getInt(cursor.getColumnIndex(SettingsTable.COL_CONSUMPTION));
                mConsumptionSpinner.setSelection(consumption);
                break;
            case SPEED_LOADER:
                int speed = cursor.getInt(cursor.getColumnIndex(SettingsTable.COL_SPEED));
                mSpeedSpinner.setSelection(speed);
        }
    }

    /**
     * Called when CursorLoaders are reset.
     *
     * @param cursorLoader CursorLoader object
     */
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        // Nothing to do here
    }
}