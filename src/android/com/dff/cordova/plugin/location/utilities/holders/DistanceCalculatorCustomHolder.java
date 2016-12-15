package com.dff.cordova.plugin.location.utilities.holders;

import android.location.Location;
import android.os.Handler;
import android.util.Log;
import com.dff.cordova.plugin.location.resources.LocationResources;

/**
 * Created by anahas on 13.12.2016.
 *
 * @author Anthony Nahas
 * @version 3.3.0
 * @since 13.12.2016
 */
public class DistanceCalculatorCustomHolder implements Runnable {


    private static final String TAG = "DistanceCalculatorCustomHolder";
    private Handler mHandler;
    private int mCounter = 0;

    public DistanceCalculatorCustomHolder(Handler mHandler) {
        this.mHandler = mHandler;
    }

    @Override
    public void run() {
        Location lastGoodLocation = LocationResources.getLastGoodLocation();

        if (lastGoodLocation != null && LocationResources.CUSTOM_DISTANCE_CALCULATOR != null) {

            if (LocationResources.CUSTOM_DISTANCE_CALCULATOR.getStartLocation() != null &&
                    LocationResources.CUSTOM_DISTANCE_CALCULATOR.getEndLocation() != null) {
                LocationResources.CUSTOM_DISTANCE_CALCULATOR.update(lastGoodLocation);
                Log.d(TAG, "dist calc with " + mCounter++ + " = " + LocationResources.CUSTOM_DISTANCE_CALCULATOR.getDistance() + "m");
            } else {
                LocationResources.CUSTOM_DISTANCE_CALCULATOR.init(lastGoodLocation);
                Log.d(TAG, "dist calc initial with  " + LocationResources.CUSTOM_DISTANCE_CALCULATOR.getDistance() + "m");
            }
        }
        mHandler.postDelayed(this, LocationResources.DISTANCE_CALCULATOR_CUSTOM_DELAY);
    }
}
