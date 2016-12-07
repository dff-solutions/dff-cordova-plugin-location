package com.dff.cordova.plugin.location.utilities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.dff.cordova.plugin.location.resources.LocationResources;
import com.dff.cordova.plugin.location.services.PendingLocationsIntentService;

/**
 * Class that deals with app crashes.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 06.12.2016
 */
public class CrashHelper implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHelper";
    private PreferencesHelper mPreferencesHelper;
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;


    /**
     * Custom constructor.
     *
     * @param context                  - The Context of the application/service.
     * @param uncaughtExceptionHandler - The default uncaughtExceptionHandler.
     */
    public CrashHelper(Context context, Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        mPreferencesHelper = new PreferencesHelper(context);
        mContext = context;
        mDefaultUncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    /**
     * On crash: set a key/value in shared preferences.
     *
     * @param thread    - The terminated thread.
     * @param throwable - The occured uncaught exception
     */
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Log.e(TAG, "uncaughtException");
        try {
            for (int i = 0; i <= 5; i++) {
                LocationResources.addLocationToList("test " + i);
            }
            mContext.startService(new Intent(mContext, PendingLocationsIntentService.class).setAction(LocationResources.ACTION_INTENT_STORE_PENDING_LOCATIONS));
            mPreferencesHelper.setCanLocationCanBeCleared(false);
        } catch (Exception e) {
            Log.e(TAG, "error: ", e);
        } finally {
            mDefaultUncaughtExceptionHandler.uncaughtException(thread, throwable);
        }
    }
}
