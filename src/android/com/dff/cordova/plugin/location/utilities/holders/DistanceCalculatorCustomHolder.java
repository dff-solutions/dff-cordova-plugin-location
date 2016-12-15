package com.dff.cordova.plugin.location.utilities.holders;

import android.location.Location;
import android.os.Handler;
import com.dff.cordova.plugin.location.resources.LocationResources;

/**
 * Created by anahas on 13.12.2016.
 *
 * @author Anthony Nahas
 * @version 3.1.2
 * @since 13.12.2016
 */
public class DistanceCalculatorCustomHolder implements Runnable {


    private static final String TAG = "DistanceCalculatorCustomHolder";
    private Handler mHandler;
    int counter = 0;

    public DistanceCalculatorCustomHolder(Handler mHandler) {
        this.mHandler = mHandler;
    }

    @Override
    public void run() {
        Location lastGoodLocation = LocationResources.getLastGoodLocation();

        if (lastGoodLocation != null) {

        }
        mHandler.postDelayed(this, LocationResources.DISTANCE_CALCULATOR_CUSTOM_DELAY);
    }
}
