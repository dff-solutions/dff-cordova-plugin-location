package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.core.config.LocationConfigHelper;

import org.json.JSONObject;

import javax.inject.Inject;

/**
 * Action to get current configuration.
 */
public class GetConfig extends LocationAction {
    public static final String ACTION = "getConfig";

    private LocationConfigHelper locationConfigHelper;

    @Inject
    public GetConfig(
        LocationConfigHelper locationConfigHelper
    ) {
        this.locationConfigHelper = locationConfigHelper;
    }

    @Override
    protected void execute() throws Exception {
        JSONObject configJson = locationConfigHelper.getLocationConfigJson();
        
        if (configJson != null) {
            callbackContext.success(configJson);
        } else {
            callbackContext.success((String) null);
        }
    }

    @Override
    public String getActionName() {
        return ACTION;
    }
}
