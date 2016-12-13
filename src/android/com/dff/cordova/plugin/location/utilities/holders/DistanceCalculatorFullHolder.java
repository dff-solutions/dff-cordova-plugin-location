package com.dff.cordova.plugin.location.utilities.holders;

import android.location.Location;
import android.os.Handler;
import android.util.Log;
import com.dff.cordova.plugin.location.classes.DistanceCalculator;
import com.dff.cordova.plugin.location.resources.LocationResources;

import java.util.ArrayList;

/**
 * Created by anahas on 12.12.2016.
 *
 * @author Anthony Nahas
 * @version 2.0
 * @since 12.12.2016
 */
public class DistanceCalculatorFullHolder implements Runnable {

    private static final String TAG = "DistanceCalculatorFullHolder";
    private Handler mHandler;

    public DistanceCalculatorFullHolder(Handler mHandler) {
        this.mHandler = mHandler;
    }

    @Override
    public void run() {
        ArrayList<DistanceCalculator> list = LocationResources.getDistanceCalculatorFullList();
        Location lastGoodLocation = LocationResources.getLastGoodLocation();

        if (lastGoodLocation != null) {
            if (list != null && !list.isEmpty()) {
                int indexOfLastItem = list.size() - 1;
                DistanceCalculator distanceCalculator = new DistanceCalculator(list.get(indexOfLastItem).getEndLocation(),
                        lastGoodLocation); // try and catch IndexOutOfBoundsException
                Log.d(TAG, "distance calculator: " + distanceCalculator);
                LocationResources.addDistanceToFullList(distanceCalculator);

            } else {
                //only for the first record
                DistanceCalculator distanceCalculator = new DistanceCalculator(lastGoodLocation, lastGoodLocation);
                LocationResources.addDistanceToFullList(distanceCalculator);
            }
        }

        mHandler.postDelayed(this, LocationResources.DISTANCE_CALCULATOR_FULL_DELAY);
    }
}
