package com.dff.cordova.plugin.location.actions;

import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import com.dff.cordova.plugin.dagger2.abstracts.Action;
import com.dff.cordova.plugin.dagger2.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.broadcasts.ChangeProviderReceiver;
import com.dff.cordova.plugin.location.resources.Resources;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Action to register and event and a listener in order to identify the location provider.
 *
 * @author Anthony Nahas
 * @version 1.5
 * @since 23.06.17
 */
@Singleton
public class RegisterProviderListenerAction extends Action {

    private Context mContext;
    private ChangeProviderReceiver mChangeProviderReceiver;

    @Inject
    public RegisterProviderListenerAction
        (@ApplicationContext Context mContext,
         ChangeProviderReceiver mChangeProviderReceiver
        ) {
        this.mContext = mContext;
        this.mChangeProviderReceiver = mChangeProviderReceiver;
    }

    @Override
    public void execute() {
        mChangeProviderReceiver.setCallbackContext(getCallbackContext());
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mChangeProviderReceiver,
            new IntentFilter(Resources.BROADCAST_ACTION_ON_CHANGED_PROVIDER));
    }
}
