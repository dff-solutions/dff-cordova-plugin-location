package com.dff.cordova.plugin.location.utilities.helpers;

import android.location.Location;
import android.util.Log;

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
            return new JSONObject(gson.toJson(location));
        } catch (JSONException e) {
            Log.e(TAG, "Error: ", e);
        }
        return new JSONObject();
    }
}
