package afcc.taavi.kase.afcc.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import afcc.taavi.kase.afcc.R;
import afcc.taavi.kase.afcc.database.ConsumptionTable;
import afcc.taavi.kase.afcc.database.DistanceTable;
import afcc.taavi.kase.afcc.database.SettingsTable;
import afcc.taavi.kase.afcc.database.SpeedTable;
import afcc.taavi.kase.afcc.database.UnitTable;

/**
 * Created by Taavi Kase
 *
 * Settings activity
 */
public class SettingsActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "SettingsActivity";
    private static final int SETTINGS_LOADER = 0;
    private static final int DISTANCE_SPINNER_LOADER = 1;
    private static final int UNIT_SPINNER_LOADER = 2;
    private static final int CONSUMPTION_SPINNER_LOADER = 3;
    private static final int SPEED_SPINNER_LOADER = 4;

    private int mDistance = 0;
    private int mUnit = 0;
    private int mConsumption = 0;
    private int mSpeed = 0;
    private int mPosition = 0;

    private SimpleCursorAdapter mDistanceAdapter;
    private SimpleCursorAdapter mUnitAdapter;
    private SimpleCursorAdapter mConsumptionAdapter;
    private SimpleCursorAdapter mSpeedAdapter;

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

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e(TAG, "No actionbar");
        }

        initializeSpinners();
    }

    /**
     * Initializes spinner values
     */
    private void initializeSpinners() {
        mDistanceSpinner = (Spinner) findViewById(R.id.distanceSpinner);
        mUnitSpinner = (Spinner) findViewById(R.id.unitSpinner);
        mConsumptionSpinner = (Spinner) findViewById(R.id.consumptionSpinner);
        mSpeedSpinner = (Spinner) findViewById(R.id.speedSpinner);

        getSupportLoaderManager().restartLoader(SETTINGS_LOADER, null, this);
    }

    /**
     * Called when a button is clicked
     *
     * @param clickedButton Button that was clicked
     */
    public void onClick(View clickedButton) {
        int id = clickedButton.getId();

        switch (id) {
            case R.id.saveButton:
                save();
                break;
            case R.id.restoreButton:
                restoreDefaults();
                break;
        }
    }

    /**
     * Called when Save button is clicked. Saves all data to database
     */
    private void save() {
        mDistance = (int) mDistanceSpinner.getSelectedItemId();
        mUnit = (int) mUnitSpinner.getSelectedItemId();
        mConsumption = (int) mConsumptionSpinner.getSelectedItemId();
        mSpeed = (int) mSpeedSpinner.getSelectedItemId();

        ContentValues values = new ContentValues();
        values.put(SettingsTable.COL_DISTANCE, mDistance);
        values.put(SettingsTable.COL_UNIT, mUnit);
        values.put(SettingsTable.COL_CONSUMPTION, mConsumption);
        values.put(SettingsTable.COL_SPEED, mSpeed);

        ContentResolver content = getContentResolver();
        String selection = SettingsTable._ID + " = 0";
        content.update(SettingsTable.CONTENT_URI, values, selection, null);

        makeToast("Changes saved!");
        finish();
    }

    /**
     * Restores spinners default settings
     */
    private void restoreDefaults() {
        ContentValues values = new ContentValues();
        values.put(SettingsTable.COL_DISTANCE, 0);
        values.put(SettingsTable.COL_UNIT, 0);
        values.put(SettingsTable.COL_CONSUMPTION, 0);
        values.put(SettingsTable.COL_SPEED, 0);

        ContentResolver content = getContentResolver();
        String selection = SettingsTable._ID + " = 0";
        content.update(SettingsTable.CONTENT_URI, values, selection, null);

        getSupportLoaderManager().restartLoader(SETTINGS_LOADER, null, this);
        makeToast("Defaults restored!");
    }

    /**
     * Gets data from settings cursor. Initializes corresponding class variables and starts spinner
     * loaders
     *
     * @param cursor Holds data queried from settings table
     */
    private void getSettings(Cursor cursor) {
        mDistance = cursor.getInt(cursor.getColumnIndex(SettingsTable.COL_DISTANCE));
        mPosition = mDistance;
        mUnit = cursor.getInt(cursor.getColumnIndex(SettingsTable.COL_UNIT));
        mConsumption = cursor.getInt(cursor.getColumnIndex(SettingsTable.COL_CONSUMPTION));
        mSpeed = cursor.getInt(cursor.getColumnIndex(SettingsTable.COL_SPEED));

        int[] to = {android.R.id.text1};

        setDistanceSpinnerLoader(to);
        setUnitSpinnerLoader(to);
        setConsumptionSpinnerLoader(to);
        setSpeedSpinnerLoader(to);
    }

    /**
     * Sets adapter and starts cursor loader to inflate distance spinner. Also sets on item selected
     * listener so if user selects different distance unit other spinners show data corresponding to
     * that.
     *
     * @param to TextView to set data to
     */
    private void setDistanceSpinnerLoader(int[] to) {
        String[] from = {DistanceTable.COL_TEXT, DistanceTable._ID};
        mDistanceAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, null,
                from, to, DISTANCE_SPINNER_LOADER);

        mDistanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getSupportLoaderManager().restartLoader(DISTANCE_SPINNER_LOADER, null, this);
        mDistanceSpinner.setAdapter(mDistanceAdapter);
        mDistanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != mPosition) {
                    mPosition = position;
                    onDistanceSelected();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    /**
     * Called when user selects new distance unit. Restarts loaders for unit and consumption spinner
     * and sets speed spinner selection according to new distance unit.
     */
    private void onDistanceSelected() {
        mDistance = (int) mDistanceSpinner.getSelectedItemId();
        getSupportLoaderManager().restartLoader(UNIT_SPINNER_LOADER, null, this);
        getSupportLoaderManager().restartLoader(CONSUMPTION_SPINNER_LOADER, null, this);
        mSpeed = mDistance;
        mSpeedSpinner.setSelection(mSpeed);
    }

    /**
     * Sets adapter and starts cursor loader to inflate unit spinner
     *
     * @param to TextView to set data to
     */
    private void setUnitSpinnerLoader(int[] to) {
        String[] from = {UnitTable.COL_TEXT, UnitTable._ID};
        mUnitAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, null,
                from, to, UNIT_SPINNER_LOADER);

        mUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getSupportLoaderManager().restartLoader(UNIT_SPINNER_LOADER, null, this);
        mUnitSpinner.setAdapter(mUnitAdapter);
    }

    /**
     * Sets adapter and starts cursor loader to inflate consumption spinner
     *
     * @param to TextView to set data to
     */
    private void setConsumptionSpinnerLoader(int[] to) {
        String[] from = {ConsumptionTable.COL_TEXT, ConsumptionTable._ID};
        mConsumptionAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item,
                null, from, to, CONSUMPTION_SPINNER_LOADER);

        mConsumptionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getSupportLoaderManager().restartLoader(CONSUMPTION_SPINNER_LOADER, null, this);
        mConsumptionSpinner.setAdapter(mConsumptionAdapter);
    }

    /**
     * Sets adapter and starts cursor loader to inflate speed spinner
     *
     * @param to TextView to set data to
     */
    private void setSpeedSpinnerLoader(int[] to) {
        String[] from = {SpeedTable.COL_TEXT, SpeedTable._ID};
        mSpeedAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, null,
                from, to, SPEED_SPINNER_LOADER);

        mSpeedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getSupportLoaderManager().restartLoader(SPEED_SPINNER_LOADER, null, this);
        mSpeedSpinner.setAdapter(mSpeedAdapter);
    }

    /**
     * Creates CursorLoaders
     *
     * @param id     CursorLoader ID
     * @param bundle May hold various data needed for queries
     * @return CursorLoader object
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        switch (id) {
            case SETTINGS_LOADER:
                Uri settingsUri = SettingsTable.CONTENT_URI;
                return new CursorLoader(this, settingsUri, null, null, null, null);
            case DISTANCE_SPINNER_LOADER:
                Uri distanceUri = DistanceTable.CONTENT_URI;
                return new CursorLoader(this, distanceUri, null, null, null, null);
            case UNIT_SPINNER_LOADER:
                Uri unitUri = UnitTable.CONTENT_URI;
                String unitSelection = UnitTable.COL_DISTANCE_ID + " = ?";
                String[] unitSelectionArgs = {String.valueOf(mDistance)};
                return new CursorLoader(this, unitUri, null, unitSelection, unitSelectionArgs, null);
            case CONSUMPTION_SPINNER_LOADER:
                Uri consumptionUri = ConsumptionTable.CONTENT_URI;
                String consumptionSelection = ConsumptionTable.COL_DISTANCE_ID + " = ?";
                String[] consumptionSelectionArgs = {String.valueOf(mDistance)};
                return new CursorLoader(this, consumptionUri, null, consumptionSelection,
                        consumptionSelectionArgs, null);
            case SPEED_SPINNER_LOADER:
                Uri speedUri = SpeedTable.CONTENT_URI;
                return new CursorLoader(this, speedUri, null, null, null, null);
            default:
                return null;
        }
    }

    /**
     * Called after query is finished.
     *
     * @param cursorLoader CursorLoader object
     * @param cursor       Holds data from database query
     */
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        switch (cursorLoader.getId()) {
            case SETTINGS_LOADER:
                cursor.moveToFirst();
                getSettings(cursor);
                break;
            case DISTANCE_SPINNER_LOADER:
                mDistanceAdapter.swapCursor(cursor);
                mDistanceSpinner.setSelection(mDistance);
                break;
            case UNIT_SPINNER_LOADER:
                mUnitAdapter.swapCursor(cursor);
                break;
            case CONSUMPTION_SPINNER_LOADER:
                mConsumptionAdapter.swapCursor(cursor);

                if (mDistance == 0) {
                    mConsumptionSpinner.setSelection(mConsumption);
                }

                break;
            case SPEED_SPINNER_LOADER:
                mSpeedAdapter.swapCursor(cursor);
                mSpeedSpinner.setSelection(mSpeed);
                break;
        }
    }

    /**
     * Called when CursorLoaders are reset.
     *
     * @param cursorLoader CursorLoader object
     */
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        switch (cursorLoader.getId()) {
            case DISTANCE_SPINNER_LOADER:
                mDistanceAdapter.swapCursor(null);
                break;
            case UNIT_SPINNER_LOADER:
                mUnitAdapter.swapCursor(null);
                break;
            case CONSUMPTION_SPINNER_LOADER:
                mConsumptionAdapter.swapCursor(null);
                break;
            case SPEED_SPINNER_LOADER:
                mSpeedAdapter.swapCursor(null);
                break;
        }
    }
}