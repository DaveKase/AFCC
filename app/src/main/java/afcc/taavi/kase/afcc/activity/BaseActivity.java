package afcc.taavi.kase.afcc.activity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.app.Activity;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Taavi Kase.
 *
 * This class is used to contain methods that would otherwise be repeated in sub-activities.
 */
public class BaseActivity extends Activity {
    public static final int DISTANCE_KM = 0;
    public static final int DISTANCE_MILES = 1;

    public static final int UNIT_LITERS = 0;
    public static final int UNIT_GALLONS_USA = 1;
    public static final int UNIT_GALLONS_UK = 2;

    public static final int CONSUMPTION_L_100_KM = 0;
    public static final int CONSUMPTION_KM_L = 1;
    public static final int CONSUMPTION_MPG_USA = 2;
    public static final int CONSUMPTION_MPG_UK = 3;

    public static final int SPEED_KM_H = 0;
    public static final int SPED_MPH = 1;

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
}