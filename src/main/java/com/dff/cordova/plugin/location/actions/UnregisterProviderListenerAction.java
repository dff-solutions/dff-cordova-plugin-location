package com.dff.cordova.plugin.location.actions;

import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;

import com.dff.cordova.plugin.location.broadcasts.ChangeProviderReceiver;
import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by anahas on 23.06.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 23.06.17
 */
@Singleton
public class UnregisterProviderListenerAction extends Action {

    private Context mContext;
    private ChangeProviderReceiver mChangeProviderReceiver;

    @Inject
    public UnregisterProviderListenerAction
        (@ApplicationContext Context mContext,
         ChangeProviderReceiver mChangeProviderReceiver
        ) {
        this.mContext = mContext;
        this.mChangeProviderReceiver = mChangeProviderReceiver;
    }

    @Override
    public void execute() {
        if (mChangeProviderReceiver != null) {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mChangeProviderReceiver);
            callbackContext.success();
        }
    }
}
