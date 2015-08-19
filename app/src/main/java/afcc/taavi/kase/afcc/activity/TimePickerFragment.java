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

    /**
     * Creates dialog box
     *
     * @param savedInstanceState State of the saved instance
     * @return New time picker dialog box
     */
    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(
                getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
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
