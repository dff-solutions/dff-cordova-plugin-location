package com.dff.cordova.plugin.location.utilities;

import android.os.Handler;
import android.util.Log;
import com.dff.cordova.plugin.location.resources.LocationResources;

/**
 * Created by anahas on 01.12.2016.
 *
 * @author Anthony Nahas
 * @version 0.9
 * @since 01.12.2016
 */
public class LocationsHolder implements Runnable {

    private static final String TAG = "LocationHolder";
    private Handler mHandler;
    int counter = 0;

    public LocationsHolder(Handler mHandler){
        this.mHandler = mHandler;
    }

    @Override
    public void run() {
       if(LocationResources.getLastGoodLocation() != null){
           String location = LocationResources.getLastGoodLocationToString() +
                   LocationResources.getLastGoodLocation().getTime();
           LocationResources.addLocationToList(location);
       }
        Log.d(TAG, "locationHandler with counter of " + counter++);
       mHandler.postDelayed(this, LocationResources.LOCATION_DELAY);
    }
}
