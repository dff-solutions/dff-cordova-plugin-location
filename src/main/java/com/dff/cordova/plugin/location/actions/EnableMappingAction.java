package com.dff.cordova.plugin.location.actions;

import com.dff.cordova.plugin.location.resources.Res;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by anahas on 23.06.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 23.06.17
 */
@Singleton
public class EnableMappingAction extends Action {

    @Inject
    public EnableMappingAction() {
    }

    @Override
    public void execute() {
        Res.IS_TO_CALCULATE_DISTANCE = true;
        callbackContext.success();
    }
}