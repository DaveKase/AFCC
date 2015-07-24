package afcc.taavi.kase.afcc.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import afcc.taavi.kase.afcc.R;
import afcc.taavi.kase.afcc.database.SettingsTable;

/**
 * Created by Taavi Kase on 24.09.2014.
 * 
 * Average speed calculator activity
 */
public class AverageSpeedActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    //private static final String TAG = "AverageSpeedActivity";
    private static final int UNITS_LOADER = 0;
    private int mUnit = 0;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState May contain data supplied by saveInstanceState or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_speed);

        getSupportLoaderManager().restartLoader(UNITS_LOADER, null, this);
    }

    /**
     * Called when calculate button was clicked
     *
     * @param v View that was clicked
     */
    public void calculate(View v) {
        try {
            double distance = parser(getTextFromEditText(R.id.distanceEdit));

            EditText timeEdit = (EditText) findViewById(R.id.timeEdit);
            String time = timeEdit.getText().toString();

            if (time.equals("")) {
                makeToast("Time not entered");
            } else {
                calculateAverageSpeed(distance, time);
            }
        } catch (NumberFormatException e) {
            makeToast("Distance not inserted");
        }
    }

    /**
     * Calculates average speed from distance and time
     *
     * @param distance Distance travelled
     * @param time     Time taken to travel that distance
     */
    private void calculateAverageSpeed(double distance, String time) {
        if (time.contains(":")) {
            try {
                String[] split = time.split(":");
                int hour = Integer.parseInt(split[0]);
                int minute = Integer.parseInt(split[1]);

                int seconds = (60 * minute) + (3600 * hour);
                double minutes = seconds / 60;
                double hours = minutes / 60;

                String speed = rounder(distance / hours);
                showResults(speed);
            } catch (NumberFormatException e) {
                makeToast("Wrong time values used, hours and minutes have to be integers");
            }
        } else {
            makeToast("Time is in wrong format, should be HH:mm");
        }
    }

    /**
     * Shows the final result as km/h or mph, depends the selection made in SettingsActivity
     *
     * @param speed Speed to show
     */
    private void showResults(String speed) {
        TextView averageSpeedText = (TextView) findViewById(R.id.averageSpeedText);
        TextView averageSpeedResult = (TextView) findViewById(R.id.averageSpeedResult);

        String speedUnit = "";
        switch (mUnit) {
            case 0:
                speedUnit = "km / h";
                break;
            case 1:
                speedUnit = "mph";
        }

        String text = speed + " " + speedUnit;
        averageSpeedResult.setText(text);
        averageSpeedText.setVisibility(View.VISIBLE);
        averageSpeedResult.setVisibility(View.VISIBLE);
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
            case UNITS_LOADER:
                Uri settingsUri = SettingsTable.CONTENT_URI;
                String[] projection = {SettingsTable.COL_DISTANCE};
                return new CursorLoader(this, settingsUri, projection, null, null, null);
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
            case UNITS_LOADER:
                cursor.moveToFirst();
                mUnit = cursor.getInt(cursor.getColumnIndex(SettingsTable.COL_DISTANCE));
                String distance = getDistanceUnit(mUnit);

                TextView distanceUnitText = (TextView) findViewById(R.id.distanceUnitText);
                distanceUnitText.setText(distance);
                break;
        }
    }

    private String getDistanceUnit(int distance) {
        switch (distance) {
            case 0:
                return "km";
            case 1:
                return "miles";
            default:
                return "";
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