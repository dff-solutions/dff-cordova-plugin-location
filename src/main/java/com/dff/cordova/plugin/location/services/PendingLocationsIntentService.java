package com.dff.cordova.plugin.location.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.dff.cordova.plugin.location.resources.LocationResources;
import com.dff.cordova.plugin.location.utilities.helpers.FileHelper;

/**
 * Intent service that works async to perform expansive operations like writing and reading of files.
 *
 * @author Anthony Nahas
 * @version 1.2
 * @since 05.12.2016
 */
public class PendingLocationsIntentService extends IntentService {

    private static final String TAG = "PendingLocationsIntentService";

    public PendingLocationsIntentService() {
        super("PendingLocationsIntentService");
    }

    @Override
    public void onCreate() {
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
            if (action.equals(LocationResources.ACTION_INTENT_STORE_PENDING_LOCATIONS)) {
                FileHelper.storePendingLocation(this);
                FileHelper.storeLocationsMultimap(this);
            }
            if (action.equals(LocationResources.ACTION_INTENT_RESTORE_PENDING_LOCATIONS)) {
                FileHelper.restorePendingLocation(this);
                FileHelper.restoreLocationsMultimap(this);
            }
        }
    }
}
