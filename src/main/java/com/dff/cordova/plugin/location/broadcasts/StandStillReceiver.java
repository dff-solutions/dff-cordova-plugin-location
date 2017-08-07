package com.dff.cordova.plugin.location.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.dff.cordova.plugin.common.AbstractPluginListener;
import com.dff.cordova.plugin.common.log.CordovaPluginLog;
import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.resources.Resources;
import com.dff.cordova.plugin.location.utilities.holders.StopHolder;

import org.apache.cordova.CallbackContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Broadcast receiver that forward a success callback to JS
 *
 * @author Anthony Nahas
 * @version 1.1
 * @since 27.02.2017
 */
@Singleton
public class StandStillReceiver extends BroadcastReceiver {

    private static final String TAG = "StandStillReceiver";
    private CallbackContext mCallbackContext;
    private Handler mStopHandler;
    private StopHolder mStopHolder;

    // TODO: 07.08.2017 to test
    @Inject
    public StandStillReceiver(StopHolder mStopHolder) {
        this.mStopHolder = mStopHolder;
        runStopHolder();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        String action = intent.getAction();
        if (action.equals(Resources.BROADCAST_ACTION_ON_STAND_STILL)) {
            AbstractPluginListener.sendPluginResult(mCallbackContext);
        } else if (action.equals(Resources.BROADCAST_ACTION_STOP)) {
            stopStopHolder();
            context.unregisterReceiver(this);
        }
    }

    public void setCallbackContext(CallbackContext mCallbackContext) {
        this.mCallbackContext = mCallbackContext;
    }

    /**
     * run the stop holder to control an achieved distance within an interval time!
     */
    private void runStopHolder() {
        Log.d(TAG, "stop holder has been just started!");
        mStopHandler = new Handler(Looper.getMainLooper());
        mStopHolder.setHandler(mStopHandler);
        mStopHandler.postDelayed(mStopHolder, Resources.DISTANCE_CALCULATOR_STOP_DELAY);
    }

    /**
     * Stop the stop holder
     */
    private void stopStopHolder() {
        Log.d(TAG, "Stop the stop holder!");
        try {
            if (mStopHandler != null) {
                mStopHandler.removeCallbacksAndMessages(null);
            }
        } catch (NullPointerException e) {
            CordovaPluginLog.e(TAG, "Error: ", e);
        }
    }

}
