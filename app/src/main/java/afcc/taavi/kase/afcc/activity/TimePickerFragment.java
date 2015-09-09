package afcc.taavi.kase.afcc.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Taavi on 19.08.2015.
 *
 * Creates new TimePickerFragment to enable time to be set on EditText
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private TimePickedListener mListener;
    //private static final String TAG = "TimePickerFragment";

    /**
     * Creates dialog box
     *
     * @param savedInstanceState State of the saved instance
     * @return New time picker dialog box
     */
    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int hour;
        int minute;

        savedInstanceState = getArguments();

        if (savedInstanceState != null) {
            Bundle bundle = getTime(savedInstanceState);
            hour = bundle.getInt("hour");
            minute = bundle.getInt("minute");
        } else {
            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        }

        return new TimePickerDialog(
                getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    /**
     * Gets time from savedInstanceState
     *
     * @param savedInstanceState Instance to get the time from
     * @return New Bundle with hour and minute
     */
    private Bundle getTime(Bundle savedInstanceState) {
        String time = savedInstanceState.getString("time");
        assert time != null;

        String[] hourAndMinute = time.split(":");
        int hour = Integer.parseInt(hourAndMinute[0]);
        int minute = Integer.parseInt(hourAndMinute[1]);

        Bundle bundle = new Bundle();
        bundle.putInt("hour", hour);
        bundle.putInt("minute", minute);

        return bundle;
    }

    /**
     * When the fragment is initially shown (i.e. attached to the activity), cast the activity to
     * the callback interface type
     *
     * @param activity Activity to cast to the callback interface
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (TimePickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " +
                    TimePickedListener.class.getName());
        }
    }

    /**
     * When the time is selected, send it to the activity via its callback interface method
     *
     * @param view      The view associated with this listener.
     * @param hourOfDay The hour that was set.
     * @param minute    The minute that was set.
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);

        mListener.onTimePicked(c);
    }

    /**
     * Useful interface
     */
    public interface TimePickedListener {
        void onTimePicked(Calendar time);
    }
}
