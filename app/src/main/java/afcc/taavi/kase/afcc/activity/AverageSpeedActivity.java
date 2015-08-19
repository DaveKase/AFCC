package afcc.taavi.kase.afcc.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import afcc.taavi.kase.afcc.R;
import afcc.taavi.kase.afcc.database.SettingsTable;

/**
 * Created by Taavi Kase on 24.09.2014.
 *
 * Average speed calculator activity
 */
public class AverageSpeedActivity extends BaseActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, TimePickerFragment.TimePickedListener {

    //private static final String TAG = "AverageSpeedActivity";
    private static final int UNITS_LOADER = 0;
    private int mUnit = 0;
    private EditText mEditText;

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
        setListenersToEditTexts();
    }

    /**
     * Sets on focus change and on click listeners to start and end time EditTexts
     */
    private void setListenersToEditTexts() {
        EditText startTimeEdit = (EditText) findViewById(R.id.startTimeEdit);
        EditText endTimeEdit = (EditText) findViewById(R.id.endTimeEdit);

        startTimeEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setTime(v);
                }
            }
        });

        startTimeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(v);
            }
        });

        endTimeEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setTime(v);
                }
            }
        });

        endTimeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(v);
            }
        });
    }

    /**
     * Sets time to correct EditText
     *
     * @param v View to set the time to
     */
    public void setTime(View v) {
        mEditText = (EditText) v;
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    /**
     * Called when time is picked from time picker dialog
     *
     * @param time Time that was picked
     */
    @Override
    public void onTimePicked(Calendar time) {
        mEditText.setText(DateFormat.format("HH:mm", time));
    }

    /**
     * Called when calculate button was clicked
     *
     * @param v View that was clicked
     */
    public void calculate(View v) {
        try {
            double distance = parser(getTextFromEditText(R.id.distanceEdit));

            EditText startTimeEdit = (EditText) findViewById(R.id.startTimeEdit);
            EditText endTimeEdit = (EditText) findViewById(R.id.endTimeEdit);

            String startTime = startTimeEdit.getText().toString();
            String endTime = endTimeEdit.getText().toString();

            if (distance > 0 && !startTime.equals("") && !endTime.equals("")) {
                calculateAverageSpeed(distance, startTime, endTime);
            } else {
                validateEditTexts(distance, startTime, endTime);
            }
        } catch (NumberFormatException e) {
            makeToast("Distance not inserted");
        }
    }

    /**
     * Checks if there is some invalid data entered or data wasn't entered at all to EditTexts
     *
     * @param distance Distance that should have been entered and it has to be bigger than zero
     * @param startTime Start time that should have been entered
     * @param endTime End time that should have been entered
     */
    private void validateEditTexts(double distance, String startTime, String endTime) {
        if (distance <= 0) {
            makeToast("Distance has to be bigger than zero");
        } else if (startTime.equals("")) {
            makeToast("Start time is not inserted");
        } else if (endTime.equals("")) {
            makeToast("End time is not inserted");
        }
    }

    /**
     * Calculates average speed from distance and time
     *
     * @param distance  Distance travelled
     * @param startTime Time taken to travel that distance
     */
    private void calculateAverageSpeed(double distance, String startTime, String endTime) {
        try {
            double startHours = getHoursFromTime(startTime);
            double endHours = getHoursFromTime(endTime);
            double hours = endHours - startHours;

            if (hours > 0) {
                String speed = rounder(distance / hours);
                showResults(speed);
            } else {
                makeToast("There's nothing to calculate, time interval is zero");
            }
        } catch (Exception e) {
            makeToast("Time is in wrong format");
        }
    }

    /**
     * Gets double hours from String time (HH:mm format)
     *
     * @param time Time to get the double hours from
     * @return Double hours from String time
     */
    private double getHoursFromTime(String time) {
        String[] split = time.split(":");
        int hour = Integer.parseInt(split[0]);
        int minute = Integer.parseInt(split[1]);

        int seconds = (60 * minute) + (3600 * hour);
        double minutes = seconds / 60;
        return minutes / 60;
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
                break;
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

    /**
     * Gets the distance unit from int
     *
     * @return Distance unit based on int
     */
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