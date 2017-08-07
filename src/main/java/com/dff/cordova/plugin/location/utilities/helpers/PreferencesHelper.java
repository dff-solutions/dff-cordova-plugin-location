package com.dff.cordova.plugin.location.utilities.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.dff.cordova.plugin.location.dagger.annotations.Private;
import com.dff.cordova.plugin.location.resources.Resources;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class to save and restore shared preferences.
 *
 * @author Anthony Nahas
 * @version 5.0
 * @since 06.12.2016
 */
@Singleton
public class PreferencesHelper {

    private static final String TAG = "PreferenceHelper";
    private Context mContext;

    private SharedPreferences mSharedPreferences;

    @Inject
    public PreferencesHelper(@Private SharedPreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
//        mContext = context;
//        mSharedPreferences = mContext.getSharedPreferences(Resources.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Store a key/value in the shared preferences in order to make a state always available.
     *
     * @param state the state to be stored true/false
     * @return whether the state has been successfully stored
     */
    public boolean setLocationCanBeCleared(Boolean state) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(Resources.SP_KEY_CLEAR_LOCATIONS, state);
        //editor.putInt("counter", 12);
        Boolean res = editor.commit();
        Log.d(TAG, "Success of shared preference's commit (CAN BE CLEARED) is: " + res);
        return res;
    }

    public boolean setReturnType(String type) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Resources.SP_KEY_RETURN_TYPE, type);
        return editor.commit();
    }

    public boolean setMinTime(long minTime) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(Resources.SP_KEY_MIN_TIME, minTime);
        return editor.commit();
    }

    public boolean setMinDistance(float minDistance) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(Resources.SP_KEY_MIN_DISTANCE, minDistance);
        return editor.commit();
    }

    public boolean setMinAccuracy(int minAccuracy) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(Resources.SP_KEY_MIN_ACCURACY, minAccuracy);
        return editor.commit();
    }

    public boolean setLocationMaxAge(int maxAge) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(Resources.SP_KEY_LOCATION_MAX_AGE, maxAge);
        return editor.commit();
    }

    public boolean setLocationRequestDelay(int requestDelay) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(Resources.SP_KEY_LOCATION_REQUEST_DELAY, requestDelay);
        return editor.commit();
    }

    public boolean setStopdID(String stopdID) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Resources.SP_KEY_STOP_ID, stopdID);
        return editor.commit();
    }

    public boolean setIsServiceStarted(boolean isStarted) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(Resources.SP_KEY_IS_SERVICE_STARTED, isStarted);
        return editor.commit();
    }

    public boolean isServiceStarted() {
        return mSharedPreferences.getBoolean(Resources.SP_KEY_IS_SERVICE_STARTED, false);
    }

    public boolean setIsLocationsMappingEnabled(boolean isEnable) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(Resources.SP_KEY_IS_LOCATIONS_MAPPING_ENABLED, isEnable);
        return editor.commit();
    }

    public boolean isLocationsMappingEnabled() {
        return mSharedPreferences.getBoolean(Resources.SP_KEY_IS_LOCATIONS_MAPPING_ENABLED, false);
    }

    public boolean storeTotalDistance(float distance) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(Resources.SP_KEY_TOTAL_DISTANCE, distance);
        Boolean res = editor.commit();
        Log.d(TAG, "Success of shared preference's commit (TOTAL DISTANCE) is: " + res);
        return res;
    }

    public boolean storeCustomDistance(float distance) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(Resources.SP_KEY_CUSTOM_DISTANCE, distance);
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
        return mSharedPreferences.getBoolean(Resources.SP_KEY_CLEAR_LOCATIONS, false);
    }

    public String getReturnType() {
        return mSharedPreferences.getString(Resources.SP_KEY_RETURN_TYPE, Resources.LOCATION_RETURN_TYPE);
    }

    public long getMinTime() {
        return mSharedPreferences.getLong(Resources.SP_KEY_MIN_TIME, Resources.LOCATION_MIN_TIME);
    }

    public float getMinDistance() {
        return mSharedPreferences.getFloat(Resources.SP_KEY_MIN_DISTANCE, Resources.LOCATION_MIN_DISTANCE);
    }

    public int getMinAccuracy() {
        return mSharedPreferences.getInt(Resources.SP_KEY_MIN_ACCURACY, Resources.LOCATION_MIN_ACCURACY);
    }

    public int getLocationMaxAge() {
        return mSharedPreferences.getInt(Resources.SP_KEY_LOCATION_MAX_AGE, Resources.LOCATION_MAX_AGE);
    }

    public int getLocationRequestDelay() {
        return mSharedPreferences.getInt(Resources.SP_KEY_LOCATION_REQUEST_DELAY, Resources.LOCATION_DELAY);
    }

    public float getStoredTotalDistance() {
        return mSharedPreferences.getFloat(Resources.SP_KEY_TOTAL_DISTANCE, 0);
    }

    public float getStoreCustomDistance() {
        return mSharedPreferences.getFloat(Resources.SP_KEY_CUSTOM_DISTANCE, 0);
    }

    public String getStopID() {
        return mSharedPreferences.getString(Resources.SP_KEY_STOP_ID, Resources.UNKNOWN);
    }

    /**
     * Store saved properties from the resources into shared preference
     */
    public void storeProperties() {
        setReturnType(Resources.LOCATION_RETURN_TYPE);
        setMinTime(Resources.LOCATION_MIN_TIME);
        setMinDistance(Resources.LOCATION_MIN_DISTANCE);
        setMinAccuracy(Resources.LOCATION_MIN_ACCURACY);
        setLocationMaxAge(Resources.LOCATION_MAX_AGE);
        setLocationRequestDelay(Resources.LOCATION_DELAY);
        setIsLocationsMappingEnabled(Resources.IS_TO_CALCULATE_DISTANCE);
        setStopdID(Resources.STOP_ID);
    }

    /**
     * Restore saved properties from shared preference to the resources
     */
    public void restoreProperties() {
        Resources.LOCATION_RETURN_TYPE = getReturnType();
        Resources.LOCATION_MIN_TIME = getMinTime();
        Resources.LOCATION_MIN_DISTANCE = getMinDistance();
        Resources.LOCATION_MIN_ACCURACY = getMinAccuracy();
        Resources.LOCATION_MAX_AGE = getLocationMaxAge();
        Resources.LOCATION_DELAY = getLocationRequestDelay();
        Resources.IS_TO_CALCULATE_DISTANCE = isLocationsMappingEnabled();
        Resources.STOP_ID = getStopID();
    }
}
