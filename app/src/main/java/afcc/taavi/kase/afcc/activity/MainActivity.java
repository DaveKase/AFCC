package afcc.taavi.kase.afcc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import afcc.taavi.kase.afcc.AfccApplication;
import afcc.taavi.kase.afcc.R;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Taavi Kase
 *
 * Main activity
 */
public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private AdView mAdView;
    private Tracker mTracker;

    /**
     * Called when Activity is first created
     *
     * @param savedInstanceState May contain data supplied by saveInstanceState or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        AfccApplication application = (AfccApplication) getApplication();
        mTracker = application.getDefaultTracker();

        setAds();
    }

    /**
     * Sets Google Ads to corresponding AdView
     */
    private void setAds() {
        mAdView = (AdView) findViewById(R.id.adView);
        String deviceId = getResourceString(R.string.device_id);
        String secDeviceId = getResourceString(R.string.sec_device_id);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(deviceId)
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(secDeviceId)
                .build();

        mAdView.loadAd(adRequest);
    }

    /**
     * Called when activity is resumed
     */
    @Override
    public void onResume() {
        super.onResume();

        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        if (mAdView != null) {
            mAdView.resume();
        }
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

    /**
     * Called when activity is paused
     */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }

        super.onPause();
    }

    /**
     * Called when activity is destroyed
     */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }

        super.onDestroy();
    }
}