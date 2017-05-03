package com.dff.cordova.plugin.location.utilities.helpers;

import android.location.Location;
import android.util.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

    public static Map<String, Collection<JSONObject>> convertLocationsToJsonMultimap(ListMultimap<String, Location> locationMultimap) {
        ListMultimap<String, JSONObject> jsonObjectMultimap = ArrayListMultimap.create();
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

    public static Map<String, Object> parseJSONtoMap(String jsonString) {
        Map<String, Object> map = null;
        try {
            map = new ObjectMapper().readValue(jsonString, Map.class);
            Log.d(TAG, "on parsing json to map, map size = " + map.size());
            Log.d(TAG, map.toString());

        } catch (IOException e) {
            Log.e(TAG, "Error: ", e);
        }
        return map;
    }

    public static ListMultimap<String, Location> convertMapToLocationsMultiMap(Map<String, Object> map) {
        assert map != null;
        ListMultimap<String, Location> multimap = ArrayListMultimap.create();
        for (String key : map.keySet()) {
            //(Collection<JSONObject> collection = map.get(key);
            Log.d(TAG, map.get(key).toString());
            /*
            for (JSONObject jsonLocation : collection) {
                //convert json to location and add it to the multimap
                multimap.put(key, convertJSONToLocation(jsonLocation));
            }
            */
        }
        Log.d(TAG, "locations'multimap size = " + multimap.size());
        Log.d(TAG, multimap.toString());

        return multimap;
    }

    private static Location convertJSONToLocation(JSONObject jsonLocation) {
        Location location = new Location("GPS");
        try {
            location.setLongitude(jsonLocation.getDouble(properties.longitude.name()));
            location.setLatitude(jsonLocation.getDouble(properties.latitude.name()));
            location.setAltitude(jsonLocation.getDouble(properties.altitude.name()));
            location.setAccuracy((float) jsonLocation.getDouble(properties.accuracy.name()));
            location.setSpeed((float) jsonLocation.getDouble(properties.speed.name()));
            location.setBearing((float) jsonLocation.getDouble(properties.bearing.name()));
            location.setTime(jsonLocation.getLong(properties.time.name()));
            Log.d(TAG, location.toString());
        } catch (JSONException e) {
            Log.e(TAG, "Error: ", e);
        }
        return location;
    }
}
