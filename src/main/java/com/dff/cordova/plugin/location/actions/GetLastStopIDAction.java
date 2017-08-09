package com.dff.cordova.plugin.location.actions;

import com.dff.cordova.plugin.common.action.Action;
import com.dff.cordova.plugin.location.resources.Resources;

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
public class GetLastStopIDAction extends Action {

    @Inject
    public GetLastStopIDAction() {
    }

    @Override
    public void execute() {
        callbackContext.success(Resources.STOP_ID);
    }
}
