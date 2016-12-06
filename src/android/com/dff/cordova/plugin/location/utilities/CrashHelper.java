package com.dff.cordova.plugin.location.utilities;

import android.content.SharedPreferences;
import android.util.Log;
import com.dff.cordova.plugin.location.resources.LocationResources;

/**
 * Created by anahas on 06.12.2016.
 */
public class CrashHelper implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHelper";
    private SharedPreferences mSharedPreferences;
    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;


    public CrashHelper(SharedPreferences sharedPreferences, Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        mSharedPreferences = sharedPreferences;
        mDefaultUncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {

    }

    private void store() {
        //SharedPreferences sharedPreferences = c.get(LocationResources.SHARED_PREFERENCE_NAME, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(LocationResources.SP_KEY_CLEAR_LOCATIONS, true);
        editor.putInt("counter", 12);
        Boolean res = editor.commit();
        Log.d(TAG, "commit is: " + res);
    }
}
