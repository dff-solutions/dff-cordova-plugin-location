package com.dff.cordova.plugin.location.utilities.helpers;

import android.location.Location;
import android.util.Log;

import com.dff.cordova.plugin.location.resources.Res;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

/**
 * Created by anahas on 07.07.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 07.07.17
 */

public class LocationHelper {

    private final String TAG = LocationHelper.class.getSimpleName();

    @Inject
    public LocationHelper() {
    }

    public JSONObject toJson(Location location) {
        Gson gson = new Gson();
        try {
            if (location != null) {
                return new JSONObject(gson.toJson(location));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error: ", e);
        }
        return new JSONObject();
    }

    /**
     * Get the speed of the last good saved location.
     * If it's available, the speed will be converted to KM/h
     *
     * @return - The speed of the last good location.
     */
    public double toKmh(Location location) {
        assert location != null;
        if (location.hasSpeed() && location.getSpeed() > 0) {
            return Math.round(location.getSpeed() * 3.6);
        }
        return 0;
    }
}
