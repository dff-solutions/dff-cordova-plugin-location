package com.dff.cordova.plugin.location.utilities.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.dff.cordova.plugin.location.resources.LocationResources;

import javax.inject.Inject;

import dagger.Module;

/**
 * Class to save and restore shared preferences.
 *
 * @author Anthony Nahas
 * @version 5.0
 * @since 06.12.2016
 */
@Module
public class PreferencesHelper {

    private static final String TAG = "PreferenceHelper";
    private Context mContext;

    @Inject
    public SharedPreferences sharedPreferences;

    public PreferencesHelper(Context context) {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(LocationResources.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Store a key/value in the shared preferences in order to make a state always available.
     *
     * @param state the state to be stored true/false
     * @return whether the state has been successfully stored
     */
    public boolean setLocationCanBeCleared(Boolean state) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LocationResources.SP_KEY_CLEAR_LOCATIONS, state);
        //editor.putInt("counter", 12);
        Boolean res = editor.commit();
        Log.d(TAG, "Success of shared preference's commit (CAN BE CLEARED) is: " + res);
        return res;
    }

    public boolean setReturnType(String type) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LocationResources.SP_KEY_RETURN_TYPE, type);
        return editor.commit();
    }

    public boolean setMinTime(long minTime) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(LocationResources.SP_KEY_MIN_TIME, minTime);
        return editor.commit();
    }

    public boolean setMinDistance(float minDistance) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(LocationResources.SP_KEY_MIN_DISTANCE, minDistance);
        return editor.commit();
    }

    public boolean setMinAccuracy(int minAccuracy) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(LocationResources.SP_KEY_MIN_ACCURACY, minAccuracy);
        return editor.commit();
    }

    public boolean setLocationMaxAge(int maxAge) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(LocationResources.SP_KEY_LOCATION_MAX_AGE, maxAge);
        return editor.commit();
    }

    public boolean setLocationRequestDelay(int requestDelay) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(LocationResources.SP_KEY_LOCATION_REQUEST_DELAY, requestDelay);
        return editor.commit();
    }

    public boolean setStopdID(String stopdID) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LocationResources.SP_KEY_STOPID, stopdID);
        return editor.commit();
    }

    public boolean setIsServiceStarted(boolean isStarted) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LocationResources.SP_KEY_IS_SERVICE_STARTED, isStarted);
        return editor.commit();
    }

    public boolean isServiceStarted() {
        return sharedPreferences.getBoolean(LocationResources.SP_KEY_IS_SERVICE_STARTED, false);
    }

    public boolean setIsLocationsMappingEnabled(boolean isEnable) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LocationResources.SP_KEY_IS_LOCATIONS_MAPPING_ENABLED, isEnable);
        return editor.commit();
    }

    public boolean isLocationsMappingEnabled() {
        return sharedPreferences.getBoolean(LocationResources.SP_KEY_IS_LOCATIONS_MAPPING_ENABLED, false);
    }

    public boolean storeTotalDistance(float distance) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(LocationResources.SP_KEY_TOTAL_DISTANCE, distance);
        Boolean res = editor.commit();
        Log.d(TAG, "Success of shared preference's commit (TOTAL DISTANCE) is: " + res);
        return res;
    }

    public boolean storeCustomDistance(float distance) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
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
        return sharedPreferences.getBoolean(LocationResources.SP_KEY_CLEAR_LOCATIONS, false);
    }

    public String getReturnType() {
        return sharedPreferences.getString(LocationResources.SP_KEY_RETURN_TYPE, LocationResources.LOCATION_RETURN_TYPE);
    }

    public long getMinTime() {
        return sharedPreferences.getLong(LocationResources.SP_KEY_MIN_TIME, LocationResources.LOCATION_MIN_TIME);
    }

    public float getMinDistance() {
        return sharedPreferences.getFloat(LocationResources.SP_KEY_MIN_DISTANCE, LocationResources.LOCATION_MIN_DISTANCE);
    }

    public int getMinAccuracy() {
        return sharedPreferences.getInt(LocationResources.SP_KEY_MIN_ACCURACY, LocationResources.LOCATION_MIN_ACCURACY);
    }

    public int getLocationMaxAge() {
        return sharedPreferences.getInt(LocationResources.SP_KEY_LOCATION_MAX_AGE, LocationResources.LOCATION_MAX_AGE);
    }

    public int getLocationRequestDelay() {
        return sharedPreferences.getInt(LocationResources.SP_KEY_LOCATION_REQUEST_DELAY, LocationResources.LOCATION_DELAY);
    }

    public float getStoredTotalDistance() {
        return sharedPreferences.getFloat(LocationResources.SP_KEY_TOTAL_DISTANCE, 0);
    }

    public float getStoreCustomDistance() {
        return sharedPreferences.getFloat(LocationResources.SP_KEY_CUSTOM_DISTANCE, 0);
    }

    public String getStopID() {
        return sharedPreferences.getString(LocationResources.SP_KEY_STOPID, LocationResources.UNKNOWN);
    }

    /**
     * Store saved properties from the resources into shared preference
     */
    public void storeProperties() {
        setReturnType(LocationResources.LOCATION_RETURN_TYPE);
        setMinTime(LocationResources.LOCATION_MIN_TIME);
        setMinDistance(LocationResources.LOCATION_MIN_DISTANCE);
        setMinAccuracy(LocationResources.LOCATION_MIN_ACCURACY);
        setLocationMaxAge(LocationResources.LOCATION_MAX_AGE);
        setLocationRequestDelay(LocationResources.LOCATION_DELAY);
        setIsLocationsMappingEnabled(LocationResources.IS_TO_CALCULATE_DISTANCE);
        setStopdID(LocationResources.STOP_ID);
    }

    /**
     * Restore saved properties from shared preference to the resources
     */
    public void restoreProperties() {
        LocationResources.LOCATION_RETURN_TYPE = getReturnType();
        LocationResources.LOCATION_MIN_TIME = getMinTime();
        LocationResources.LOCATION_MIN_DISTANCE = getMinDistance();
        LocationResources.LOCATION_MIN_ACCURACY = getMinAccuracy();
        LocationResources.LOCATION_MAX_AGE = getLocationMaxAge();
        LocationResources.LOCATION_DELAY = getLocationRequestDelay();
        LocationResources.IS_TO_CALCULATE_DISTANCE = isLocationsMappingEnabled();
        LocationResources.STOP_ID = getStopID();
    }
}
