package com.dff.cordova.plugin.location.utilities.helpers;

import android.location.Location;
import android.util.Log;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Map;

/**
 * Class that deals with multimaps and json
 * --> converting multimap string -  col "location" to mutlimap string - col "JSONObject"
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 03.05.2017
 */
public class MultimapHelper {

    private static final String TAG = MultimapHelper.class.getSimpleName();

    // TODO: 03.05.2017 : add the file in plugin.xml

    public enum properties {
        longitude,
        latitude,
        altitude,
        accuracy,
        speed,
        bearing,
        time
    }

    public static Map<String, Collection<JSONObject>> convertLocationsToJsonMultimap(Multimap<String, Location> locationMultimap) {
        Multimap<String, JSONObject> jsonObjectMultimap = ArrayListMultimap.create();
        for (String key : locationMultimap.keySet()) {
            Collection<Location> locationsCollection = locationMultimap.get(key);
            for (Location location : locationsCollection) {
                //convert to JSON and add it
                jsonObjectMultimap.put(key, convertLocationToJSONObject(location));
            }
        }
        Log.d(TAG, "jsonObjectMultimap's size = " + jsonObjectMultimap.size());
        Log.d(TAG, jsonObjectMultimap.toString());

        return jsonObjectMultimap.asMap();
    }

    private static JSONObject convertLocationToJSONObject(Location location) {
        assert location != null;
        JSONObject jsonLocation = new JSONObject();
        try {
            jsonLocation.put(properties.longitude.name(), location.getLongitude());
            jsonLocation.put(properties.latitude.name(), location.getLatitude());
            jsonLocation.put(properties.altitude.name(), location.getAltitude());
            jsonLocation.put(properties.accuracy.name(), location.getAccuracy());
            jsonLocation.put(properties.speed.name(), location.getSpeed());
            jsonLocation.put(properties.bearing.name(), location.getBearing());
            jsonLocation.put(properties.time.name(), location.getTime());
            Log.d(TAG, jsonLocation.toString());
        } catch (JSONException e) {
            Log.e(TAG, "Error: ", e);
            return null;
        }
        return jsonLocation;
    }

}
