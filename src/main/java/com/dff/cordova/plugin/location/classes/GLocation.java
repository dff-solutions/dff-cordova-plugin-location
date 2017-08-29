package com.dff.cordova.plugin.location.classes;

import android.location.Location;
import android.util.Log;
import com.dff.cordova.plugin.location.interfaces.IGLocation;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * GLocation class maps the location object from the sdl
 *
 * @author Anthony Nahas
 * @version 2.0
 * @since 15.08.2017
 */
public class GLocation implements IGLocation {

    public static final String TAG = "GLocation";

    public static final String LNG = "longitude";
    public static final String LAT = "latitude";
    public static final String ALT = "altitude";
    public static final String ACC = "accuracy";
    public static final String SPD = "speed";
    public static final String BEARING = "bearing";
    public static final String TIME = "time";

    private double longitude;
    private double latitude;
    private double altitude;
    private float accuracy;
    private float speed;
    private float bearing;
    private long time;

    public GLocation() {
    }

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
    @Override
    public synchronized JSONObject toJson() {
        Gson gson = new Gson();
        try {
            return new JSONObject(gson.toJson(this));
        } catch (JSONException e) {
            Log.e(TAG, "Error: ", e);
        }
        return new JSONObject();
    }

    @Override
    public synchronized GLocation fromJson(String json) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(json, GLocation.class);
        } catch (Exception e) {
            Log.e(TAG, "Error: ", e);
        }
        return null;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public double getAltitude() {
        return altitude;
    }

    @Override
    public float getAccuracy() {
        return accuracy;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public float getBearing() {
        return bearing;
    }

    @Override
    public long getTime() {
        return time;
    }


    @Override
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public void setLatitude(double latitude) {
        this.longitude = latitude;
    }

    @Override
    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    @Override
    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    @Override
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    @Override
    public void setTime(long time) {
        this.time = time;
    }
}
