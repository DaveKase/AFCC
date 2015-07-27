package afcc.taavi.kase.afcc.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.TextView;

import afcc.taavi.kase.afcc.R;
import afcc.taavi.kase.afcc.database.SettingsTable;

/**
 * Created by Taavi Kase
 *
 * Speedometer activity
 */
public class SpeedometerActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int UNIT_LOADER = 0;
    /**
     * Called when Activity is first created
     *
     * @param savedInstanceState May contain data supplied by saveInstanceState or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speedometer);

        getSpeedUnit();
    }

    private void getSpeedUnit() {
        getSupportLoaderManager().restartLoader(UNIT_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case UNIT_LOADER:
                Uri settingsUri = SettingsTable.CONTENT_URI;
                String[] projection = {SettingsTable.COL_SPEED};
                return new CursorLoader(this, settingsUri, projection, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch(loader.getId()) {
            case UNIT_LOADER:
                cursor.moveToFirst();
                setUnit(cursor);
                break;
        }
    }

    private void setUnit(Cursor cursor) {
        int unit = cursor.getInt(cursor.getColumnIndex(SettingsTable.COL_SPEED));
        String unitValue = getUnit(unit);

        TextView speedUnit = (TextView) findViewById(R.id.speedUnit);
        speedUnit.setText(unitValue);
    }

    private String getUnit(int unit) {
        switch (unit) {
            case SPEED_KM_H:
                return "km / h";
            case SPEED_MPH:
                return "mph";
            default:
                return "";
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}