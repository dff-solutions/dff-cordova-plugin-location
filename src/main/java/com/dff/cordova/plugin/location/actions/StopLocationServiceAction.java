package com.dff.cordova.plugin.location.actions;

import android.content.Context;
import android.os.Message;
import android.os.Messenger;

import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.handlers.LocationRequestHandler;
import com.dff.cordova.plugin.location.resources.Resources;
import com.dff.cordova.plugin.location.utilities.helpers.MessengerHelper;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;

import org.apache.cordova.CallbackContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Stop location service
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 19.06.17
 */
@Singleton
public class StopLocationServiceAction extends Action {

    private Context mContext;
    private MessengerHelper mMessengerHelper;
    private PreferencesHelper mPreferencesHelper;
    private LocationRequestHandler mLocationRequestHandler;

    @Inject
    public StopLocationServiceAction(
        @ApplicationContext Context mContext,
        MessengerHelper mMessengerHelper,
        PreferencesHelper mPreferencesHelper,
        LocationRequestHandler mLocationRequestHandler
    ) {
        this.mContext = mContext;
        this.mMessengerHelper = mMessengerHelper;
        this.mPreferencesHelper = mPreferencesHelper;
        this.mLocationRequestHandler = mLocationRequestHandler;
    }

    @Override
    public Action with(CallbackContext callbackContext) {
        mLocationRequestHandler.setCallbackContext(callbackContext);
        return this;
    }


    @Override
    public void execute() {
        Message msg = Message.obtain(null, Resources.WHAT.STOP_LOCATION_SERVICE.ordinal());
        msg.replyTo = new Messenger(mLocationRequestHandler);
        mMessengerHelper.send(msg, callbackContext);
        mPreferencesHelper.setIsServiceStarted(false);
    }
}
