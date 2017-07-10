package com.dff.cordova.plugin.location.handlers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.dagger.annotations.LocationRequestLooper;
import com.dff.cordova.plugin.location.resources.Resources;
import com.dff.cordova.plugin.location.services.LocationService;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;

import org.apache.cordova.CallbackContext;

import javax.inject.Inject;

/**
 * Class to handle the answer sent from the location service handler.
 * On result, forward to the user (JS) using a callback context.
 *
 * @author Anthony Nahas
 * @version 6.0.0
 * @since 30.11.2016
 */
public class LocationRequestHandler extends Handler {

    private static final String TAG = "LocationRequestHandler";
    private Context mContext;
    private PreferencesHelper mPreferencesHelper;
    private CallbackContext mCallbackContext;

    /**
     * Custom constructor
     *
     * @param looper   - The used looper.
     * @param mContext - The application/service context.
     */
    @Inject
    public LocationRequestHandler(
        @ApplicationContext Context mContext,
        @LocationRequestLooper Looper looper,
        PreferencesHelper mPreferencesHelper
    ) {
        super(looper);
        this.mContext = mContext;
        this.mPreferencesHelper = mPreferencesHelper;
    }

    /**
     * Handle the message sent by the location service handler.
     *
     * @param msg - The message received.
     */
    @Override
    public void handleMessage(Message msg) {
        Bundle data;
        Resources.WHAT msg_what = Resources.WHAT.values()[msg.what];

        switch (msg_what) {
            case START_LOCATION_SERVICE:
                data = msg.getData();
                if (data.getBoolean(Resources.IS_LOCATION_MANAGER_LISTENING)) {
                    mCallbackContext.success();
                } else {
                    mCallbackContext.error("No provider has been found to request a new location");
                }
                break;
            case STOP_LOCATION_SERVICE:
                mContext.stopService(new Intent(mContext, LocationService.class));
                break;
            case GET_LOCATION:
                Log.d(TAG, "what = " + msg.what);
                data = msg.getData();
                //String location = data.getString(Resources.DATA_LOCATION_KEY);
                int returnType = data.getInt(Resources.LOCATION_RETURN_TYPE_KEY);

                if (Resources.getLastGoodLocation() != null) {
                    switch (returnType) {
                        case 0:
                            mCallbackContext.success(Resources.getLastGoodLocationAsString());
                            break;
                        case 1:
                            mCallbackContext.success(Resources.getLastGoodLocationAsJson());
                            break;
                        default:
                            mCallbackContext.success(Resources.getLastGoodLocationAsJson());
                            break;
                    }
                } else {
                    mCallbackContext.success("");
                }
                if (mPreferencesHelper.getCanLocationBeCleared()) {
                    switch (Resources.LOCATION_RETURN_TYPE) {
                        case Resources.DFF_STRING:
                            Resources.clearDffStringLocationsList();
                            break;
                        case Resources.JSON:
                            Resources.clearJsonLocationsList();
                            break;
                    }
                }
                break;
            default:
                String errorMsg = "no 'what' property of the msg has been found!";
                //mCallbackContext.error(errorMsg);
                Log.w(TAG, errorMsg);
                break;
        }

        super.handleMessage(msg);
    }

    public void setCallbackContext(CallbackContext mCallbackContext) {
        this.mCallbackContext = mCallbackContext;
    }
}
