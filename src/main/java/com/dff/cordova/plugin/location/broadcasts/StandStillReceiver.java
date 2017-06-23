package com.dff.cordova.plugin.location.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.dff.cordova.plugin.common.AbstractPluginListener;
import com.dff.cordova.plugin.common.log.CordovaPluginLog;
import com.dff.cordova.plugin.location.resources.Res;
import com.dff.cordova.plugin.location.utilities.holders.StopHolder;
import org.apache.cordova.CallbackContext;

/**
 * Broadcast receiver that forward a success callback to JS
 *
 * @author Anthony Nahas
 * @version 1.1
 * @since 27.02.2017
 */
public class StandStillReceiver extends BroadcastReceiver {

    private static final String TAG = "StandStillReceiver";
    private Context mContext;
    private CallbackContext mCallbackContext;
    private Handler mStopHandler;
    private StopHolder mStopHolder;


    public StandStillReceiver(Context mContext, CallbackContext mCallbackContext) {
        this.mContext = mContext;
        this.mCallbackContext = mCallbackContext;
        runStopHolder();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        String action = intent.getAction();
        if (action.equals(Res.BROADCAST_ACTION_ON_STAND_STILL)) {
            AbstractPluginListener.sendPluginResult(mCallbackContext);
        } else if (action.equals(Res.BROADCAST_ACTION_STOP)) {
            stopStopHolder();
            context.unregisterReceiver(this);
        }
    }

    /**
     * run the stop holder to control an achieved distance within an interval time!
     */
    private void runStopHolder() {
        Log.d(TAG, "stop holder has been just started!");
        mStopHandler = new Handler(Looper.getMainLooper());
        mStopHolder = new StopHolder(mStopHandler, mContext);
        mStopHandler.postDelayed(mStopHolder, Res.DISTANCE_CALCULATOR_STOP_DELAY);
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
