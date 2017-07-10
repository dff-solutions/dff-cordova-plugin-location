package com.dff.cordova.plugin.location.resources;

import org.json.JSONObject;

import java.util.ArrayList;
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

    private List<JSONObject> mLocationList;

    @Inject
    public Res() {
        mLocationList = new ArrayList<>();
    }

    /**
     * add synchronized a new location to the location list and return whether the add operation
     * was successful!
     * @param jsonLocation - a location object as stringified json location
     * @return - whether a location has been added
     */
    public synchronized boolean addLocationTolist(JSONObject jsonLocation) {
        if (!mLocationList.contains(jsonLocation)) {
            mLocationList.add(jsonLocation);
            return true;
        }
        return false;
    }
}
