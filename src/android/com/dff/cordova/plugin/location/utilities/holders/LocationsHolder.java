package com.dff.cordova.plugin.location.utilities.holders;

import android.os.Handler;
import android.util.Log;
import com.dff.cordova.plugin.location.resources.LocationResources;

/**
 * Class to hold the last good location within an interval of time.
 *
 * @author Anthony Nahas
 * @version 2.0
 * @since 01.12.2016
 */
public class LocationsHolder implements Runnable {

    private static final String TAG = "LocationHolder";
    private Handler mHandler;
    private int mCounter = 0;

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
            switch (LocationResources.LOCATION_RETURN_TYPE) {
                case LocationResources.DFF_STRING:
                    String location = LocationResources.getLastGoodLocationAsString() + "|" +
                        LocationResources.getLastGoodLocation().getTime();
                    LocationResources.addLocationToListAsDffString(location);
                    Log.d(TAG, "Location has been added as (dffString) to the array list with size = "
                        + LocationResources.getLocationListDffString().size());
                    break;
                case LocationResources.JSON:
                    LocationResources.addLocationToListAsJson(LocationResources.getLastGoodLocationAsJson());
                    Log.d(TAG, "The location is null and will not be added to the arraylist with size = "
                        + LocationResources.getLocationListJson().size());
                    break;
            }
        } else {
            Log.d(TAG, "The location is null and will not be added to the arraylist");
        }
        Log.d(TAG, "locationHandler with mCounter of " + mCounter++);
        mHandler.postDelayed(this, LocationResources.LOCATION_DELAY);
    }
}
