package com.dff.cordova.plugin.location.actions;

import android.content.Context;
import android.content.Intent;
import com.dff.cordova.plugin.dagger2.abstracts.Action;
import com.dff.cordova.plugin.dagger2.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.configurations.JSActions;
import com.dff.cordova.plugin.location.services.PendingLocationsIntentService;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Start the location intent service in order to store all properties
 * as well as the locations in the right form and place (shared preference, files..)
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 28.06.17
 */
@Singleton
public class StoreAction extends Action {

    private Context mContext;
    private JSActions mJsActions;

    @Inject
    public StoreAction
        (@ApplicationContext Context mContext,
         JSActions mJsActions
        ) {
        this.mContext = mContext;
        this.mJsActions = mJsActions;
    }

    @Override
    public void execute() {
        mContext.startService(new Intent(mContext, PendingLocationsIntentService.class)
            .setAction(mJsActions.restore_pending_locations));
    }
}
