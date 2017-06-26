package com.dff.cordova.plugin.location.actions;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.resources.Res;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Send broadcast receiver to stop the stop listener
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 26.06.17
 */
@Singleton
public class UnregisterStopListenerAction extends Action {

    private Context mContext;

    @Inject
    public UnregisterStopListenerAction(@ApplicationContext Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void execute() {
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(Res.BROADCAST_ACTION_STOP));
    }
}
