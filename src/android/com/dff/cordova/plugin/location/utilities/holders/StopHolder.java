package com.dff.cordova.plugin.location.utilities.holders;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import com.dff.cordova.plugin.location.resources.LocationResources;
import android.os.Handler;


/**
 * Class to hold the achieved distance between 2 intervals (stand still watcher).
 *
 * @author Anthony Nahas
 * @version 1.1
 * @since 27.02.2017
 */
public class StopHolder implements Runnable {

    private static final String TAG = "StopHolder";
    private Handler mHandler;
    private Context mContext;
    private int mCounter = 0;


    public StopHolder(Handler mHandler, Context mContext) {
        this.mHandler = mHandler;
        this.mContext = mContext;
    }

    @Override
    public void run() {
        Location lastGoodLocation = LocationResources.getLastGoodLocation();
        if (lastGoodLocation != null && LocationResources.STOP_DISTANCE_CALCULATOR != null) {

            if (LocationResources.STOP_DISTANCE_CALCULATOR.getStartLocation() != null &&
                LocationResources.STOP_DISTANCE_CALCULATOR.getEndLocation() != null) {
                if (LocationResources.STOP_DISTANCE_CALCULATOR.getAchievedDistance(lastGoodLocation) < LocationResources.STOP_HOLDER_MIN_DISTANCE) {
                    mCounter++;
                    if (mCounter == LocationResources.STOP_HOLDER_COUNTER_LIMIT) {
                        mContext.sendBroadcast(new Intent(LocationResources.BROADCAST_ACTION_ON_STAND_STILL));
                        mCounter = 0;
                    }
                } else {
                    mCounter = 0;
                }
            } else {
                LocationResources.STOP_DISTANCE_CALCULATOR.init(lastGoodLocation);
                Log.d(TAG, "dist calc initial with  " + LocationResources.CUSTOM_DISTANCE_CALCULATOR.getDistance() + "m");
            }
        }
        mHandler.postDelayed(this, LocationResources.DISTANCE_CALCULATOR_STOP_DELAY);
    }
}
