package com.dff.cordova.plugin.location.resources;

import android.location.Location;
import android.util.Log;
import com.dff.cordova.plugin.location.classes.DistanceCalculator;
import com.dff.cordova.plugin.location.utilities.helpers.TimeHelper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class to hold and handle properties related to the Location Plugin.
 *
 * @author Anthony Nahas
 * @version 6.0.0
 * @see 'https://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Multimap.html' (for multi hash map class)
 * @since 30.11.2016
 */
public class LocationResources {

    /*+++++++++++++++++++++++++++++++++++INIT+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    private static final String TAG = "LocationResources";
    private static Location LAST_GOOD_LOCATION = null;
    private static ArrayList<String> LOCATION_LIST_DFF_STRING = new ArrayList<>();
    private static ArrayList<JSONObject> LOCATION_LIST_JSON = new ArrayList<>();

    private static ListMultimap<String, Location> LOCATION_MULTIMAP = ArrayListMultimap.create();
    public static final DistanceCalculator TOTAL_DISTANCE_CALCULATOR = new DistanceCalculator();
    public static final DistanceCalculator CUSTOM_DISTANCE_CALCULATOR = new DistanceCalculator();
    public static final DistanceCalculator STOP_DISTANCE_CALCULATOR = new DistanceCalculator();

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
    public static final String ACTION_GET_TOTAL_DISTANCE_CALCULATOR = "distance.action.GET_TOTAL_DISTANCE_CALCULATOR";
    public static final String ACTION_RUN_CUSTOM_DISTANCE_CALCULATOR = "distance.action.RUN_CUSTOM_DISTANCE_CALCULATOR";
    public static final String ACTION_GET_CUSTOM_DISTANCE_CALCULATOR = "distance.action.GET_CUSTOM_DISTANCE_CALCULATOR";
    public static final String ACTION_GET_KEY_SET_FROM_LOCATIONS_MULTI_MAP = "location.action.GET_KEY_SET_FROM_LOCATIONS_MULTI_MAP";
    public static final String ACTION_SET_LOCATION_LISTENER = "location.action.SET_LOCATION_LISTENER";
    public static final String ACTION_SET_MIN_TIME = "location.action.SET_MIN_TIME";
    public static final String ACTION_CANCEL_STOP_LISTENER = "location.action.CANCEL_STOP_LISTENER";
    public static final String ACTION_ENABLE_MAPPING_LOCATIONS = "location.action.ACTION_ENABLE_MAPPING_LOCATIONS";
    public static final String ACTION_SET_STOP_ID = "hash_map.action.SET_STOP_ID";
    public static final String ACTION_GET_LAST_STOP_ID = "hash_map.action.GET_LAST_STOP_ID";
    public static final String ACTION_CLEAR_STOP_ID = "hash_map.action.CLEAR_STOP_ID";


    //Broadcasts Actions
    public static final String BROADCAST_ACTION_ON_STAND_STILL = "com.dff.cordova.plugin.location.broadcast.StandStillReceiver";
    public static final String BROADCAST_ACTION_STOP = "com.dff.cordova.plugin.location.broadcast.StandStillReceiver#stop";
    public static final String ACTION_SET_STOP_LISTENER = "location.action.SET_STOP_LISTENER";
    public static final String BROADCAST_ACTION_ON_NEW_LOCATION = "com.dff.cordova.plugin.location.broadcast.NewLocationReceiver";


    //Bundle keys
    public static final String LOCATION_RETURN_TYPE_KEY = "location.return.TYPE_KEY";
    public static final String IS_LOCATION_MANAGER_LISTENING = "is.location.manager.listening";
    public static final String LOCATION_MIN_TIME_KEY = "location.min.time.key";
    public static final String LOCATION_MIN_DISTANCE_KEY = "location.min.distance.key";

    //JSON keys (params)
    public static final String RETURN_TYPE = "returnType";
    public static final String MIN_TIME = "minTime";
    public static final String MIN_DISTANCE = "minDistance";
    public static final String MIN_ACCURACY = "minAccuracy";
    public static final String MAX_AGE = "locationMaxAge";
    public static final String DELAY = "locationRequestDelay";
    public static final String CLEAR = "clear";
    public static final String CLEAN = "clear";

    public static String UNKNOWN = "UNKNOWN";

    public static final String DFF_STRING = "dffString";
    public static final String JSON = "json";

    public static final String JSON_KEY_DISTANCE = "value";

    public static final String JSON_KEY_STOP_ID = "stopID";


    //Settings with default values
    public static int LOCATION_MIN_ACCURACY = 20; // in meters | 20 in production
    public static int LOCATION_MAX_AGE = 30; //in seconds
    public static long LOCATION_MIN_TIME = 0; //in msec
    public static float LOCATION_MIN_DISTANCE = 0; //in m
    public static int LOCATION_DELAY = 50000; // in ms 50sec
    public static String LOCATION_RETURN_TYPE = JSON;
    public static int LOCATION_RETURN_TYPE_INT = 1; // 1 = json, 0 = string

    //Settings for the stop holder
    public static int STOP_HOLDER_COUNTER_LIMIT = 10;
    public static int STOP_HOLDER_MIN_DISTANCE = 50;
    public static int STOP_HOLDER_DELAY = 60000; //1min

    //Location HashMap
    public static boolean IS_TO_CALCULATE_DISTANCE = false;
    public static String STOP_ID = "UNKNOWN";

    //Shared Preferences
    public static final String SP_KEY_CLEAR_LOCATIONS = "clearLocationKey";
    public static final String SP_KEY_TOTAL_DISTANCE = "totalDistanceKey";
    public static final String SP_KEY_CUSTOM_DISTANCE = "customDistanceKey";
    public static final String SP_KEY_RETURN_TYPE = "returnTypeKEy";
    public static final String SP_KEY_MIN_TIME = "minTimeKey";
    public static final String SP_KEY_MIN_DISTANCE = "minDistanceKey";
    public static final String SP_KEY_MIN_ACCURACY = "minAccuracy";
    public static final String SP_KEY_LOCATION_MAX_AGE = "locationMaxAgeKey";
    public static final String SP_KEY_LOCATION_REQUEST_DELAY = "locationRequestDelayKey";
    public static final String SP_KEY_IS_SERVICE_STARTED = "isServiceStartedKey";
    public static final String SP_KEY_IS_LOCATIONS_MAPPING_ENABLED = "isLocationsMappingEnabledKey";
    public static final String SP_KEY_STOPID = "stopIDKey";
    public static String LOCATION_FILE_NAME = "pendinglocations.sav";
    public static String LOCATIONS_MULTIMAP_FILE_NAME = "locationsmultimap.sav";
    public static String SHARED_PREFERENCE_NAME = "preferences";

    //Settings related to the Distance calculator class
    public static int DISTANCE_CALCULATOR_FULL_DELAY = 30000; //in ms 10sec for testing
    public static int DISTANCE_CALCULATOR_CUSTOM_DELAY = 30000; //in ms - 30sec
    public static int DISTANCE_CALCULATOR_STOP_DELAY = 60000; //1min


    /**
     * Enumeration to deal while parsing the actions.
     */
    public enum WHAT {
        START_LOCATION_SERVICE,
        STOP_LOCATION_SERVICE,
        GET_LOCATION,
        RUN_TOTAL_DISTANCE_CALCULATOR,
        GET_TOTAL_DISTANCE_CALCULATOR,
        RUN_CUSTOM_DISTANCE_CALCULATOR,
        GET_CUSTOM_DISTANCE_CALCULATOR,
        SET_LOCATION_LISTENER
    }

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
    public synchronized static Location getLastGoodLocation() {
        return LAST_GOOD_LOCATION;
    }

