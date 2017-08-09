package com.dff.cordova.plugin.location.actions;

import android.content.Context;
import android.content.Intent;

import com.dff.cordova.plugin.common.action.Action;
import com.dff.cordova.plugin.location.configurations.JSActions;
import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.services.PendingLocationsIntentService;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Restore stored value from the shared preference or respectively from file system.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 19.06.17
 */
@Singleton
public class RestoreAction extends Action {

    private Context mContext;
    private JSActions mJsActions;
    private PreferencesHelper mPreferencesHelper;

    @Inject
    public RestoreAction(
        @ApplicationContext Context mContext,
        JSActions mJsActions,
        PreferencesHelper mPreferencesHelper
    ) {
        this.mContext = mContext;
        this.mJsActions = mJsActions;
        this.mPreferencesHelper = mPreferencesHelper;
    }


    @Override
    public void execute() {
        mContext.startService(new Intent(mContext, PendingLocationsIntentService.class)
            .setAction(mJsActions.restore_pending_locations));
    }
}
