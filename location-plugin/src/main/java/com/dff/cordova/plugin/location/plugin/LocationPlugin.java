package com.dff.cordova.plugin.location.plugin;

import com.dff.cordova.plugin.location.plugin.action.LocationAction;
import com.dff.cordova.plugin.location.plugin.dagger.LocationPluginDaggerManager;
import com.dff.cordova.plugin.location.plugin.listeners.callback.OnLocationStatusChangedCallbackHandler;
import com.dff.cordova.plugin.location.plugin.listeners.callback.OnLocationUpdateCallbackHandler;
import com.dff.cordova.plugin.location.plugin.listeners.callback.OnProviderEnabledCallbackHandler;
import com.dff.cordova.plugin.location.plugin.listeners.callback.OnProximityAlertCallbackHandler;
import com.dff.cordova.plugin.shared.configurations.ActionsManager;
import com.dff.cordova.plugin.shared.dagger.annotations.PluginPermissions;
import com.dff.cordova.plugin.shared.helpers.PermissionHelper;
import com.dff.cordova.plugin.shared.log.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;

import java.util.Arrays;

import javax.inject.Inject;

/**
 * Defines plugin for cordova.
 */
public class LocationPlugin extends CordovaPlugin {
    private static final String TAG = "LocationPlugin";

    static final int PERMISSION_REQUEST_CODE = 0;
    private boolean permissionsRequested = false;
    
    @Inject
    Log log;
    
    @Inject
    OnLocationStatusChangedCallbackHandler onLocationStatusChangedCallbackHandler;
    
    @Inject
    OnLocationUpdateCallbackHandler onLocationUpdateCallbackHandler;
    
    @Inject
    OnProviderEnabledCallbackHandler onProviderEnabledCallbackHandler;
    
    @Inject
    OnProximityAlertCallbackHandler onProximityAlertCallbackHandler;
    
    @Inject
    ActionsManager<LocationAction> actionsManager;

    @Inject
    PermissionHelper permissionHelper;

    @Inject
    @PluginPermissions
    String[] pluginPermissions;
    
    /**
     * Called after plugin construction and fields have been initialized.
     */
    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();

        LocationPluginDaggerManager
            .getInstance()
            .register(this);

        LocationPluginDaggerManager
            .getInstance()
            .context(cordova.getActivity().getApplicationContext())
            .inject(this);

        log.d(TAG, "pluginInitialize");

        onLocationStatusChangedCallbackHandler.onInitialize();
        onLocationUpdateCallbackHandler.onInitialize();
        onProviderEnabledCallbackHandler.onInitialize();
        onProximityAlertCallbackHandler.onInitialize();
    }

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
        requestPermissions();
    }
    
    /**
     * The final call you receive before your activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        log.d(TAG, "onDestroy");
        actionsManager.onDestroy();
        onLocationStatusChangedCallbackHandler.onDestroy();
        onLocationUpdateCallbackHandler.onDestroy();
        onProviderEnabledCallbackHandler.onDestroy();
        onProximityAlertCallbackHandler.onDestroy();

        LocationPluginDaggerManager
            .getInstance()
            .unregister(this);
    }
    
    /**
     * Executes the request.
     * <p>
     * This method is called from the WebView thread. To do a non-trivial amount of work, use:
     * cordova.getThreadPool().execute(runnable);
     * <p>
     * To run on the UI thread, use:
     * cordova.getActivity().runOnUiThread(runnable);
     *
     * @param action          The action to execute.
     * @param args            The exec() arguments.
     * @param callbackContext The mCallback context used when calling back into JavaScript.
     * @return Whether the action was valid.
     */
    @Override
    public boolean execute(
        String action,
        final JSONArray args,
        final CallbackContext callbackContext
    ) {
        try {
            // check permissions
            if (!requestPermissions()) {
                // try action anyway since not all actions require permission.
                // actions are queued and PERMISSIONS might be granted when action is running
                log.w(TAG, String.format(
                    "required permissions %s not granted",
                    Arrays.toString(pluginPermissions)
                ));
            }

            boolean execResult = actionsManager.executeAction(action, args, callbackContext);
            
            return execResult || super.execute(action, args, callbackContext);
        } catch (Exception e) {
            log.e(TAG, e.getMessage(), e);
            callbackContext.error(e.getMessage());
            
            return false;
        }
    }

    /**
     * Requests permissions if user has not selected the Don't ask again option
     * for all permissions.
     *
     * @return True if all permissions are granted false otherwise
     */
    private boolean requestPermissions() {
        boolean allGranted = permissionHelper.hasAllPermissions(pluginPermissions);
        boolean showPermissionRationale = permissionHelper
            .shouldShowRequestPermissionRationale(cordova.getActivity(), pluginPermissions);

        log.d(TAG, String.format("all permissions granted: %b", allGranted));
        log.d(TAG, String.format("permissions requested: %b", permissionsRequested));

        if (!allGranted && (!permissionsRequested || showPermissionRationale)) {
            log.d(TAG, String.format(
                "request permissions for %s",
                Arrays.toString(pluginPermissions))
            );
            cordova.requestPermissions(this, PERMISSION_REQUEST_CODE, pluginPermissions);
            permissionsRequested = true;
        }

        return allGranted;
    }
}
