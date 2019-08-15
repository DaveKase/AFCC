package afcc.taavi.kase.afcc.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Calendar;

import afcc.taavi.kase.afcc.AfccApplication;
import afcc.taavi.kase.afcc.R;
import afcc.taavi.kase.afcc.database.SettingsTable;

/**
 * Created by Taavi Kase on 24.09.2014.
 *
 * Average speed calculator activity
 */
public class AverageSpeedActivity extends BaseActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, TimePickerFragment.TimePickedListener {

    private static final String TAG = "AverageSpeedActivity";
    private static final int UNITS_LOADER = 0;
    private int mUnit = 0;
    private EditText mEditText;
    private Tracker mTracker;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState May contain data supplied by saveInstanceState or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_speed);

        AfccApplication application = (AfccApplication) getApplication();
        mTracker = application.getDefaultTracker();

        try {
            //noinspection ConstantConditions
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e(TAG, "No actionbar");
        }

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
        String text = ((EditText) v).getText().toString();
        TimePickerFragment newFragment = new TimePickerFragment();

        if (!text.equals("")) {
            Bundle bundle = new Bundle();
            bundle.putString("time", text);
            newFragment.setArguments(bundle);
        }

        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    /**
     * Called when time is picked from time picker dialog
     *
     * @param time Time that was picked
     */
    @Override
    public void onTimePicked(Calendar time) {
        int hours = time.get(Calendar.HOUR_OF_DAY);
        int minutes = time.get(Calendar.MINUTE);
        String h = String.valueOf(hours);
        String m = String.valueOf(minutes);

        if (hours < 10) {
            h = "0" + hours;
        }

        if (minutes < 10) {
            m = "0" + minutes;
        }

        String t = h + ":" + m;
        mEditText.setText(t);
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
                calculateAverageSpeed(distance, startTime, endTime, false);
            } else {
                validateEditTexts(distance, startTime, endTime);
            }
        } catch (NumberFormatException e) {
            makeToast(getResourceString(R.string.err_no_distance));
        }
    }

    /**
     * Checks if there is some invalid data entered or data wasn't entered at all to EditTexts
     *
     * @param distance  Distance that should have been entered and it has to be bigger than zero
     * @param startTime Start time that should have been entered
     * @param endTime   End time that should have been entered
     */
    private void validateEditTexts(double distance, String startTime, String endTime) {
        if (distance <= 0) {
            makeToast(getResourceString(R.string.err_dis_less_zero));
        } else if (startTime.equals("")) {
            makeToast(getResourceString(R.string.err_no_start));
        } else if (endTime.equals("")) {
            makeToast(getResourceString(R.string.err_no_end));
        }
    }

    /**
     * Calculates average speed from distance and time
     *
     * @param distance     Distance travelled
     * @param startTime    Time taken to travel that distance
     * @param isSecondCall True if this method is called from itself, false otherwise
     */
    private void calculateAverageSpeed(double distance, String startTime, String endTime, boolean isSecondCall) {
        try {
            double startHours = getHoursFromTime(startTime);
            double endHours = getHoursFromTime(endTime);
            double hours = endHours - startHours;

            if (hours > 0) {
                String speed = rounder(distance / hours);
                showResults(speed);
            } else {
                if (isSecondCall) {
                    makeToast(getResourceString(R.string.err_int_zero));
                } else {
                    startTime = revertAmPm(startTime);
                    endTime = revertAmPm(endTime);

                    calculateAverageSpeed(distance, startTime, endTime, true);
                }
            }
        } catch (Exception e) {
            makeToast(getResourceString(R.string.err_wrong_time));
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

        String speedUnit = "";
        switch (mUnit) {
            case 0:
                speedUnit = "km/h";
                break;
            case 1:
                speedUnit = "mph";
                break;
        }

        String text = getResourceString(R.string.average_speed) + " " + speed + " " + speedUnit;
        averageSpeedText.setText(text);
    }

    /**
     * Converts PM time to AM and AM time to PM to allow time calculations where start time is PM
     * and end time is AM
     *
     * @param time Time to convert
     * @return Converted time
     */
    private String revertAmPm(String time) {
        String[] hourAndMinute = time.split(":");
        String hour = hourAndMinute[0];
        String minute = hourAndMinute[1];
        String t;

        int h = Integer.parseInt(hour);

        hour = h < 13 ? String.valueOf(h + 12) : String.valueOf(h - 12);
        t = hour + ":" + minute;

        return t;
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
                return getResourceString(R.string.val_km);
            case 1:
                return getResourceString(R.string.val_m);
            default:
                return "";
        }
    }

    /**
     * Called when activity is resumed
     */
    @Override
    public void onResume() {
        super.onResume();

        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
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