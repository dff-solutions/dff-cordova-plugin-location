package com.dff.cordova.plugin.location.resources;

import android.location.Location;
import android.util.Log;
import com.dff.cordova.plugin.location.classes.DistanceCalculator;
import com.dff.cordova.plugin.location.utilities.helpers.TimeHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;

/**
 * Class to hold and handle properties related to the Location Plugin.
 *
 * @author Anthony Nahas
 * @version 4.0.1
 * @since 30.11.2016
 */
public class LocationResources {

    /*+++++++++++++++++++++++++++++++++++INIT+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    private static final String TAG = "LocationResources";
    private static Location LAST_GOOD_LOCATION = null;
    private static ArrayList<String> LAST_GOOD_LOCATION_LIST = new ArrayList<String>();
    public static final DistanceCalculator TOTAL_DISTANCE_CALCULATOR = new DistanceCalculator();
    public static final DistanceCalculator CUSTOM_DISTANCE_CALCULATOR = new DistanceCalculator();

    //Actions
    public static final String ACTION_START_SERVICE = "location.action.START_SERVICE";
    public static final String ACTION_STOP_SERVICE = "location.action.STOP_SERVICE";
    public static final String ACTION_GET_LOCATION = "location.action.GET_LOCATION";
    public static final String ACTION_GET_LOCATION_LIST = "location.action.GET_LOCATION_LIST";
    public static final String ACTION_SET_MIN_ACCURACY = "location.action.SET_MIN_ACCURACY";
    public static final String ACTION_SET_MAX_AGE = "location.action.SET_MAX_AGE";
    public static final String ACTION_INTENT_STORE_PENDING_LOCATIONS = "location.action.intent.STORE_PENDING_LOCATIONS";
    public static final String ACTION_INTENT_RESTORE_PENDING_LOCATIONS = "location.action.intent.RESTORE_PENDING_LOCATIONS";
    public static final String ACTION_RUN_TOTAL_DISTANCE_CALCULATOR = "distance.action.RUN_TOTAL_DISTANCE_CALCULATOR";
    public static final String ACTION_GET_TOTAL_DISTANCE = "distance.action.GET_TOTAL_DISTANCE";
    public static final String ACTION_RUN_CUSTOM_DISTANCE_CALCULATOR = "distance.action.RUN_CUSTOM_DISTANCE_CALCULATOR";
    public static final String ACTION_GET_CUSTOM_DISTANCE = "distance.action.GET_CUSTOM_DISTANCE";
    public static final String ACTION_SET_LOCATION_LISTENER = "location.action.SET_LOCATION_LISTENER";
    public static final String ACTION_SET_MIN_TIME = "location.action.SET_MIN_TIME";
    public static final String BROADCAST_ACTION_ON_NEW_LOCATION = "com.dff.cordova.plugin.location.broadcast.NewLocationReceiver";


    //JSON keys
    public static final String JSON_KEY_DISTANCE = "value";


    //Settings with default values
    public static int LOCATION_MIN_ACCURACY = 20; // in meters | 20 in production
    public static int LOCATION_MAX_AGE = 30; //in seconds
    public static long LOCATION_MIN_TIME = 0; //in msec
    public static int LOCATION_DELAY = 50000; // in ms 50sec
    public static int LOCATION_RETURN_TYPE = 1; // 1 = json, 0 = string
    public static String LOCATION_RETURN_TYPE_KEY = "location.return.TYPE_KEY";

    //Shared Preferences
    public static final String SP_KEY_CLEAR_LOCATIONS = "clearLocationKey";
    public static final String SP_KEY_TOTAL_DISTANCE = "totalDistanceKey";
    public static final String SP_KEY_CUSTOM_DISTANCE = "customDistanceKey";
    public static String LOCATION_FILE_NAME = "pendinglocations.sav";
    public static String SHARED_PREFERENCE_NAME = "preferences";

    //Settings related to the Distance calculator class
    public static int DISTANCE_CALCULATOR_FULL_DELAY = 10000; //in ms 10sec for testing
    public static int DISTANCE_CALCULATOR_CUSTOM_DELAY = 30000; //in ms


    //What
    public static final int WHAT_GET_LOCATION = 1;
    public static final int WHAT_RUN_TOTAL_DISTANCE_CALCULATOR = 2;
    public static final int WHAT_GET_TOTAL_DISTANCE_CALCULATOR = 3;
    public static final int WHAT_RUN_CUSTOM_DISTANCE_CALCULATOR = 4;
    public static final int WHAT_GET_CUSTOM_DISTANCE_CALCULATOR = 5;
    public static final int WHAT_SET_LOCATION_LISTENER = 6;


    /**
     * Update the last good location object.
     *
     * @param location - The location object to be updated.
     */
    public static void setLastGoodLocation(Location location) {
        LAST_GOOD_LOCATION = location;
    }

