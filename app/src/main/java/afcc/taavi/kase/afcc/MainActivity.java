package afcc.taavi.kase.afcc;

import afcc.taavi.kase.afcc.Database.Settings;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by Taavi Kase
 *
 * Main activity
 */
public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    /**
     * Called when Activity is first created
     *
     * @param savedInstanceState May contain data supplied by saveInstanceState or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        insertDefaultSettingsValues();
    }

    /** Inserts default values to settings table */
    private void insertDefaultSettingsValues() {
        Uri settingsUri = Settings.CONTENT_URI;
        ContentValues values = new ContentValues();
        values.put(Settings._ID, 0);
        values.put(Settings.COL_DISTANCE, 0);
        values.put(Settings.COL_UNIT, 0);
        values.put(Settings.COL_CONSUMPTION, 0);

        ContentResolver content = getContentResolver();
        content.insert(settingsUri, values);
    }

    /**
     * Creates an options menu
     *
     * @param menu Options menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Called when an item is selected from options menu
     *
     * @param item Selected menu item
     * @return true if Settings menu was clicked
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when a button is pressed in activity
     *
     * @param clickedButton Button that was clicked
     */
    public void onClick(View clickedButton) {
        Intent intent = null;

        switch(clickedButton.getId()) {
            case R.id.fuelConsumptionButton:
                intent = new Intent(this, AverageFuelConsumptionActivity.class);
                break;
            case R.id.speedButton:
                intent = new Intent(this, SpeedometerActivity.class);
                break;
            case R.id.speedCalculatorButton:
                intent = new Intent(this, AverageSpeedActivity.class);
                break;
        }

        try {
            startActivity(intent);
        } catch(NullPointerException e) {
            Log.e(TAG, "Error with starting activity, have you declared an intent");
        }
    }
}