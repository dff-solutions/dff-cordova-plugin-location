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
 * Created by anahas on 07.07.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 07.07.17
 */
@Singleton
public class Res {

    public static final String TAG = Res.class.getSimpleName();

    private LocationHelper mLocationHelper;

    private Location mLastGoodLocation;
    private List<JSONObject> mLocationList;

    @Inject
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
    public synchronized boolean addLocationTolist(JSONObject jsonLocation) {
        if (!mLocationList.contains(jsonLocation)) {
            mLocationList.add(jsonLocation);
            return true;
        }
        Log.d(TAG, "location already exists");
        return false;
    }

    /**
     * Clear the last good locations list.
     */
    public synchronized void clearDffStringLocationsList() {
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

    public synchronized JSONObject getLocationJSON() {
        mLastGoodLocation.setSpeed(mLocationHelper.toKmh(mLastGoodLocation));
        return mLocationHelper.toJson(mLastGoodLocation);
    }

    /**
     * Update the last good location object.
     *
     * @param mLastGoodLocation - The location object to be updated.
     */
    public synchronized void setLocation(Location mLastGoodLocation) {
        this.mLastGoodLocation = mLastGoodLocation;
    }

    public synchronized void clearLocation() {
        this.mLastGoodLocation = null;
    }
}
