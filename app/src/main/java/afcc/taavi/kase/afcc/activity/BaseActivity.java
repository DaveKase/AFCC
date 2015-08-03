package afcc.taavi.kase.afcc.activity;

import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Taavi Kase.
 *
 * This class is used to contain constants and methods that would otherwise be repeated in
 * sub-activities.
 */
public class BaseActivity extends FragmentActivity {
    public static final int DISTANCE_KM = 0;
    public static final int DISTANCE_MILES = 1;

    public static final int UNIT_LITERS = 0;
    public static final int UNIT_GALLONS = 1;

    public static final int CONSUMPTION_L_100_KM = 0;
    public static final int CONSUMPTION_KM_L = 1;
    public static final int CONSUMPTION_MPG = 2;

    public static final int SPEED_KM_H = 0;
    public static final int SPEED_MPH = 1;

    /**
     * Called when user selects a menu item
     *
     * @param item Item that was selected
     * @return True if selection was handled, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Makes Toast messages with text specified.
     *
     * @param text Text to show
     */
    public void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * Rounds double values to two decimal places and returns String value of rounded double.
     *
     * @param value Value to round.
     * @return String value of rounded object
     */
    public String rounder(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        double result = bd.doubleValue();

        return String.valueOf(result);
    }

    /**
     * Returns parsed double from String object
     *
     * @param parseFrom String object to parse
     * @return Double object of parsed String
     */
    public double parser(String parseFrom) {
        return Double.parseDouble(parseFrom);
    }

    /**
     * Gets text from specified EditText
     *
     * @param textViewId ID of an EditText
     * @return String gotten from EditText
     */
    public String getTextFromEditText(int textViewId) {
        EditText editText = (EditText) findViewById(textViewId);
        return editText.getText().toString();
    }

    /**
     * Gets string value from resource id if exists
     *
     * @param id ID of an item to get
     * @return String value of the item
     */
    public String getResourceString(int id) {
        return getResources().getString(id);
    }
}