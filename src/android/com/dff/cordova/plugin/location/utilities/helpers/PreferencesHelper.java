package com.dff.cordova.plugin.location.utilities.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.dff.cordova.plugin.location.resources.LocationResources;

/**
 * Class to save and restore shared preferences.
 *
 * @author Anthony Nahas
 * @version 4.0
 * @since 06.12.2016
 */
public class PreferencesHelper {

    private static final String TAG = "PreferenceHelper";
    private Context mContext;
    private SharedPreferences mSharedPreferences;

    public PreferencesHelper(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(LocationResources.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Store a key/value in the shared preferences in order to make a state always available.
     *
     * @param state the state to be stored true/false
     * @return whether the state has been successfully stored
     */
    public boolean setLocationCanBeCleared(Boolean state) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(LocationResources.SP_KEY_CLEAR_LOCATIONS, state);
        //editor.putInt("counter", 12);
        Boolean res = editor.commit();
        Log.d(TAG, "Success of shared preference's commit (CAN BE CLEARED) is: " + res);
        return res;
    }

    public boolean setReturnType(String type) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(LocationResources.SP_KEY_RETURN_TYPE, type);
        return editor.commit();
    }

    public String getReturnType() {
        return mSharedPreferences.getString(LocationResources.SP_KEY_RETURN_TYPE, LocationResources.LOCATION_RETURN_TYPE);
    }

    public boolean setMinTime(long minTime) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        return editor.commit();
    }

    public boolean setMinDistance(float minDistance) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        return editor.commit();
    }

    public boolean setMinAccuracy(int minAccuracy) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        return editor.commit();
    }

    public boolean locationMaxAge(int maxAge) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        return editor.commit();
    }

    public boolean locationRequestDelay(int requestDelay) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        return editor.commit();
    }

    public boolean setIsServiceStarted(boolean isStarted) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(LocationResources.SP_KEY_IS_SERVICE_STARTED, isStarted);
        return editor.commit();
    }

    public boolean isServiceStarted() {
        return mSharedPreferences.getBoolean(LocationResources.SP_KEY_IS_SERVICE_STARTED, false);
    }

    public boolean storeTotalDistance(float distance) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(LocationResources.SP_KEY_TOTAL_DISTANCE, distance);
        Boolean res = editor.commit();
        Log.d(TAG, "Success of shared preference's commit (TOTAL DISTANCE) is: " + res);
        return res;
    }

    public boolean storeCustomDistance(float distance) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(LocationResources.SP_KEY_CUSTOM_DISTANCE, distance);
        Boolean res = editor.commit();
        Log.d(TAG, "Success of shared preference's commit (CUSTOM DISTANCE) is: " + res);
        return res;
    }

    /**
     * Restore the state of "can Location be cleared"
     *
     * @return The state
     */
    public boolean getCanLocationBeCleared() {
        return mSharedPreferences.getBoolean(LocationResources.SP_KEY_CLEAR_LOCATIONS, false);
    }

    public float getStoredTotalDistance() {
        return mSharedPreferences.getFloat(LocationResources.SP_KEY_TOTAL_DISTANCE, 0);
    }

    public float getStoreCustomDistance() {
        return mSharedPreferences.getFloat(LocationResources.SP_KEY_CUSTOM_DISTANCE, 0);
    }
}
