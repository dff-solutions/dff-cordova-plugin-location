package com.dff.cordova.plugin.location.utilities.holders;

import android.location.Location;
import android.os.Handler;
import com.dff.cordova.plugin.location.classes.DistanceCalculator;
import com.dff.cordova.plugin.location.resources.LocationResources;

import java.util.ArrayList;

/**
 * Created by anahas on 13.12.2016.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 13.12.2016
 */
public class DistanceCalculatorCustomHolder implements Runnable {


    private static final String TAG = "DistanceCalculatorCustomHolder";
    private Handler mHandler;


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
                DistanceCalculator distanceCalculator = new DistanceCalculator(list.get(indexOfLastItem).getEndLocation(),
                        lastGoodLocation);
                LocationResources.addDistanceToCustomList(distanceCalculator);
            } else {
                //only for the first record
                DistanceCalculator distanceCalculator = new DistanceCalculator(lastGoodLocation, lastGoodLocation);
                LocationResources.addDistanceToCustomList(distanceCalculator);
            }
        }

        mHandler.postDelayed(this, LocationResources.DISTANCE_CALCULATOR_CUSTOM_DELAY);
    }
}
