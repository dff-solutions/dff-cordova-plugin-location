package com.dff.cordova.plugin.location.classes;

import android.location.Location;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * GLocation class maps the location object from the sdl
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 15.08.2017
 */

public class GLocation {

    public static final String TAG = "GLocation";

    public static final String LNG = "longitude";
    public static final String LAT = "latitude";
    public static final String ALT = "altitude";
    public static final String ACC = "accuracy";
    public static final String SPD = "speed";
    public static final String BEARING = "bearing";
    public static final String TIME = "time";

    public double longitude;
    public double latitude;
    public double altitude;
    public float accuracy;
    public float speed;
    public float bearing;
    public long time;

    public GLocation(Location location) {
        if (location != null) {
            try {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                altitude = location.getAltitude();
                accuracy = location.getAccuracy();
                speed = location.getSpeed();
                bearing = location.getBearing();
                time = location.getTime();
            } catch (NullPointerException e) {
                Log.e(TAG, "Error: ", e);
            }
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return LNG
            + " : "
            + longitude
            + "\n"
            + LAT
            + " : "
            +
            latitude
            +
            "\n"
            + ACC
            + " : "
            + accuracy
            +
            "\n";
    }

    /**
     * Get location as JSON object
     *
     * @return - The Location in JSON.
     */
    public synchronized JSONObject toJson() {
        Gson gson = new Gson();
        try {
            return new JSONObject(gson.toJson(this));
        } catch (JSONException e) {
            Log.e(TAG, "Error: ", e);
        }
        return new JSONObject();
    }
}
