package com.dff.cordova.plugin.location.utilities.holders;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.resources.Res;
import com.dff.cordova.plugin.location.resources.Resources;

import android.os.Handler;

import javax.inject.Inject;


/**
 * Class to hold the achieved distance between 2 intervals (stand still watcher).
 *
 * @author Anthony Nahas
 * @version 1.1
 * @since 27.02.2017
 */
public class StopHolder implements Runnable {

    private static final String TAG = "StopHolder";
    private Context mContext;
    private Res mRes;
    private Handler mHandler;
    private int mCounter = 0;


    @Inject
    public StopHolder
        (
            @ApplicationContext Context mContext,
            Res mRes) {
        this.mContext = mContext;
        this.mRes = mRes;
    }

    @Override
    public void run() {
        Location lastGoodLocation = mRes.getLocation();
        if (lastGoodLocation != null && Resources.STOP_DISTANCE_CALCULATOR != null) {

            if (Resources.STOP_DISTANCE_CALCULATOR.getStartLocation() != null &&
                Resources.STOP_DISTANCE_CALCULATOR.getEndLocation() != null) {
                if (Resources.STOP_DISTANCE_CALCULATOR.getAchievedDistance(lastGoodLocation) < Resources.STOP_HOLDER_MIN_DISTANCE) {
                    mCounter++;
                    if (mCounter == Resources.STOP_HOLDER_COUNTER_LIMIT) {
                        mContext.sendBroadcast(new Intent(Resources.BROADCAST_ACTION_ON_STAND_STILL));
                        mCounter = 0;
                    }
                } else {
                    mCounter = 0;
                }
            } else {
                Resources.STOP_DISTANCE_CALCULATOR.init(lastGoodLocation);
                Log.d(TAG, "dist calc initial with  " + Resources.STOP_DISTANCE_CALCULATOR.getDistance() + "m");
            }
        }
        mHandler.postDelayed(this, Resources.DISTANCE_CALCULATOR_STOP_DELAY);
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void setHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }
}