    /*+++++++++++++++++++++++++++++++++++GETTER+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /**
     * Return the last good location object.
     *
     * @return - The last good location object.
     */
    public static Location getLastGoodLocation() {
        return LAST_GOOD_LOCATION;
    }

    public static ArrayList<String> getLastGoodLocationList() {
        return LAST_GOOD_LOCATION_LIST;
    }

    /**
     * Return the last good location that has been persisted as string representation.
     *
     * @return - The last good location in string representation.
     */
    public static String getLastGoodLocationAsString() {
        return LAST_GOOD_LOCATION.getLongitude() + "|" +
            LAST_GOOD_LOCATION.getLatitude() + "|" +
            getSpeedOfLastGoodLocation() + "|" +
            LAST_GOOD_LOCATION.getBearing();
    }

    /**
     * Get location as JSON object
     *
     * @return - The Location in JSON.
     */
    public static JSONObject getLastGoodLocationAsJson() {
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

    /**
     * Return the location in string representation.
     * Note: this function is used for test purposes.
     *
     * @return - The location in string representation.
     */
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

    /**
     * Get the speed of the last good saved location.
     * If it's available, the speed will be converted to KM/h
     *
     * @return - The speed of the last good location.
     */
    private static double getSpeedOfLastGoodLocation() {
        if (LAST_GOOD_LOCATION.hasSpeed() && LAST_GOOD_LOCATION.getSpeed() > 0) {
            return Math.round(LAST_GOOD_LOCATION.getSpeed() * 3.6);
        }
        return 0;
    }


    /*++++++++++++++++++++++++++SETTER++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /**
     * Update the value of the location'S max age as configuration.
     *
     * @param maxAge - The maximum age that a location can have.
     */
    public static void setLocationMaxAge(int maxAge) {
        LOCATION_MAX_AGE = maxAge;
    }

    /**
     * Update the value of the location'S minimum accuracy as configuration.
     *
     * @param minAccuracy - The minimum accuracy that a location should have to be persisted.
     */
    public static void setLocationMinAccuracy(int minAccuracy) {
        LOCATION_MIN_ACCURACY = minAccuracy;
    }


    /**
     * Update the minimum time of requesting a new location as configuration.
     *
     * @param minTime - The minimum time needed to request a new location.
     */
    public static void setLocationMinTime(long minTime) {
        LOCATION_MIN_TIME = minTime;
    }


    /*++++++++++++++++++++++++++OTHERS++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /**
     * Parse a what property of an action in order to give it a integer representation and use it across the Plugin.
     *
     * @param action - The action to be executed.
     * @return - The integer representation of the requested action.
     */
    public static int parseWHAT(String action) {
        //we dnt use switch statement since < java 7 does not support string switches --> int
        if (action.equals(ACTION_GET_LOCATION)) {
            return 1;
        } else if (action.equals(ACTION_RUN_TOTAL_DISTANCE_CALCULATOR)) {
            return 2;
        } else if (action.equals(ACTION_GET_TOTAL_DISTANCE)) {
            return 3;
        } else if (action.equals(ACTION_RUN_CUSTOM_DISTANCE_CALCULATOR)) {
            return 4;
        } else if (action.equals(ACTION_GET_CUSTOM_DISTANCE)) {
            return 5;
        } else if (action.equals(ACTION_SET_LOCATION_LISTENER)) {
            return 6;
        } else {
            return 0; //should be never reached
        }
    }

    /**
     * Add a new location object to the last good location list for persistence purposes.
     *
     * @param location - The new location to store.
     */
    public static synchronized void addLocationToList(String location) {
        if (!LAST_GOOD_LOCATION_LIST.contains(location)) {
            Log.d(TAG, "location already exists");
        }
        LAST_GOOD_LOCATION_LIST.add(location);
    }

    /**
     * Clear the last good locations list.
     */
    public static synchronized void clearLocationsList() {
        try {
            LAST_GOOD_LOCATION_LIST.clear();
        } catch (ConcurrentModificationException e) {
            Log.e(TAG, "Error while clearing the location list: ", e);
        }
    }

    /**
     * Print out the difference time between the current time of the operating system
     * and the one the last good location.
     * <p>
     * NB: Used for test/debug purposes.
     */
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

}