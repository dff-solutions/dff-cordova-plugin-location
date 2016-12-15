package com.dff.cordova.plugin.location.utilities.holders;

import android.location.Location;
import android.os.Handler;
import android.util.Log;
import com.dff.cordova.plugin.location.resources.LocationResources;

/**
 * Created by anahas on 12.12.2016.
 *
 * @author Anthony Nahas
 * @version 3.1.2
 * @since 12.12.2016
 */
public class DistanceCalculatorFullHolder implements Runnable {

    private static final String TAG = "DistanceCalculatorFullHolder";
    private Handler mHandler;
    private int mCounter = 0;

    public DistanceCalculatorFullHolder(Handler mHandler) {
        this.mHandler = mHandler;
    }

    @Override
    public void run() {
        Location lastGoodLocation = LocationResources.getLastGoodLocation();

        if (lastGoodLocation != null && LocationResources.TOTAL_DISTANCE_CALCULATOR != null) {

            if (LocationResources.TOTAL_DISTANCE_CALCULATOR.getStartLocation() != null &&
                    LocationResources.TOTAL_DISTANCE_CALCULATOR.getEndLocation() != null) {
                LocationResources.TOTAL_DISTANCE_CALCULATOR.update(lastGoodLocation);
                Log.d(TAG, "dist calc with " + mCounter++ + " = " + LocationResources.TOTAL_DISTANCE_CALCULATOR.getDistance() + "m");
            } else {
                LocationResources.TOTAL_DISTANCE_CALCULATOR.init(lastGoodLocation);
                Log.d(TAG, "dist calc initial with  " + LocationResources.TOTAL_DISTANCE_CALCULATOR.getDistance() + "m");
            }
        }
        mHandler.postDelayed(this, LocationResources.DISTANCE_CALCULATOR_FULL_DELAY);
    }
}