    public synchronized static ArrayList<String> getLocationListDffString() {
        return LOCATION_LIST_DFF_STRING;
    }

    public synchronized static ArrayList<JSONObject> getLocationListJson() {
        return LOCATION_LIST_JSON;
    }

    /**
     * Return the last good location that has been persisted as string representation.
     *
     * @return - The last good location in string representation.
     */
    public synchronized static String getLastGoodLocationAsString() {
        Location location = getLastGoodLocation();
        assert location != null;
        return location.getLongitude() + "|" +
            location.getLatitude() + "|" +
            getSpeedOfLastGoodLocation() + "|" +
            location.getBearing();
    }

    /**
     * Get location as JSON object
     *
     * @return - The Location in JSON.
     */
    public synchronized static JSONObject getLastGoodLocationAsJson() {
        Location location = getLastGoodLocation();
        assert location != null;
        JSONObject jsonLocation = new JSONObject();
        try {
            jsonLocation.put("longitude", location.getLongitude());
            jsonLocation.put("latitude", location.getLatitude());
            jsonLocation.put("altitude", location.getAltitude());
            jsonLocation.put("accuracy", location.getAccuracy());
            jsonLocation.put("speed", getSpeedOfLastGoodLocation());
            jsonLocation.put("bearing", location.getBearing());
            jsonLocation.put("time", location.getTime());
        } catch (JSONException e) {
            Log.e(TAG, "Error: ", e);
            return null;
        }
        return jsonLocation;
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
        Location location = getLastGoodLocation();
        assert location != null;
        if (location.hasSpeed() && location.getSpeed() > 0) {
            return Math.round(location.getSpeed() * 3.6);
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
     * Add a new location object to the last good location list for persistence purposes as dff string.
     *
     * @param location - The new location to store.
     */
    public static synchronized void addLocationToListAsDffString(String location) {
        if (!LOCATION_LIST_DFF_STRING.contains(location)) {
            LOCATION_LIST_DFF_STRING.add(location);
            return;
        }
        Log.d(TAG, "location already exists");
    }

    /**
     * Clear the last good locations list.
     */
    public static synchronized void clearDffStringLocationsList() {
        try {
            LOCATION_LIST_DFF_STRING.clear();
        } catch (ConcurrentModificationException e) {
            Log.e(TAG, "Error while clearing the location list: ", e);
        }
    }

    /**
     * Add a new location object to the last good location list for persistence purposes as json.
     *
     * @param location - The new location to store.
     */
    public static synchronized void addLocationToListAsJson(JSONObject location) {
        if (!LOCATION_LIST_JSON.contains(location)) {
            LOCATION_LIST_JSON.add(location);
            return;
        }
        Log.d(TAG, "location already exists");
    }

    /**
     * Clear the last good locations list.
     */
    public static synchronized void clearJsonLocationsList() {
        try {
            LOCATION_LIST_JSON.clear();
        } catch (ConcurrentModificationException e) {
            Log.e(TAG, "Error while clearing the location list: ", e);
        }
    }

    public static void setLocationsMultiMap(ListMultimap<String, Location> multiMap) {
        LOCATION_MULTIMAP = multiMap;
    }

    public static synchronized void addLocationToMultimap(Location location) {
        LOCATION_MULTIMAP.put(STOP_ID, location);
    }

    public static synchronized void clearLocationsMultimap() {
        try {
            LOCATION_MULTIMAP.clear();
        } catch (ConcurrentModificationException e) {
            Log.e(TAG, "Error while clearing the location hash map: ", e);
        }
    }

    public static synchronized Multimap<String, Location> getLocationsMultimap() {
        try {
            return LOCATION_MULTIMAP;
        } catch (ConcurrentModificationException e) {
            Log.e(TAG, "Error while clearing the location hash map: ", e);
        }
        return null;
    }

    public static synchronized void logLocationsMultimap() {
        assert LOCATION_MULTIMAP != null;
        for (String stopID : LOCATION_MULTIMAP.keySet()) {
            List<Location> locations = LOCATION_MULTIMAP.get(stopID);
            Log.d(TAG, "stopID: " + stopID + " --> " + locations);
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