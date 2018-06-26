package com.dff.cordova.plugin.location.actions;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dff.cordova.plugin.dagger2.abstracts.Action;
import com.dff.cordova.plugin.dagger2.annotations.ApplicationContext;
import com.dff.cordova.plugin.dagger2.annotations.Shared;
import com.dff.cordova.plugin.location.classes.GLocationManager;
import com.dff.cordova.plugin.location.events.OnStartLocationService;
import com.dff.cordova.plugin.location.resources.Resources;
import com.dff.cordova.plugin.location.services.LocationService;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

/**
 * Created by anahas on 16.06.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 16.06.17
 */
public class StartLocationServiceAction extends Action {

    public static final String TAG = StartLocationServiceAction.class.getSimpleName();

    private Context mContext;
    private GLocationManager mGLocationManager;
    private EventBus mEventBus;
    private PreferencesHelper mPreferencesHelper;

    @Inject
    public StartLocationServiceAction(
            @ApplicationContext Context mContext,
            @Shared GLocationManager mGLocationManager,
            EventBus mEventBus,
            PreferencesHelper mPreferencesHelper
    ) {
        this.mContext = mContext;
        this.mGLocationManager = mGLocationManager;
        this.mEventBus = mEventBus;
        this.mPreferencesHelper = mPreferencesHelper;

        this.mEventBus.register(this);
    }

    @Override
    public void execute() {

        try {
            JSONObject params = getArgs().getJSONObject(0);
            if (params != null) {
                Resources.LOCATION_MIN_TIME = params.optLong(Resources.MIN_TIME, Resources.LOCATION_MIN_TIME);
                Resources.LOCATION_MIN_DISTANCE = (float) params.optDouble(Resources.MIN_DISTANCE, Resources.LOCATION_MIN_DISTANCE);
                Resources.LOCATION_MIN_ACCURACY = params.optInt(Resources.MIN_ACCURACY, Resources.LOCATION_MIN_ACCURACY);
                Resources.LOCATION_MAX_AGE = params.optInt(Resources.MAX_AGE, Resources.LOCATION_MAX_AGE);
                mPreferencesHelper.storeProperties();
                mContext.startService(new Intent(mContext, LocationService.class));
            }
        } catch (JSONException e) {
            Logger.t(TAG).e(TAG, "Error: " + e.getMessage());
            getCallbackContext().error("Error while trying to start the location service: " + e);

        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.mEventBus.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnStartLocationService event) {

        Log.i(TAG, "location service is running --> " + mGLocationManager.isListening());
        if (getCallbackContext() != null) {
            if (mGLocationManager.isListening()) {
                getCallbackContext().success();
            } else {
                getCallbackContext().error("Location Manager is not listening since the service could not be " +
                        "started or No provider has been found to request a new location");
            }
        }
    }
}
