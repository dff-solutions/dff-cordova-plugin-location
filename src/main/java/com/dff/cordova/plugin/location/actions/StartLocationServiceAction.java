package com.dff.cordova.plugin.location.actions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.handlers.LocationRequestHandler;
import com.dff.cordova.plugin.location.resources.Res;
import com.dff.cordova.plugin.location.services.LocationService;
import com.dff.cordova.plugin.location.utilities.helpers.MessengerHelper;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by anahas on 16.06.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 16.06.17
 */
@Singleton
public class StartLocationServiceAction extends Action {

    public static final String TAG = StartLocationServiceAction.class.getSimpleName();

    private Context mContext;
    private MessengerHelper mMessengerHelper;
    private PreferencesHelper mPreferencesHelper;
    private LocationRequestHandler mLocationRequestHandler;

    private CallbackContext mCallbackContext;
    private JSONArray mArguments;

    //RequestHandler
    @Inject
    public StartLocationServiceAction(
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
        mCallbackContext = callbackContext;
        mLocationRequestHandler.setCallbackContext(mCallbackContext);
        return this;
    }

    @Override
    public Action andHasArguments(JSONArray args) {
        mArguments = args;
        return this;
    }

    @Override
    public void execute() {

        mContext.startService(new Intent(mContext, LocationService.class));
        Message msg = Message.obtain(null, Res.WHAT.START_LOCATION_SERVICE.ordinal());

        Bundle data = new Bundle();
        try {
            JSONObject params = mArguments.getJSONObject(0);
            if (params != null) {
                Res.LOCATION_RETURN_TYPE = params.optString(Res.RETURN_TYPE, Res.LOCATION_RETURN_TYPE);
                Res.LOCATION_MIN_TIME = params.optLong(Res.MIN_TIME, Res.LOCATION_MIN_TIME);
                Res.LOCATION_MIN_DISTANCE = (float) params.optDouble(Res.MIN_DISTANCE, Res.LOCATION_MIN_DISTANCE);
                Res.LOCATION_MIN_ACCURACY = params.optInt(Res.MIN_ACCURACY, Res.LOCATION_MIN_ACCURACY);
                Res.LOCATION_MAX_AGE = params.optInt(Res.MAX_AGE, Res.LOCATION_MAX_AGE);
                Res.LOCATION_DELAY = params.optInt(Res.DELAY, Res.LOCATION_DELAY);
                mPreferencesHelper.storeProperties();
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error: ", e);
        }
        data.putLong(Res.LOCATION_MIN_TIME_KEY, Res.LOCATION_MIN_TIME);
        data.putFloat(Res.LOCATION_MIN_DISTANCE_KEY, Res.LOCATION_MIN_DISTANCE);
        msg.setData(data);
        msg.replyTo = new Messenger(mLocationRequestHandler);
        mMessengerHelper.send(msg, mCallbackContext);
    }
}
