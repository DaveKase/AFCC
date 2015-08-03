package afcc.taavi.kase.afcc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import afcc.taavi.kase.afcc.R;

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

        setAds();
    }

    /**
     * Sets Google Ads to corresponding AdView
     */
    private void setAds() {
        AdView mAdView = (AdView) findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();

        String deviceId = getResourceString(R.string.device_id);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(deviceId).build();
        mAdView.loadAd(adRequest);
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
        Intent intent;

        switch (id) {
            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                break;
            case R.id.action_about:
                intent = new Intent(this, AboutActivity.class);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        startActivity(intent);
        return true;
    }

    /**
     * Called when a button is pressed in activity
     *
     * @param clickedButton Button that was clicked
     */
    public void onClick(View clickedButton) {
        Intent intent = null;

        switch (clickedButton.getId()) {
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
        } catch (NullPointerException e) {
            Log.e(TAG, "Error with starting activity, have you declared an intent");
        }
    }
}