package afcc.taavi.kase.afcc;

import afcc.taavi.kase.afcc.Database.Settings;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
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

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState May contain data supplied by saveInstanceState or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        versionName();
        startLoaders();
    }

    /**
     * Gets the app version name and sets it to versionNumberText TextView.
     */
    private void versionName() {
        String version;

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            version = "Did not get a version name";
        }

        TextView versionNumberText = (TextView) findViewById(R.id.versionNumberText);
        versionNumberText.setText(version);
    }

    /** Starts CursorLoaders */
    private void startLoaders() {
        getLoaderManager().restartLoader(DISTANCE_LOADER, null, this);
        getLoaderManager().restartLoader(UNIT_LOADER, null, this);
        getLoaderManager().restartLoader(CONSUMPTION_LOADER, null, this);
    }

    /**
     * Called when Save button is clicked. Saves all data to database
     *
     * @param clickedButton SaveButton
     */
    public void save(View clickedButton) {
        Spinner distanceSpinner = (Spinner) findViewById(R.id.distanceSpinner);
        Spinner unitSpinner = (Spinner) findViewById(R.id.unitSpinner);
        Spinner consumptionSpinner = (Spinner) findViewById(R.id.consumptionSpinner);

        int distance = (int) distanceSpinner.getSelectedItemId();
        int unit = (int) unitSpinner.getSelectedItemId();
        int consumption = (int) consumptionSpinner.getSelectedItemId();

        ContentValues values = new ContentValues();
        values.put(Settings.COL_DISTANCE, distance);
        values.put(Settings.COL_UNIT, unit);
        values.put(Settings.COL_CONSUMPTION, consumption);

        ContentResolver content = getContentResolver();
        String selection = Settings._ID + " = 0";
        content.update(Settings.CONTENT_URI, values, selection, null);

        makeToast("Changes saved!");
        finish();
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
        Uri settingsUri = Settings.CONTENT_URI;

        switch(id) {
            case DISTANCE_LOADER:
                String[] distanceProjection = {Settings.COL_DISTANCE};
                return new CursorLoader(this, settingsUri, distanceProjection, null, null, null);
            case UNIT_LOADER:
                String[] unitProjection = {Settings.COL_UNIT};
                return new CursorLoader(this, settingsUri, unitProjection, null, null, null);
            case CONSUMPTION_LOADER:
                String[] consumptionProjection = {Settings.COL_CONSUMPTION};
                return new CursorLoader(this, settingsUri, consumptionProjection, null, null, null);
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
        cursor.moveToPosition(0);

        if(cursorCount > 0) {
            setSelections(loaderId, cursor);
        } else {
            Spinner distanceSpinner = (Spinner) findViewById(R.id.distanceSpinner);
            Spinner unitSpinner = (Spinner) findViewById(R.id.unitSpinner);
            Spinner consumptionSpinner = (Spinner) findViewById(R.id.consumptionSpinner);

            distanceSpinner.setSelection(0);
            unitSpinner.setSelection(0);
            consumptionSpinner.setSelection(0);
        }
    }

    /**
     * Sets spinner selections
     *
     * @param loaderId Loader ID
     * @param cursor Holds data from database query
     */
    private void setSelections(int loaderId, Cursor cursor) {
        Spinner distanceSpinner = (Spinner) findViewById(R.id.distanceSpinner);
        Spinner unitSpinner = (Spinner) findViewById(R.id.unitSpinner);
        Spinner consumptionSpinner = (Spinner) findViewById(R.id.consumptionSpinner);

        switch (loaderId) {
            case DISTANCE_LOADER:
                int distance = cursor.getInt(cursor.getColumnIndex(Settings.COL_DISTANCE));
                distanceSpinner.setSelection(distance);
                break;
            case UNIT_LOADER:
                int unit = cursor.getInt(cursor.getColumnIndex(Settings.COL_UNIT));
                unitSpinner.setSelection(unit);
                break;
            case CONSUMPTION_LOADER:
                int consumption = cursor.getInt(cursor.getColumnIndex(Settings.COL_CONSUMPTION));
                consumptionSpinner.setSelection(consumption);
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
        // Nothing to do here.
    }
}