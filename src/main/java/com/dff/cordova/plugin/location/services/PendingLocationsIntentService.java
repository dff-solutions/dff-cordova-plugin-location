package com.dff.cordova.plugin.location.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.dff.cordova.plugin.location.configurations.JSActions;
import com.dff.cordova.plugin.location.dagger.DaggerManager;
import com.dff.cordova.plugin.location.utilities.helpers.FileHelper;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;

import javax.inject.Inject;

/**
 * Intent service that works async to perform expansive operations like writing and reading of files.
 *
 * @author Anthony Nahas
 * @version 1.2
 * @since 05.12.2016
 */
public class PendingLocationsIntentService extends IntentService {

    @Inject
    FileHelper mFileHelper;

    @Inject
    PreferencesHelper mPreferencesHelper;

    @Inject
    JSActions mJsActions;

    private static final String TAG = "PendingLocationsIntentService";

    public PendingLocationsIntentService() {
        super("PendingLocationsIntentService");
    }

    @Override
    public void onCreate() {
        DaggerManager
            .getInstance()
            .in(getApplication())
            .inject(this);
        super.onCreate();
        Log.d(TAG, "onCreate()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    /**
     * Handle the received intent by action.
     *
     * @param intent The intent sent to be performed.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent()");
        String action = intent.getAction();

        if (action != null) {
            Log.d(TAG, "Action = " + intent.getAction());
            if (action.equals(mJsActions.store_pending_locations)) {
                mFileHelper.storePendingLocation();
                mFileHelper.storeLocationsMultimap();
            }
            if (action.equals(mJsActions.restore_pending_locations)) {
                mFileHelper.restorePendingLocation();
                mFileHelper.restoreLocationsMultimap();
                mPreferencesHelper.restoreProperties();
                mPreferencesHelper.setIsServiceStarted(false);
            }
        }
    }
}
