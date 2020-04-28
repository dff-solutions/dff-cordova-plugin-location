package com.dff.cordova.plugin.location.plugin.action;

import android.location.LocationManager;
import android.os.Build;

import com.dff.cordova.plugin.shared.helpers.AndroidSdkHelper;

import javax.inject.Inject;

/**
 * Action to get current enabled/disabled state of location mode.
 * Action requires SDK 28 or higher.
 */
public class IsLocationEnabled extends LocationAction {
    public static final String ACTION = "isLocationEnabled";
    
    private AndroidSdkHelper androidSdkHelper;
    private LocationManager locationManager;
    
    @Inject
    public IsLocationEnabled(
        AndroidSdkHelper androidSdkHelper,
        LocationManager locationManager
    ) {
        this.androidSdkHelper = androidSdkHelper;
        this.locationManager = locationManager;
    }
    
    @Override
    protected void execute() {
        int neededSDK = Build.VERSION_CODES.P;
        
        if (androidSdkHelper.isSdkGreaterOrEqual(neededSDK)) {
            boolean enabled = locationManager.isLocationEnabled();
            callbackContext.success(enabled ? 1 : 0);
        } else {
            callbackContext.error(String.format(
                "action requires SDK %s or higher. Current SDK: %s",
                neededSDK,
                androidSdkHelper.getSdkVersionCode()
            ));
        }
    }
    
    @Override
    public String getActionName() {
        return ACTION;
    }
}
