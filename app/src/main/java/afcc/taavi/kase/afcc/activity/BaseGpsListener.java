package afcc.taavi.kase.afcc.activity;

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by Taavi on 27.07.2015.
 *
 * Provides location listener interface
 */
public interface BaseGpsListener extends LocationListener, GpsStatus.Listener {
    void onLocationChanged(Location location);
    void onProviderDisabled(String provider);
    void onProviderEnabled(String provider);
    void onStatusChanged(String provider, int status, Bundle extras);
    void onGpsStatusChanged(int event);
}
