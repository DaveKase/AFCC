package afcc.taavi.kase.afcc;

import android.os.Bundle;
import android.widget.TextView;
import android.view.View;

/**
 * Created by Taavi Kase on 24.09.2014.
 *
 * Average fuel consumption activity
 */
public class AverageFuelConsumptionActivity extends BaseActivity {
    private String mAverageConsumption = "";

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState May contain data supplied by saveInstanceState or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_fuel_consumption);
    }

    /**
     * Called when a button is clicked
     *
     * @param clickedButton Object of a clicked button
     */
    public void onClick(View clickedButton) {
        switch(clickedButton.getId()) {
            case R.id.calculateButton:
                calculate();
                break;
            case R.id.saveButton:
                save();
                break;
            case R.id.previousResultsButton:
                show();
                break;
        }
    }

    /**
     * Gets the values from EditTexts, performs average fuel consumption calculation and shows the
     * result
     */
    public void calculate() {
        try {
            double distance = parser(getTextFromEditText(R.id.distanceEdit));
            double fuel = parser(getTextFromEditText(R.id.fuelEdit));
            double value = (fuel * 100) / distance;
            String result = rounder(value);
            showResult(result);
        } catch (NumberFormatException e) {
            catcher();
        }
    }

    /**
     * Makes result TextViews visible and shows result
     *
     * @param result Result to show
     */
    private void showResult(String result) {
        TextView averageText = (TextView) findViewById(R.id.averageText);
        TextView averageResultText = (TextView) findViewById(R.id.averageResultText);
        mAverageConsumption = result + " l / 100 km";

        averageResultText.setText(mAverageConsumption);
        averageText.setVisibility(View.VISIBLE);
        averageResultText.setVisibility(View.VISIBLE);
    }

    /**
     * Called when there is a double parsing exception
     */
    private void catcher() {
        String text;

        if(getTextFromEditText(R.id.distanceEdit).equals("")) {
            text = "Insert value to distance travelled box!";
        } else if(getTextFromEditText(R.id.fuelEdit).equals("")) {
            text = "Insert value to fuel box!";
        } else {
            text = "Wrong values used, try again!";
        }

        makeToast(text);
    }

    /**
     * Saves the result to database
     */
    private void save() {
        if(!mAverageConsumption.equals("")) {
            makeToast(mAverageConsumption + " saved!");
        } else {
            makeToast("Nothing to save");
        }
    }

    /**
     * Shows previous results
     */
    private void show() {
        makeToast("Show clicked");
    }
}
