package com.dff.cordova.plugin.location.utilities;

import android.os.Handler;
import com.dff.cordova.plugin.location.resources.LocationResources;

/**
 * Created by anahas on 12.12.2016.
 */
public class DistanceCalculatorFullHolder implements Runnable {

    private static final String TAG = "DistanceCalculatorFullHolder";
    private Handler mHandler;

    public DistanceCalculatorFullHolder(Handler mHandler) {
        this.mHandler = mHandler;
    }

    @Override
    public void run() {

        mHandler.postDelayed(this, LocationResources.DISTANCE_CALCULATOR_FULL_DELAY);
    }
}
