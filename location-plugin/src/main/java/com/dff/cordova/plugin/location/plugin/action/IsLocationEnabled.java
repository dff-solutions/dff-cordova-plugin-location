package com.dff.cordova.plugin.location.plugin.action;

import javax.inject.Inject;

/**
 * Action to get current enabled/disabled state of location mode.
 * Action requires SDK 28 or higher.
 */
public class IsLocationEnabled extends LocationAction {
    public static final String ACTION = "isLocationEnabled";
    
    @Inject
    public IsLocationEnabled(
   
    ) {
    
    }
    
    @Override
    protected void execute() {
    
    }
    
    @Override
    public String getActionName() {
        return ACTION;
    }
}
