package com.dff.cordova.plugin.location.utilities;

import android.content.Context;
import android.util.Log;

/**
 * Class that deals with app crashes.
 *
 * @author Anthony Nahas
 * @version 1.2
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
            //LocationResources.testAddLocation();
            FileHelper.storePendingLocation(mContext);
            Log.d(TAG, "store pending locations");
            //mContext.startService(new Intent(mContext, PendingLocationsIntentService.class).setAction(LocationResources.ACTION_INTENT_STORE_PENDING_LOCATIONS));
            mPreferencesHelper.setLocationCanBeCleared(false);
            Log.d(TAG, "set can location be cleared --> false");
        } catch (Exception e) {
            Log.e(TAG, "error: ", e);
        } finally {
            mDefaultUncaughtExceptionHandler.uncaughtException(thread, throwable);
            Log.d(TAG, "def uncaught excp");
        }
    }
}
