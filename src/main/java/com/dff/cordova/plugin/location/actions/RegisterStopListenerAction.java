package com.dff.cordova.plugin.location.actions;

import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.dff.cordova.plugin.location.broadcasts.StandStillReceiver;
import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.resources.Resources;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Send broadcast receiver to set the stop listener
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 26.06.17
 */
@Singleton
public class RegisterStopListenerAction extends Action {

    private Context mContext;
    private StandStillReceiver mStandStillReceiver;

    @Inject
    public RegisterStopListenerAction
        (
            @ApplicationContext Context mContext,
            StandStillReceiver mStandStillReceiver
        ) {
        this.mContext = mContext;
        this.mStandStillReceiver = mStandStillReceiver;
    }

    @Override
    public void execute() {
        mStandStillReceiver.setCallbackContext(callbackContext);
        Resources.STOP_HOLDER_COUNTER_LIMIT = args.optInt(0, Resources.STOP_HOLDER_COUNTER_LIMIT);
        Resources.STOP_HOLDER_MIN_DISTANCE = args.optInt(1, Resources.STOP_HOLDER_MIN_DISTANCE);
        Resources.STOP_HOLDER_DELAY = args.optInt(2, Resources.STOP_HOLDER_DELAY);

        LocalBroadcastManager.getInstance(mContext).registerReceiver(mStandStillReceiver,
            new IntentFilter(Resources.BROADCAST_ACTION_ON_STAND_STILL));
    }
}
