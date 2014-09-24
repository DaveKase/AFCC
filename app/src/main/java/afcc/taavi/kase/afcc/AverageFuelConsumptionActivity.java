package afcc.taavi.kase.afcc;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;

/**
 * Created by Taavi Kase on 24.09.2014.
 *
 * Average fuel consumption activity
 */
public class AverageFuelConsumptionActivity extends BaseActivity  implements LoaderManager.LoaderCallbacks<Cursor>  {
    private String mAverageConsumption = "";
    private static final int SETTINGS_LOADER = 0;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState May contain data supplied by saveInstanceState or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_fuel_consumption);

        getLoaderManager().restartLoader(SETTINGS_LOADER, null, this);
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


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Uri settingsUri = Database.Settings.CONTENT_URI;
        switch(id) {
            case SETTINGS_LOADER:
                return new CursorLoader(this, settingsUri, null, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        switch(cursorLoader.getId()) {
            case SETTINGS_LOADER:
                if(cursor.getCount() > 0) {
                    cursor.moveToPosition(0);
                    int distance = cursor.getInt(cursor.getColumnIndex(Database.Settings.COL_DISTANCE));
                    int unit = cursor.getInt(cursor.getColumnIndex(Database.Settings.COL_UNIT));
                    setTexts(distance, unit);
                }

                break;
        }
    }

    private void setTexts(int distance, int unit) {
        TextView distanceText = (TextView) findViewById(R.id.kmText);
        TextView unitText = (TextView) findViewById(R.id.litreText);

        String distanceUnit = getDistanceUnit(distance);
        String amountUnit = getAmountUnit(unit);

        distanceText.setText(distanceUnit);
        unitText.setText(amountUnit);
    }

    private String getDistanceUnit(int distance) {
        switch (distance) {
            case DISTANCE_KM:
                return "kilometres";
            case DISTANCE_MILES:
                return "miles";
        }

        return "";
    }

    private String getAmountUnit(int unit) {
        switch (unit) {
            case UNIT_LITERS:
                return "litres";
            case UNIT_GALLONS_UK:
                return "gallons (UK)";
            case UNIT_GALLONS_USA:
                return "gallons (USA)";
        }

        return "";
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        // Nothing to do here
    }
}
