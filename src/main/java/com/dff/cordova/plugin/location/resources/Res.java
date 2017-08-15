package com.dff.cordova.plugin.location.resources;

import android.location.Location;
import android.util.Log;

import com.dff.cordova.plugin.common.log.CordovaPluginLog;
import com.dff.cordova.plugin.location.classes.GLocation;
import com.dff.cordova.plugin.location.utilities.helpers.LocationHelper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.List;


import static com.dff.cordova.plugin.location.resources.Resources.STOP_ID;

/**
 * Resources classes to deal with the allocated location object and the location list
 *
 * @author Anthony Nahas
 * @version 9.0.0-rc3
 * @since 07.07.17
 */
public class Res {

    public static final String TAG = Res.class.getSimpleName();

    private LocationHelper mLocationHelper;

    private GLocation mLocation;
    private List<JSONObject> mLocationList;
    private ListMultimap<String, Location> mLocationMultimap;

    public Res(LocationHelper mLocationHelper) {
        this.mLocationHelper = mLocationHelper;
        mLocationList = new ArrayList<>();
        mLocationMultimap = ArrayListMultimap.create();
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
     * Get the locations multimap - synchronized
     *
     * @return - the target multimap
     */
    public synchronized ListMultimap<String, Location> getLocationListMultimap() {
        try {
            return mLocationMultimap;
        } catch (ConcurrentModificationException e) {
            CordovaPluginLog.e(TAG, "Error: @ConcurrentModificationException - while getting the location hash map: ", e);
        }
        return null;
    }

    /**
     * Return the last good location object.
     *
     * @return - The last good location object.
     */
    public synchronized GLocation getLocation() {
        return mLocation;
    }


    /**
     * Update the last good location object.
     *
     * @param mLocation - The location object to be updated.
     */
    public synchronized void setLocation(Location mLocation) {
        mLocation.setSpeed((float) mLocationHelper.toKmh(mLocation));
        this.mLocation = new GLocation(mLocation);

        addLocation(this.mLocation.toJson());
        Log.d(TAG, "size of the location list --> " + getLocationList().size());

        if (Resources.IS_TO_CALCULATE_DISTANCE) {
            Log.d(TAG, "Location is to calculate - mapping in " + Resources.STOP_ID);
            mapLocation(mLocation);
//                        mRes.logLocationListMultimap();
        }
    }

    /**
     * setter multimap for restore purpoeses
     *
     * @param mLocationMultimap - the locations multimap to restore
     */
    public synchronized void setLocationMultimap(ListMultimap<String, Location> mLocationMultimap) {
        this.mLocationMultimap = mLocationMultimap;
        Log.d(TAG, "on restore locations'multimap - size --> " + this.mLocationMultimap.size());
        Log.d(TAG, "on restore locations'multimap - keySet --> " + this.mLocationMultimap.keySet());
    }

    public synchronized void clearLocation() {
        mLocation = null;
    }

    public synchronized void mapLocation(Location location) {
        boolean mapped = mLocationMultimap.put(STOP_ID, location);
        Log.d(TAG, "mapping success --> " + mapped + " with " + Resources.STOP_ID);
        Log.d(TAG, "size of the location multimap list --> " + getLocationListMultimap().size());
        Log.d(TAG, "keyset and keyset size of the location multimap list --> "
            + getLocationListMultimap().keySet()
            + " size --> "
            + getLocationListMultimap().keySet().size());
    }

    /**
     * clear the location list's multimap
     */
    public synchronized void clearLocationListMultimap() {
        try {
            mLocationMultimap.clear(); // // TODO: 07.08.2017 instead clear --> new multimap
        } catch (ConcurrentModificationException e) {
            CordovaPluginLog.e(TAG, "Error: @ConcurrentModificationException - while clearing the location hash map: ", e);
        }
    }

    /**
     * Log the location list's multimap
     */
    public synchronized void logLocationListMultimap() {
        if (mLocationMultimap != null) {
            for (String stopID : mLocationMultimap.keySet()) {
                Collection<Location> locations = mLocationMultimap.get(stopID);
                Log.d(TAG, "stopID: " + stopID + " --> " + locations);
            }
        }
    }
}
