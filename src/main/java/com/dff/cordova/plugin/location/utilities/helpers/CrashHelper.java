package com.dff.cordova.plugin.location.utilities.helpers;

import android.util.Log;
import com.dff.cordova.plugin.common.log.CordovaPluginLog;
import com.dff.cordova.plugin.dagger2.annotations.DefaultUncaughException;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class that deals with app crashes.
 *
 * @author Anthony Nahas
 * @version 3.0
 * @since 06.12.2016
 */
@Singleton
public class CrashHelper implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHelper";
    private FileHelper mFileHelper;
    private PreferencesHelper mPreferencesHelper;
    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;


    @Inject
    public CrashHelper
        (PreferencesHelper mPreferencesHelper,
         FileHelper mFileHelper,
         @DefaultUncaughException Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler) {

        this.mPreferencesHelper = mPreferencesHelper;
        this.mFileHelper = mFileHelper;
        this.mDefaultUncaughtExceptionHandler = mDefaultUncaughtExceptionHandler;
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
            mPreferencesHelper.storeProperties();
            mFileHelper.storePendingLocation();
            mFileHelper.storeLocationsMultimap();
            Log.d(TAG, "store pending locations and locations'multimap");
            Log.d(TAG, "set can location be cleared --> false");
        } catch (Exception e) {
            CordovaPluginLog.e(TAG, "Error: ", e);
        } finally {
            mDefaultUncaughtExceptionHandler.uncaughtException(thread, throwable);
            Log.d(TAG, "def uncaught excp");
        }
    }
}
