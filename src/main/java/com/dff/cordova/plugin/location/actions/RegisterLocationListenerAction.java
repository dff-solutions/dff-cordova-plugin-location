package com.dff.cordova.plugin.location.actions;

import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.dff.cordova.plugin.common.action.Action;
import com.dff.cordova.plugin.location.broadcasts.NewLocationReceiver;
import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.resources.Resources;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Action that deals with the local broadcast manager in order to register
 * the new_location broadcast receiver
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 28.06.17
 */
@Singleton
public class RegisterLocationListenerAction extends Action {

    private static final String TAG = RegisterLocationListenerAction.class.getSimpleName();

    private Context mContext;
    private NewLocationReceiver mNewLocationReceiver;

    @Inject
    public RegisterLocationListenerAction
        (
            @ApplicationContext Context mContext,
            NewLocationReceiver mNewLocationReceiver
        ) {
        this.mContext = mContext;
        this.mNewLocationReceiver = mNewLocationReceiver;
    }

    @Override
    public void execute() {
        mNewLocationReceiver.setCallbackContext(callbackContext);

        LocalBroadcastManager
            .getInstance(mContext)
            .registerReceiver(mNewLocationReceiver, new IntentFilter(Resources.BROADCAST_ACTION_ON_NEW_LOCATION));
    }
}
