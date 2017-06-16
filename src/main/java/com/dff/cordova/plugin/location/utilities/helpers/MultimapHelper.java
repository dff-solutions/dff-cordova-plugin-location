package com.dff.cordova.plugin.location.utilities.helpers;

import android.location.Location;
import android.util.Log;

import com.dff.cordova.plugin.common.log.CordovaPluginLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.Module;

/**
 * Class that deals with multimaps and json
 * --> converting multimap string -  col "location" to mutlimap string - col "JSONObject"
 *
 * @author Anthony Nahas
 * @version 1.2
 * @since 03.05.2017
 */
public class MultimapHelper {

    private static final String TAG = MultimapHelper.class.getSimpleName();

    public enum properties {
        longitude,
        latitude,
        altitude,
        accuracy,
        speed,
        bearing,
        time
    }

    @Inject
    public MultimapHelper() {
    }

    public Map<String, Collection<JSONObject>> convertLocationsToJsonMultimap(ListMultimap<String, Location> locationMultimap) {
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

    public JSONObject convertLocationToJSONObject(Location location) {
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

    public Map<String, Object> parseJSONtoMap(String jsonString) {
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

    public ListMultimap<String, Location> convertMapToLocationsMultiMap(Map<String, Object> map) {
        assert map != null;
        ListMultimap<String, Location> multimap = ArrayListMultimap.create();

        for (String key : map.keySet()) {

            ArrayList locationsList = (ArrayList) map.get(key);

            for (Object LinkedHashMap : locationsList) {
                // Instantiate a new Gson instance.
                Gson gson = new Gson();
                // Convert the ordered map into an ordered string.
                String json = gson.toJson(LinkedHashMap, LinkedHashMap.class);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(json);
                    Log.d(TAG, jsonObject.toString());
                    multimap.put(key, convertJSONToLocation(jsonObject));
                } catch (JSONException e) {
                    Log.e(TAG, "Error: ", e);
                }
            }

        }
        Log.d(TAG, "locations'multimap size = " + multimap.size());
        Log.d(TAG, multimap.toString());

        return multimap;
    }

    public Location convertJSONToLocation(JSONObject jsonLocation) {
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


    public ListMultimap<String, Location> convertJSONtoMultimap(String jsonString) {
        Log.d(TAG, "on convertJSONtoMultimap");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new GuavaModule());
        try {
            return objectMapper.readValue(
                objectMapper.treeAsTokens(objectMapper.readTree(jsonString)),
                objectMapper.getTypeFactory().constructMapLikeType(
                    Multimap.class, String.class, Location.class));
        } catch (IOException e) {
            CordovaPluginLog.e(TAG, "Error: ", e);
            return ArrayListMultimap.create();
        }

    }
}
