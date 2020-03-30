package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.core.helpers.LocationDiagnosticsHelper;

import org.json.JSONException;

import javax.inject.Inject;

/**
 * Action to read diagnostics.
 */
public class ReadDiagnostics extends LocationAction {
    public static final String ACTION = "readDiagnostics";
    private LocationDiagnosticsHelper locationDiagnosticsHelper;

    @Inject
    public ReadDiagnostics(LocationDiagnosticsHelper locationDiagnosticsHelper) {
        this.locationDiagnosticsHelper = locationDiagnosticsHelper;
    }

    @Override
    protected void execute() throws JSONException {
        callbackContext.success(locationDiagnosticsHelper.getDiagnosticsJson());
    }

    @Override
    public String getActionName() {
        return ACTION;
    }
}
