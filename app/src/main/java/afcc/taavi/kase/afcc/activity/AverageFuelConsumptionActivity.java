package afcc.taavi.kase.afcc.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import afcc.taavi.kase.afcc.R;
import afcc.taavi.kase.afcc.database.PreviousResultsTable;
import afcc.taavi.kase.afcc.database.SettingsTable;

/**
 * Created by Taavi Kase on 24.09.2014.
 *
 * Average fuel consumption activity
 */
public class AverageFuelConsumptionActivity extends BaseActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static String TAG = "AverageFuelConsumptionActivity";
    private static final int SETTINGS_LOADER = 0;
    private String mAverageConsumption = "";
    private String mUnit = "";
    private int mCalculationType = 0;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState May contain data supplied by saveInstanceState or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_fuel_consumption);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e(TAG, "No actionbar");
        }

        getSupportLoaderManager().restartLoader(SETTINGS_LOADER, null, this);
    }

    /**
     * Called when a button is clicked
     *
     * @param clickedButton Object of a clicked button
     */
    public void onClick(View clickedButton) {
        switch (clickedButton.getId()) {
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
    private void calculate() {
        try {
            String result = "";
            switch(mCalculationType) {
                case CONSUMPTION_L_100_KM:
                    result = calculateL100Km();
                    break;
                case CONSUMPTION_KM_L: case CONSUMPTION_MPG:
                    result = calculateDistancePerAmount();
                    break;
                default:
                    makeToast("Did not find calculation type");
            }

            showResult(result);
        } catch (NumberFormatException e) {
            catcher();
        }
    }

    /**
     * Calculates fuel consumption for l / 100km calculation type
     *
     * @return Result as string
     */
    private String calculateL100Km() {
        double distance = parser(getTextFromEditText(R.id.distanceEdit));
        double fuel = parser(getTextFromEditText(R.id.fuelEdit));
        double value = (fuel * 100) / distance;
        return rounder(value);
    }

    /**
     * Calculates fuel consumption for l / km calculation type
     *
     * @return Result as string
     */
    private String calculateDistancePerAmount() {
        double distance = parser(getTextFromEditText(R.id.distanceEdit));
        double fuel = parser(getTextFromEditText(R.id.fuelEdit));
        double value = distance / fuel;
        return rounder(value);
    }

    /**
     * Makes result TextViews visible and shows result
     *
     * @param result Result to show
     */
    private void showResult(String result) {
        TextView averageText = (TextView) findViewById(R.id.averageText);
        TextView averageResultText = (TextView) findViewById(R.id.averageResultText);
        mUnit = getResultUnit();
        mAverageConsumption = result + " " + mUnit;

        averageResultText.setText(mAverageConsumption);
        averageText.setVisibility(View.VISIBLE);
        averageResultText.setVisibility(View.VISIBLE);
    }

    /**
     * Called when there is a double parsing exception
     */
    private void catcher() {
        String text;

        if (getTextFromEditText(R.id.distanceEdit).equals("")) {
            text = "Insert value to distance travelled box!";
        } else if (getTextFromEditText(R.id.fuelEdit).equals("")) {
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
        if (!mAverageConsumption.equals("")) {
            saveResults();

            TextView averageText = (TextView) findViewById(R.id.averageText);
            TextView averageResultText = (TextView) findViewById(R.id.averageResultText);
            EditText distanceEdit = (EditText) findViewById(R.id.distanceEdit);
            EditText fuelEdit = (EditText) findViewById(R.id.fuelEdit);

            mAverageConsumption = "";

            averageResultText.setText(mAverageConsumption);
            distanceEdit.setText("");
            fuelEdit.setText("");
            averageText.setVisibility(View.GONE);
            averageResultText.setVisibility(View.GONE);
        } else {
            makeToast("Nothing to save");
        }
    }

    /**
     * If there are results to save, this method is called to save the results to database
     */
    private void saveResults() {
        Date date = new Date();
        String dateString = new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(date);

        String[] consumptionAndUnit = mAverageConsumption.split(" ");
        String consumption = consumptionAndUnit[0];

        ContentValues values = new ContentValues();
        values.put(PreviousResultsTable.COL_DATE, dateString);
        values.put(PreviousResultsTable.COL_RESULT, consumption);
        values.put(PreviousResultsTable.COL_UNIT, mUnit);
        values.put(PreviousResultsTable.COL_ROW, "0");

        ContentResolver resolver = getContentResolver();
        resolver.insert(PreviousResultsTable.CONTENT_URI, values);

        makeToast(mAverageConsumption + " saved!");
    }

    /**
     * Shows previous results
     */
    private void show() {
        Intent intent = new Intent(this, PreviousResultsActivity.class);
        startActivity(intent);
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
        Uri settingsUri = SettingsTable.CONTENT_URI;

        switch (id) {
            case SETTINGS_LOADER:
                return new CursorLoader(this, settingsUri, null, null, null, null);
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
            case SETTINGS_LOADER:
                if (cursor.getCount() > 0) {
                    cursor.moveToPosition(0);
                    int distance = cursor.getInt(cursor.getColumnIndex(SettingsTable.COL_DISTANCE));
                    int unit = cursor.getInt(cursor.getColumnIndex(SettingsTable.COL_UNIT));
                    mCalculationType = cursor.getInt(cursor.getColumnIndex(SettingsTable.COL_CONSUMPTION));
                    setTexts(distance, unit);
                }

                break;
        }
    }

    /**
     * Sets texts to unit TextViews
     *
     * @param distance Integer representation of distance unit
     * @param unit     Integer representation of amount unit
     */
    private void setTexts(int distance, int unit) {
        TextView distanceText = (TextView) findViewById(R.id.kmText);
        TextView unitText = (TextView) findViewById(R.id.litreText);

        String distanceUnit = getDistanceUnit(distance);
        String amountUnit = getAmountUnit(unit);

        distanceText.setText(distanceUnit);
        unitText.setText(amountUnit);
    }

    /**
     * Returns string distance unit name
     *
     * @param distance Integer representation of distance unit
     * @return Unit name
     */
    private String getDistanceUnit(int distance) {
        switch (distance) {
            case DISTANCE_KM:
                return "kilometres";
            case DISTANCE_MILES:
                return "miles";
        }

        return "";
    }

    /**
     * Returns amount unit name
     *
     * @param unit Integer representation of amount unit
     * @return Unit name
     */
    private String getAmountUnit(int unit) {
        switch (unit) {
            case UNIT_LITERS:
                return "litres";
            case UNIT_GALLONS:
                return "gallons";
        }

        return "";
    }

    /**
     * Returns unit name for calculation type
     *
     * @return Unit name based on calculation type
     */
    private String getResultUnit() {
        switch (mCalculationType) {
            case CONSUMPTION_L_100_KM:
                return "l/100 km";
            case CONSUMPTION_KM_L:
                return "km/l";
            case CONSUMPTION_MPG:
                return "mpg";
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
