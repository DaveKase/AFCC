package afcc.taavi.kase.afcc.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import afcc.taavi.kase.afcc.AfccApplication;
import afcc.taavi.kase.afcc.R;
import afcc.taavi.kase.afcc.database.SettingsTable;

/**
 * Created by Taavi Kase
 *
 * Speedometer activity
 */
public class SpeedometerActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        BaseGpsListener {

    private static final String TAG = "SpeedometerActivity";
    private static final int UNIT_LOADER = 0;
    private static final int ERROR_NO_PERMISSION = 1;
    private static final int ERROR_NO_GPS = 2;
    private static final int ERROR_GENERAL_ERROR = 3;

    private int mUnit = 0;
    private int mErrorCode = 3;
    private Tracker mTracker;
    LocationManager mLocationManager;

    /**
     * Called when Activity is first created
     *
     * @param savedInstanceState May contain data supplied by saveInstanceState or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speedometer);

        AfccApplication application = (AfccApplication) getApplication();
        mTracker = application.getDefaultTracker();

        try {
            //noinspection ConstantConditions
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e(TAG, "No actionbar");
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initializeSpeedometer();
    }

    private void initializeSpeedometer() {
        showInitialScreen();
        getSpeedUnit();
        startSpeedometer();
    }

    private void showInitialScreen() {
        RelativeLayout errorView = findViewById(R.id.errorView);
        errorView.setVisibility(View.GONE);

        RelativeLayout progress = findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
    }

    /**
     * Called when Activity is about to pause
     */
    @Override
    public void onPause() {
        super.onPause();

        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mLocationManager.removeUpdates(this);
    }

    /**
     * Gets speed unit from database
     */
    private void getSpeedUnit() {
        getSupportLoaderManager().restartLoader(UNIT_LOADER, null, this);
    }

    /**
     * Starts location manager to listen to location change to get speed
     */
    private void startSpeedometer() {
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        try {
            assert mLocationManager != null;
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            updateSpeed(null);
        } catch (SecurityException se) {
            showErrorText(getResourceString(R.string.err_no_permission2));
            Log.e(TAG, "No permission to request location updates!" + se.getMessage());
            mErrorCode = ERROR_NO_PERMISSION;
        } catch (IllegalArgumentException iae) {
            showErrorText(getResourceString(R.string.err_no_gps_provider));
            Log.e(TAG, "No GPS Provider " + iae.getMessage());
            mErrorCode = ERROR_NO_GPS;
        } catch (NullPointerException npe) {
            showErrorText(getResourceString(R.string.err_npe));
            Log.e(TAG,"Error " + npe.getMessage());
            mErrorCode = ERROR_GENERAL_ERROR;
        }
    }

    /**
     * Updates speed text view
     *
     * @param location Instance of CustomLocation
     */
    public void updateSpeed(CustomLocation location) {
        int currentSpeed = 0;

        if (location != null) {
            RelativeLayout progress = findViewById(R.id.progress);
            progress.setVisibility(View.GONE);

            RelativeLayout speed = findViewById(R.id.speed);
            speed.setVisibility(View.VISIBLE);

            currentSpeed = (int) location.getSpeed();
        }

        TextView txtCurrentSpeed = this.findViewById(R.id.speedValue);
        txtCurrentSpeed.setText(String.valueOf(currentSpeed));
    }

    /**
     * Detects if unit used is km/h or mph
     *
     * @return true if km/h is used, false for mph
     */
    private boolean useMetricUnits() {
        return mUnit == SPEED_KM_H;
    }

    /**
     * Creates CursorLoaders
     *
     * @param id   CursorLoader ID
     * @param args May hold various data needed for queries
     * @return CursorLoader object
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case UNIT_LOADER:
                Uri settingsUri = SettingsTable.CONTENT_URI;
                String[] projection = {SettingsTable.COL_SPEED};
                return new CursorLoader(this, settingsUri, projection, null, null, null);
            default:
                return null;
        }
    }

    /**
     * Called after query is finished.
     *
     * @param loader CursorLoader object
     * @param cursor Holds data from database query
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case UNIT_LOADER:
                cursor.moveToFirst();
                setUnit(cursor);
                break;
        }
    }

    /**
     * Sets unit to unit text view
     *
     * @param cursor Cursor object that holds data that determines what unit should be used
     */
    private void setUnit(Cursor cursor) {
        mUnit = cursor.getInt(cursor.getColumnIndex(SettingsTable.COL_SPEED));
        String unitValue = getUnit(mUnit);

        TextView speedUnit = findViewById(R.id.speedUnit);
        speedUnit.setText(unitValue);
    }

    /**
     * Returns either km/h or mph based on query result
     *
     * @param unit If unit is 0 km/h is returned, if unit is 1 mph is returned
     * @return Unit as String
     */
    private String getUnit(int unit) {
        switch (unit) {
            case SPEED_KM_H:
                return "km/h";
            case SPEED_MPH:
                return "mph";
            default:
                return "";
        }
    }

    /**
     * Called when location changes
     *
     * @param location Instance of location class
     */
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            CustomLocation customLocation = new CustomLocation(location, this.useMetricUnits());
            this.updateSpeed(customLocation);
        }
    }

    /**
     * Called when activity is resumed
     */
    @Override
    public void onResume() {
        super.onResume();

        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        initializeSpeedometer();
    }

    /*
     * Called when there is exception in getting location fix. Shows error message on TextView, so
     * user can see what happened, instead of Toasting it for couple of seconds.
     *
     * @param errorMessage Message that is shown to user
     */
    private void showErrorText(String errorMessage) {
        RelativeLayout progress = findViewById(R.id.progress);
        progress.setVisibility(View.GONE);

        RelativeLayout speed = findViewById(R.id.speed);
        speed.setVisibility(View.GONE);

        RelativeLayout errorView = findViewById(R.id.errorView);
        errorView.setVisibility(View.VISIBLE);

        TextView errorText = findViewById(R.id.errorText);
        errorText.setText(errorMessage);

        Button errorButton = findViewById(R.id.errorButton);

        if(mErrorCode == ERROR_NO_PERMISSION) {
            errorButton.setVisibility(View.VISIBLE);
            errorButton.setText(getResourceString(R.string.speed_error_permission_text));
        } else if (mErrorCode == ERROR_GENERAL_ERROR) {
            errorButton.setVisibility(View.VISIBLE);
            errorButton.setText(getResourceString(R.string.speed_error_try_again_text));
        } else {
            errorButton.setVisibility(View.GONE);
        }
    }

    public void onSettingsButtonClick(View v) {
        switch(mErrorCode) {
            case ERROR_NO_PERMISSION:
                getPermissionFromSettings();
                break;
            case ERROR_GENERAL_ERROR:
                initializeSpeedometer();
                break;
            case ERROR_NO_GPS:
                // Have no idea what else to do here
                break;
            default:
                // Same problem here
                break;
        }

    }

    private void getPermissionFromSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    /**
     * Unused methods
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onGpsStatusChanged(int event) {}
}
