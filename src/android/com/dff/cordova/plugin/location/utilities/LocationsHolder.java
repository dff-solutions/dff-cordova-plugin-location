package com.dff.cordova.plugin.location.utilities;

import android.os.Handler;
import android.util.Log;
import com.dff.cordova.plugin.location.resources.LocationResources;

/**
 * Class to hold the last good location within an interval of time.
 *
 * @author Anthony Nahas
 * @version 0.9
 * @since 01.12.2016
 */
public class LocationsHolder implements Runnable {

    private static final String TAG = "LocationHolder";
    private Handler mHandler;
    int counter = 0;

    /**
     * Custom Constructor
     *
     * @param mHandler The handler that posts the delay
     */
    public LocationsHolder(Handler mHandler) {
        this.mHandler = mHandler;
    }

    /**
     * Repeat every x time.
     * Verify whether the last good location != null. Then hold it within x time.
     * <p>
     * x = delay time
     */
    @Override
    public void run() {
        if (LocationResources.getLastGoodLocation() != null) {
            String location = LocationResources.getLastGoodLocationToString() +
                    LocationResources.getLastGoodLocation().getTime();
            LocationResources.addLocationToList(location);
        }
        Log.d(TAG, "locationHandler with counter of " + counter++);
        mHandler.postDelayed(this, LocationResources.LOCATION_DELAY);
    }
}
