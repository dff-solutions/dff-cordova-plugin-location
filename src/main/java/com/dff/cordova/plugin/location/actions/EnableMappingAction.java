package com.dff.cordova.plugin.location.actions;

import com.dff.cordova.plugin.common.action.Action;
import com.dff.cordova.plugin.location.resources.Resources;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;

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

    private PreferencesHelper mPreferencesHelper;

    @Inject
    public EnableMappingAction(PreferencesHelper mPreferencesHelper) {
        this.mPreferencesHelper = mPreferencesHelper;
    }

    @Override
    public void execute() {

        boolean isToEnable = getArgs().optBoolean(0, true);
        Resources.IS_TO_CALCULATE_DISTANCE = isToEnable;
        mPreferencesHelper.setIsLocationsMappingEnabled(isToEnable);
        getCallbackContext().success();
    }
}