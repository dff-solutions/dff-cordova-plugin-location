package com.dff.cordova.plugin.location.resources;

import android.location.Location;
import android.util.Log;
import com.dff.cordova.plugin.location.utilities.TimeHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by anahas on 30.11.2016.
 *
 * @author Anthony Nahas
 * @version 1.3.1
 * @since 30.11.2016
 */
public class LocationResources {

    private static final String TAG = "LocationResources";
    private static Location LAST_GOOD_LOCATION = null;
    private static ArrayList<String> LAST_GOOD_LOCATION_LIST = new ArrayList<String>();

    //Actions
    public static final String ACTION_START_SERVICE = "location.action.START_SERVICE";
    public static final String ACTION_STOP_SERVICE = "location.action.STOP_SERVICE";
    public static final String ACTION_GET_LOCATION_STRING = "location.action.GET_LOCATION_STRING";
    public static final String ACTION_GET_LOCATION_JSON = "location.action.GET_LOCATION_JSON";
    public static final String ACTION_GET_LOCATION_LIST = "location.action.GET_LOCATION_LIST";
    public static final String ACTION_SET_MIN_ACCURACY = "location.action.SET_MIN_ACCURACY";
    public static final String ACTION_SET_MAX_AGE = "location.action.SET_MAX_AGE";
    public static final String ACTION_INTENT_STORE_PENDING_LOCATIONS = "location.action.intent.STORE_PENDING_LOCATIONS";
    public static final String ACTION_INTENT_RESTORE_PENDING_LOCATIONS = "location.action.intent.RESTORE_PENDING_LOCATIONS";


    //Settings with default values
    public static int LOCATION_MIN_ACCURACY = 10000; // in meters | 20 in production
    public static int LOCATION_MAX_AGE = 30; //in seconds
    public static int LOCATION_DELAY = 50000; // in mseconds 50sec
    public static final String SP_KEY_CLEAR_LOCATIONS = "clearLocationKey";
    public static String LOCATION_FILE_NAME = "pendinglocations.sav";
    public static String SHARED_PREFERENCE_NAME = "preferences";

    //What
    public static final int WHAT_GET_LOCATION = 1;

    public static int counter = 0;


    //Data Keys
    public static final String DATA_LOCATION_KEY = "data_location_key";

    public static void setLastGoodLocation(Location location) {
        LAST_GOOD_LOCATION = location;
    }

    public static Location getLastGoodLocation() {
        return LAST_GOOD_LOCATION;
    }

    public static ArrayList<String> getLastGoodLocationList() {
        return LAST_GOOD_LOCATION_LIST;
    }

    public static String getLastGoodLocationAsString() {
        return LAST_GOOD_LOCATION.getLongitude() + "|" +
                LAST_GOOD_LOCATION.getLatitude() + "|" +
                getSpeedOfLastGoodLocation() + "|" +
                LAST_GOOD_LOCATION.getBearing();
    }

    public static JSONObject getLastGoodAsJson() {
        JSONObject location = new JSONObject();
        try {
            location.put("Longitude", LAST_GOOD_LOCATION.getLongitude());
            location.put("Latitude", LAST_GOOD_LOCATION.getLatitude());
            location.put("Altitude", LAST_GOOD_LOCATION.getAltitude());
            location.put("Accuracy", LAST_GOOD_LOCATION.getAccuracy());
            location.put("Speed", getSpeedOfLastGoodLocation());
            location.put("Bearing", LAST_GOOD_LOCATION.getBearing());
            location.put("Time", LAST_GOOD_LOCATION.getTime());
        } catch (JSONException e) {
            Log.e(TAG, "Error: ", e);
        }
        return location;
    }

    public static String getTestLastGoodLocationToString() {
        printDifference();
        return LAST_GOOD_LOCATION.getLongitude() + "|" +
                LAST_GOOD_LOCATION.getLatitude() + "|" +
                getSpeedOfLastGoodLocation() + "|" +
                LAST_GOOD_LOCATION.getBearing() + "| ACC = " +
                LAST_GOOD_LOCATION.getAccuracy() + "| time age = " +
                TimeHelper.getTimeAge(LAST_GOOD_LOCATION.getTime()) + "| ep time = " +
                LAST_GOOD_LOCATION.getTime() + "| system curren time = " +
                System.currentTimeMillis();
    }

    private static double getSpeedOfLastGoodLocation() {
        if (LAST_GOOD_LOCATION.hasSpeed() && LAST_GOOD_LOCATION.getSpeed() > 0) {
            return Math.round(LAST_GOOD_LOCATION.getSpeed() * 3.6);
        }
        return 0;
    }

    public static void setLocationMaxAge(int maxAge) {
        LOCATION_MAX_AGE = maxAge;
    }

    public static void setLocationMinAccuracy(int minAccuracy) {
        LOCATION_MIN_ACCURACY = minAccuracy;
    }

    public static void addLocationToList(String location) {
        if (!LAST_GOOD_LOCATION_LIST.contains(location)) {
            Log.d(TAG, "location already exists");
        }
        LAST_GOOD_LOCATION_LIST.add(location);
    }

    public static void clearLocationsList() {
        LAST_GOOD_LOCATION_LIST.clear();
    }

    private static void printDifference() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy/MM/dd HH:mm:ss");
        try {
            Date d = new Date(LAST_GOOD_LOCATION.getTime());
            Log.d("Location", "ep =" + dateFormat.format(d));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        Date now = new Date();
        Log.d("Location", "now = " + dateFormat.format(now));
    }

    public static void testAddLocation() {
        for (int i = 0; i <= 5; i++) {
            LocationResources.addLocationToList("test " + i);
        }
    }

}