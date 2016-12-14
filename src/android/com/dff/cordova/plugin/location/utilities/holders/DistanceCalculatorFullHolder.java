package com.dff.cordova.plugin.location.utilities.holders;

import android.location.Location;
import android.os.Handler;
import android.util.Log;
import com.dff.cordova.plugin.location.classes.DistanceCalculator;
import com.dff.cordova.plugin.location.classes.RouteCalculator;
import com.dff.cordova.plugin.location.resources.LocationResources;

import java.util.ArrayList;

/**
 * Created by anahas on 12.12.2016.
 *
 * @author Anthony Nahas
 * @version 3.0.1
 * @since 12.12.2016
 */
public class DistanceCalculatorFullHolder implements Runnable {

    private static final String TAG = "DistanceCalculatorFullHolder";
    private Handler mHandler;
    int counter = 0;

    public DistanceCalculatorFullHolder(Handler mHandler) {
        this.mHandler = mHandler;
    }

    @Override
    public void run() {
        ArrayList<DistanceCalculator> list = LocationResources.getDistanceCalculatorFullList();
        Location lastGoodLocation = LocationResources.getLastGoodLocation();
        RouteCalculator routeCalculator = LocationResources.getTotalRouteCalculator();

        if (lastGoodLocation != null && routeCalculator != null) {

            if (routeCalculator.getOldDistanceCalculator() != null && routeCalculator.getNewDistanceCalculator() != null) {
                DistanceCalculator distanceCalculator = new DistanceCalculator
                        (routeCalculator.getOldDistanceCalculator().getEndLocation(), lastGoodLocation);
                routeCalculator.setNewDistanceCalculator(distanceCalculator);
                Log.d(TAG, "route calc with " + counter + " " + routeCalculator);
            } else {
                routeCalculator.setInitialDistance(new DistanceCalculator(lastGoodLocation, lastGoodLocation));
                Log.d(TAG, "route calc initial: " + routeCalculator);
            }


            if (list != null && !list.isEmpty()) {
                int indexOfLastItem = list.size() - 1;
                Log.d(TAG, "indexOfLastItem = " + indexOfLastItem);
                DistanceCalculator distanceCalculator = new DistanceCalculator(list.get(indexOfLastItem).getEndLocation(),
                        lastGoodLocation); // try and catch IndexOutOfBoundsException
                LocationResources.addDistanceToFullList(distanceCalculator);
                Log.d(TAG, "distance calculator " + counter++ + " = " + distanceCalculator);
                Log.d(TAG, "a dist calc has been added");
            } else {
                //only for the first record
                Log.d(TAG, "the list is empty and the first dist calc will be now added");
                DistanceCalculator distanceCalculator = new DistanceCalculator(lastGoodLocation, lastGoodLocation);
                LocationResources.addDistanceToFullList(distanceCalculator);
                Log.d(TAG, "the first distance calculator " + counter++ + " = " + distanceCalculator);
            }
        }
        mHandler.postDelayed(this, LocationResources.DISTANCE_CALCULATOR_FULL_DELAY);
    }
}
