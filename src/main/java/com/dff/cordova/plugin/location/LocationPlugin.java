package com.dff.cordova.plugin.location;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.util.Log;

import com.dff.cordova.plugin.common.CommonPlugin;
import com.dff.cordova.plugin.common.service.CommonServicePlugin;
import com.dff.cordova.plugin.common.service.ServiceHandler;
import com.dff.cordova.plugin.location.classes.Executor;
import com.dff.cordova.plugin.location.configurations.ActionsManager;
import com.dff.cordova.plugin.location.dagger.DaggerManager;
import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.dagger.annotations.LocationRequestHandlerThread;
import com.dff.cordova.plugin.location.services.LocationService;
import com.dff.cordova.plugin.location.utilities.helpers.FileHelper;
import com.dff.cordova.plugin.location.utilities.helpers.MessengerHelper;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;

import javax.inject.Inject;

/**
 * Cordova Plugin class that deals with the android's Location API, in order to get the location of the device as
 * well as to persist the locations when the app is not reachable.
 *
 * @author Anthony Nahas
 * @version pre-9.0.0
 * @since 28.11.2016
 */
public class LocationPlugin extends CommonServicePlugin {

    private static final String TAG = "LocationPlugin";
    private static final String[] LOCATION_PERMISSIONS =
        {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        };

    @Inject
    @ApplicationContext
    Context mContext;

    @Inject
    @LocationRequestHandlerThread
    HandlerThread mHandlerThread;

    @Inject
    ServiceHandler mServiceHandler;

    @Inject
    Executor mExecutor;

    @Inject
    ActionsManager mActionsManager;

    @Inject
    FileHelper mFileHelper;

    @Inject
    MessengerHelper mMessengerHelper;

    @Inject
    PreferencesHelper mPreferencesHelper;

    private boolean isActionValid = false;

    private CordovaInterface mCordovaInterface;

    /**
     * Def-Constructor
     */
    public LocationPlugin() {
        super(TAG);
    }

    /**
     * Initialization of the plugin and the private properties.
     * and respectively bind and start the location service.
     */
    @Override
    public void pluginInitialize() {

        DaggerManager
            .getInstance()
            .in(cordova.getActivity().getApplication())
            .and(cordova)
            .inject(this);

        mCordovaInterface = cordova;

        requestLocationPermission();
        mContext.stopService(new Intent(mContext, LocationService.class));

        super.pluginInitialize(mServiceHandler);
        init();
    }


    /**
     * Executes an action called by JavaScript
     *
     * @param action          The action to execute.
     * @param args            The exec() arguments.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return true value if the action found, otherwise false.
     * @throws JSONException
     */
    @Override
    public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {

        isActionValid = false;

        if (action != null) {
            mCordovaInterface.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "Action = " + action);
                    try {
                        if (mActionsManager.allJSAction().contains(action)) {
                            isActionValid = true;
                            mExecutor.execute
                                (
                                    mActionsManager
                                        .hash(action)
                                        .with(callbackContext)
                                        .andHasArguments(args)
                                );
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error: --> ", e);
                    }
                }
            });
        }
        return isActionValid;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFileHelper.storePendingLocation();
        mFileHelper.storeLocationsMultimap();
    }

    /**
     * request permissions if they are not granted by forwarding them to
     * the common plugin
     */
    private void requestLocationPermission() {
        for (String permission : LOCATION_PERMISSIONS) {
            CommonPlugin.addPermission(permission);
        }
    }

    private void init() {
//        mExecutor.execute(mIndex.mRestoreAction);
    }


    public CordovaInterface getCordovaInterface() {
        return mCordovaInterface;
    }

    public void setCordovaInterface(CordovaInterface mCordovaInterface) {
        this.mCordovaInterface = mCordovaInterface;
    }
}
