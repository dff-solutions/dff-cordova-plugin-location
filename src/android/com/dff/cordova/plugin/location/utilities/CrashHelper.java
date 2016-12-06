package com.dff.cordova.plugin.location.utilities;

import android.content.Context;
import android.util.Log;

/**
 * Created by anahas on 06.12.2016.
 *
 * @author Anthony Nahas
 * @version 0.9
 * @since 06.12.2016
 */
public class CrashHelper implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHelper";
    private PreferencesHelper mPreferencesHelper;
    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;


    public CrashHelper(Context context, Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        mPreferencesHelper = new PreferencesHelper(context);
        mDefaultUncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Log.e(TAG, "uncaughtException");
        try {
            mPreferencesHelper.setCanLocationCanBeCleared(false);
        } catch (Exception e) {
            Log.e(TAG, "error: ", e);
        } finally {
            mDefaultUncaughtExceptionHandler.uncaughtException(thread, throwable);
        }
    }
}
