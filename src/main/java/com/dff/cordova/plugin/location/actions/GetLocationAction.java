package com.dff.cordova.plugin.location.actions;

import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;

import com.dff.cordova.plugin.location.handlers.LocationRequestHandler;
import com.dff.cordova.plugin.location.resources.Resources;
import com.dff.cordova.plugin.location.utilities.helpers.MessengerHelper;

import org.apache.cordova.CallbackContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Get the last good location from the service if it's available.
 * Good location means in this context: accuracy < 20m..
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 20.06.17
 */
@Singleton
public class GetLocationAction extends Action {

    private MessengerHelper mMessengerHelper;
    private LocationRequestHandler mLocationRequestHandler;

    @Inject
    public GetLocationAction
        (
            MessengerHelper mMessengerHelper,
            LocationRequestHandler mLocationRequestHandler
        ) {

        this.mMessengerHelper = mMessengerHelper;
        this.mLocationRequestHandler = mLocationRequestHandler;
    }

    @Override
    public Action with(CallbackContext callbackContext) {
        mLocationRequestHandler.setCallbackContext(callbackContext);
        return this;
    }

    @Override
    public void execute() {
        Message msg = Message.obtain(null, Resources.WHAT.GET_LOCATION.ordinal());
        msg.replyTo = new Messenger(mLocationRequestHandler);
        Bundle params = new Bundle();
        params.putInt(Resources.LOCATION_RETURN_TYPE_KEY, args.optInt(0, Resources.LOCATION_RETURN_TYPE_INT));
        msg.setData(params);
        mMessengerHelper.send(msg, callbackContext);
    }
}
