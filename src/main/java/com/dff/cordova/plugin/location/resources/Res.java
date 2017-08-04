package com.dff.cordova.plugin.location.resources;

import android.location.Location;
import android.util.Log;

import com.dff.cordova.plugin.location.utilities.helpers.LocationHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Resources class to deal with the allocated location object and the location list
 *
 * @author Anthony Nahas
 * @version 2.0
 * @since 07.07.17
 */
public class Res {

    public static final String TAG = Res.class.getSimpleName();

    private LocationHelper mLocationHelper;

    private Location mLastGoodLocation;
    private List<JSONObject> mLocationList;

    public Res(LocationHelper mLocationHelper) {
        this.mLocationHelper = mLocationHelper;
        mLocationList = new ArrayList<>();
    }

    /**
     * add synchronized a new location to the location list and return whether the add operation
     * was successful!
     *
     * @param jsonLocation - a location object as stringified json location
     * @return - whether a location has been added
     */
    public synchronized boolean addLocation(JSONObject jsonLocation) {
        if (!getLocationList().contains(jsonLocation)) {
            getLocationList().add(jsonLocation);
            return true;
        }
        Log.d(TAG, "location already exists");
        return false;
    }

    /**
     * Clear the last good locations list.
     */
    public synchronized void clearList() {
        try {
            mLocationList.clear();
        } catch (ConcurrentModificationException e) {
            Log.e(TAG, "Error while clearing the location list: ", e);
        }
    }

    /**
     * Get the location list - forward synchronized!
     *
     * @return - the target location list
     */
    public synchronized List<JSONObject> getLocationList() {
        return mLocationList;
    }

    /**
     * Return the last good location object.
     *
     * @return - The last good location object.
     */
    public synchronized Location getLocation() {
        return mLastGoodLocation;
    }

    /**
     * Get location as JSON object
     *
     * @return - The Location in JSON.
     */
    public synchronized JSONObject getLocationJSON() {
        Location location = mLastGoodLocation;
        if (location != null) {
            location.setSpeed((float) mLocationHelper.toKmh(location));
            return mLocationHelper.toJson(location);
        }
        return null;
    }

    /**
     * Update the last good location object.
     *
     * @param mLastGoodLocation - The location object to be updated.
     */
    public synchronized void setLocation(Location mLastGoodLocation) {
        this.mLastGoodLocation = mLastGoodLocation;
        addLocation(mLocationHelper.toJson(mLastGoodLocation));
        Log.d(TAG, "size of the location list --> " + getLocationList().size());
    }

    public synchronized void clearLocation() {
        mLastGoodLocation = null;
    }
}
