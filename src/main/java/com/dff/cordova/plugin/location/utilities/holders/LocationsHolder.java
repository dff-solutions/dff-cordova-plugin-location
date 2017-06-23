package com.dff.cordova.plugin.location.utilities.holders;

import android.os.Handler;
import android.util.Log;

import com.dff.cordova.plugin.location.resources.Res;

import javax.inject.Inject;

/**
 * Class to hold the last good location within an interval of time.
 *
 * @author Anthony Nahas
 * @version 4.0
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
    @Inject
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
        if (Res.getLastGoodLocation() != null) {
            switch (Res.LOCATION_RETURN_TYPE) {
                case Res.DFF_STRING:
                    String location = Res.getLastGoodLocationAsString() + "|" +
                        Res.getLastGoodLocation().getTime();
                    Res.addLocationToListAsDffString(location);
                    Log.d(TAG, "Location has been added as (dffString) to the array list with size = "
                        + Res.getLocationListDffString().size());
                    break;
                case Res.JSON:
                    Res.addLocationToListAsJson(Res.getLastGoodLocationAsJson());
                    Log.d(TAG, "Location has been added as (JSON) to the array list with size = "
                        + Res.getLocationListJson().size());
                    break;
            }
        } else {
            Log.d(TAG, "The location is null and will not be added to the arraylist");
        }
        Log.d(TAG, "locationHandler with mCounter of " + mCounter++);
        mHandler.postDelayed(this, Res.LOCATION_DELAY);
    }
}
