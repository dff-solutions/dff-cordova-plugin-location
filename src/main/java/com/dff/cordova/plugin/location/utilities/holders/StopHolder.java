package com.dff.cordova.plugin.location.utilities.holders;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.util.Log;
import com.dff.cordova.plugin.dagger2.annotations.ApplicationContext;
import com.dff.cordova.plugin.dagger2.annotations.Shared;
import com.dff.cordova.plugin.location.resources.Res;
import com.dff.cordova.plugin.location.resources.Resources;

import javax.inject.Inject;


/**
 * Class to hold the achieved distance between 2 intervals (stand still watcher).
 *
 * @author Anthony Nahas
 * @version 1.2
 * @since 27.02.2017
 */
public class StopHolder implements Runnable {

    private static final String TAG = "StopHolder";
    private Context mContext;
    private Res mRes;
    private Handler mHandler;
    private int mCounter = 0;

    // TODO: 15.08.2017
    private Location mLocation;


    @Inject
    public StopHolder
        (
            @ApplicationContext Context mContext,
            @Shared Res mRes) {
        this.mContext = mContext;
        this.mRes = mRes;
    }

    @Override
    public void run() {
        if (mLocation != null) {

            if (Resources.STOP_DISTANCE_CALCULATOR.getStartLocation() != null &&
                Resources.STOP_DISTANCE_CALCULATOR.getEndLocation() != null) {
                if (Resources.STOP_DISTANCE_CALCULATOR.getAchievedDistance(mLocation) < Resources.STOP_HOLDER_MIN_DISTANCE) {
                    mCounter++;
                    if (mCounter == Resources.STOP_HOLDER_COUNTER_LIMIT) {
                        mContext.sendBroadcast(new Intent(Resources.BROADCAST_ACTION_ON_STAND_STILL));
                        mCounter = 0;
                    }
                } else {
                    mCounter = 0;
                }
            } else {
                Resources.STOP_DISTANCE_CALCULATOR.init(mLocation);
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
