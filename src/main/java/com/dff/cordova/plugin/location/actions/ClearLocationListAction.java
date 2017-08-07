package com.dff.cordova.plugin.location.actions;

import com.dff.cordova.plugin.location.dagger.annotations.Shared;
import com.dff.cordova.plugin.location.resources.Res;

import javax.inject.Inject;

/**
 * Action that deals with the location list in order to clear it.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 04.08.2017
 */
public class ClearLocationListAction extends Action {

    private static final String TAG = GetLocationListAction.class.getSimpleName();

    private Res mRes;

    @Inject
    public ClearLocationListAction(@Shared Res mRes) {
        this.mRes = mRes;
    }

    @Override
    public void execute() {
        mRes.clearList();
        callbackContext.success();
    }
}
