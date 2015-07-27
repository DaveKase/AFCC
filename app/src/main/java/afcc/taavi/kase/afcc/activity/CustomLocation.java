package afcc.taavi.kase.afcc.activity;

import android.location.Location;

/**
 * Created by Taavi on 27.07.2015.
 *
 * Provides speed based from Location class
 */
public class CustomLocation extends Location {
    //private static final String TAG = "CustomLocation";
    private boolean mUseMetricUnits = true;

    /**
     * Constructor
     *
     * @param location       Instance of Location class
     * @param useMetricUnits Sets the use of metric or imperial units for speed
     */
    public CustomLocation(Location location, boolean useMetricUnits) {
        super(location);
        mUseMetricUnits = useMetricUnits;
    }

    /**
     * Gets speed from superclass and converts it either to mph or km/h
     */
    @Override
    public float getSpeed() {
        float speed = super.getSpeed();

        if (!mUseMetricUnits) {
            // Convert m/s to mph
            speed = speed * 2.2369f;
        } else {
            // Convert m/s to km/h
            speed = (speed * 3600) / 100;
        }

        return speed;
    }
}
