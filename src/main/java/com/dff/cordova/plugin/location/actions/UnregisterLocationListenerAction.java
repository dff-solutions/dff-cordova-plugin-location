package com.dff.cordova.plugin.location.actions;

import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import com.dff.cordova.plugin.dagger2.abstracts.Action;
import com.dff.cordova.plugin.dagger2.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.broadcasts.NewLocationReceiver;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Action that deals with the local broadcast manager in order to unregister
 * the new_location broadcast receiver
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 04.07.17
 */
@Singleton
public class UnregisterLocationListenerAction extends Action {

    private Context mContext;
    private NewLocationReceiver mNewLocationReceiver;

    @Inject
    public UnregisterLocationListenerAction
        (
            @ApplicationContext Context mContext,
            NewLocationReceiver mNewLocationReceiver) {
        this.mContext = mContext;
        this.mNewLocationReceiver = mNewLocationReceiver;
    }

    @Override
    public void execute() {
        LocalBroadcastManager
            .getInstance(mContext)
            .unregisterReceiver(mNewLocationReceiver);

        getCallbackContext().success();
    }
}
