package com.dff.cordova.plugin.location;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.util.Log;
import com.dff.cordova.plugin.location.classes.Executor;
import com.dff.cordova.plugin.location.configurations.ActionsManager;
import com.dff.cordova.plugin.location.dagger.DaggerManager;
import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.dagger.annotations.LocationRequestHandlerThread;
import com.dff.cordova.plugin.location.events.OnRequestPermissionResult;
import com.dff.cordova.plugin.location.services.LocationService;
import com.dff.cordova.plugin.location.utilities.helpers.FileHelper;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import javax.inject.Inject;

/**
 * Cordova Plugin classes that deals with the android's Location API, in order to get the location of the device as
 * well as to persist the locations when the app is not reachable.
 *
 * @author Anthony Nahas
 * @version 9.1.0-beta.5
 * @since 28.11.2016
 */
public class LocationPlugin extends CordovaPlugin {

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
    EventBus mEventBus;

    @Inject
    Executor mExecutor;

    @Inject
    ActionsManager mActionsManager;

    @Inject
    FileHelper mFileHelper;

    @Inject
    PreferencesHelper mPreferencesHelper;

    private CordovaInterface mCordovaInterface;

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

        mContext.stopService(new Intent(mContext, LocationService.class));
        //@plugin version 7.2.3
        mPreferencesHelper.restoreProperties();
        mPreferencesHelper.setIsServiceStarted(false);
        super.pluginInitialize();
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

        if (action != null && mActionsManager.allJSAction().contains(action)) {
            final CordovaPlugin cordovaPlugin = this;
            mCordovaInterface.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "Action = " + action);
                    try {
                        mExecutor.execute
                            (cordovaPlugin,
                                mActionsManager
                                    .hash(action)
                                    .with(callbackContext)
                                    .andHasArguments(args)
                            );
                    } catch (Exception e) {
                        Log.e(TAG, "Error: --> ", e);
                    }
                }
            });
            Log.d(TAG, "action has been found and executed");
            return true;
        }
        Log.d(TAG, "404 - action not found");
        return false;
    }

    @Override
    public void onDestroy() {
        mFileHelper.storePendingLocation();
        mFileHelper.storeLocationsMultimap();
    }

    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        mEventBus.post(new OnRequestPermissionResult(requestCode, permissions, grantResults));
    }

    public CordovaInterface getCordovaInterface() {
        return mCordovaInterface;
    }

    public void setCordovaInterface(CordovaInterface mCordovaInterface) {
        this.mCordovaInterface = mCordovaInterface;
    }
}
