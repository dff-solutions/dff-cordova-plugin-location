package com.dff.cordova.plugin.location.utilities.holders;

import android.location.Location;
import android.os.Handler;
import android.util.Log;
import com.dff.cordova.plugin.location.classes.DistanceCalculator;
import com.dff.cordova.plugin.location.resources.LocationResources;

import java.util.ArrayList;

/**
 * Created by anahas on 13.12.2016.
 *
 * @author Anthony Nahas
 * @version 3.0.0
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
        ArrayList<DistanceCalculator> list = LocationResources.getDistanceCalculatorCustomList();
        Location lastGoodLocation = LocationResources.getLastGoodLocation();

        if (lastGoodLocation != null) {
            if (!list.isEmpty()) {
                int indexOfLastItem = list.size() - 1;
                Log.d(TAG, "indexOfLastItem = " + indexOfLastItem);
                DistanceCalculator distanceCalculator = new DistanceCalculator(list.get(indexOfLastItem).getEndLocation(),
                        lastGoodLocation);
                LocationResources.addDistanceToCustomList(distanceCalculator);
                Log.d(TAG, "distance calculator " + counter++ + " = " + distanceCalculator);
                Log.d(TAG, "a dist calc has been added");
            } else {
                //only for the first record
                Log.d(TAG, "the list is empty and the first dist calc will be now added");
                DistanceCalculator distanceCalculator = new DistanceCalculator(lastGoodLocation, lastGoodLocation);
                LocationResources.addDistanceToCustomList(distanceCalculator);
                Log.d(TAG, "the first distance calculator " + counter++ + " = " + distanceCalculator);
            }
        }
        mHandler.postDelayed(this, LocationResources.DISTANCE_CALCULATOR_CUSTOM_DELAY);
    }
}
