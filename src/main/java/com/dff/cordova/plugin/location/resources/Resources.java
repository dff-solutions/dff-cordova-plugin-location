package com.dff.cordova.plugin.location.resources;

import android.location.Location;
import android.util.Log;

import com.dff.cordova.plugin.common.log.CordovaPluginLog;
import com.dff.cordova.plugin.location.classes.DistanceCalculator;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;

/**
 * Class to hold and handle properties related to the Location Plugin.
 *
 * @author Anthony Nahas
 * @version 7.2.3
 * @see 'https://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Multimap.html' (for multi hash map class)
 * @since 30.11.2016
 */
public class Resources {

    /*+++++++++++++++++++++++++++++++++++INIT+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    private static final String TAG = "Resources";
    private static Location LAST_GOOD_LOCATION = null;
    private static ArrayList<String> LOCATION_LIST_DFF_STRING = new ArrayList<>();
    private static ArrayList<JSONObject> LOCATION_LIST_JSON = new ArrayList<>();

    private static ListMultimap<String, Location> LOCATION_MULTIMAP = ArrayListMultimap.create();


    public static final DistanceCalculator STOP_DISTANCE_CALCULATOR = new DistanceCalculator();

    //Broadcasts Actions
    public static final String BROADCAST_ACTION_ON_STAND_STILL = "com.dff.cordova.plugin.location.broadcast.StandStillReceiver";
    public static final String BROADCAST_ACTION_STOP = "com.dff.cordova.plugin.location.broadcast.StandStillReceiver#stop";
    public static final String ACTION_SET_STOP_LISTENER = "location.action.SET_STOP_LISTENER";
    public static final String BROADCAST_ACTION_ON_NEW_LOCATION = "com.dff.cordova.plugin.location.broadcast.BroadcastNewLocationReceiver";
    public static final String BROADCAST_ACTION_ON_CHANGED_PROVIDER = "com.dff.cordova.plugin.location.broadcast.BroadcastChangeProviderReceiver";

    //Bundle keys
    public static final String LOCATION_RETURN_TYPE_KEY = "location.return.TYPE_KEY";
    public static final String IS_LOCATION_MANAGER_LISTENING = "is.location.manager.listening";
    public static final String LOCATION_MIN_TIME_KEY = "location.min.time.key";
    public static final String LOCATION_MIN_DISTANCE_KEY = "location.min.distance.key";
    public static final String IS_PROVIDER_ENABLED = "location.IS_PROVIDER_ENABLED";

    //JSON keys (params)
    public static final String RETURN_TYPE = "returnType";
    public static final String MIN_TIME = "minTime";
    public static final String MIN_DISTANCE = "minDistance";
    public static final String MIN_ACCURACY = "minAccuracy";
    public static final String MAX_AGE = "locationMaxAge";
    public static final String DELAY = "locationRequestDelay";
    public static final String RESET = "reset";
    public static final String CLEAN = "clean";

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

    public static int DISTANCE_CALCULATOR_STOP_DELAY = 60000; //1min


    /**
     * Enumeration to deal while parsing the actions.
     */
    public enum WHAT {
        START_LOCATION_SERVICE,
        STOP_LOCATION_SERVICE,
        GET_LOCATION,
        SET_LOCATION_LISTENER
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

    /*++++++++++++++++++++++++++SETTER++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /*++++++++++++++++++++++++++OTHERS++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/


    public static synchronized void addLocationToMultimap(Location location) {
        LOCATION_MULTIMAP.put(STOP_ID, location);
    }

    public static synchronized void clearLocationsMultimap() {
        try {
            LOCATION_MULTIMAP.clear();
        } catch (ConcurrentModificationException e) {
            CordovaPluginLog.e(TAG, "Error @ConcurrentModificationException: while clearing the location hash map: ", e);
        }
    }

    public static synchronized ListMultimap<String, Location> getLocationsMultimap() {
        try {
            return LOCATION_MULTIMAP;
        } catch (ConcurrentModificationException e) {
            CordovaPluginLog.e(TAG, "Error @ConcurrentModificationException: while getting the location hash map: ", e);
        }
        return null;
    }

    public static synchronized void logLocationsMultimap() {
        assert LOCATION_MULTIMAP != null;
        for (String stopID : LOCATION_MULTIMAP.keySet()) {
            Collection<Location> locations = LOCATION_MULTIMAP.get(stopID);
            CordovaPluginLog.d(TAG, "stopID: " + stopID + " --> " + locations);
        }
    }

}