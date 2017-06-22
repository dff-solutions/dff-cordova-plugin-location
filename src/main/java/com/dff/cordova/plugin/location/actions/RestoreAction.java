package com.dff.cordova.plugin.location.actions;

import android.content.Context;
import android.content.Intent;

import com.dff.cordova.plugin.location.abstracts.Action;
import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.resources.LocationResources;
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
    private PreferencesHelper mPreferencesHelper;

    @Inject
    public RestoreAction(
        @ApplicationContext Context mContext,
        PreferencesHelper mPreferencesHelper
    ) {
        this.mContext = mContext;
        this.mPreferencesHelper = mPreferencesHelper;
    }


    @Override
    public Action execute() {
        mPreferencesHelper.restoreProperties();
        mPreferencesHelper.setIsServiceStarted(false);

        mContext.startService(new Intent(mContext, PendingLocationsIntentService.class)
            .setAction(LocationResources.ACTION_INTENT_RESTORE_PENDING_LOCATIONS));

        return this;
    }
}
