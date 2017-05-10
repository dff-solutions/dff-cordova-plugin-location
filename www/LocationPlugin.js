/**
 * JavaScript that consist of 14 Methods in order to call the functionality of
 * the location plugin, the Java native code.
 *
 * @author Anthony Nahas
 * @version 7.2.3
 * @since 28.11.2016
 */
const exec = require('cordova/exec');

const FEATURE = "LocationPlugin";

const ACTION_SET_MIN_ACCURACY = "location.action.SET_MIN_ACCURACY";
const ACTION_SET_MAX_AGE = "location.action.SET_MAX_AGE";
const ACTION_SET_MIN_TIME = "location.action.SET_MIN_TIME";
const ACTION_START_SERVICE = "location.action.START_SERVICE";
const ACTION_STOP_SERVICE = "location.action.STOP_SERVICE";
const ACTION_GET_LOCATION = "location.action.GET_LOCATION";
const ACTION_GET_LOCATION_LIST = "location.action.GET_LOCATION_LIST";
const ACTION_INTENT_STORE_PENDING_LOCATIONS = "location.action.intent.STORE_PENDING_LOCATIONS";
const ACTION_INTENT_RESTORE_PENDING_LOCATIONS = "location.action.intent.RESTORE_PENDING_LOCATIONS";
const ACTION_RUN_TOTAL_DISTANCE_CALCULATOR = "distance.action.RUN_TOTAL_DISTANCE_CALCULATOR";
const ACTION_GET_TOTAL_DISTANCE = "distance.action.GET_TOTAL_DISTANCE_CALCULATOR";
const ACTION_RUN_CUSTOM_DISTANCE_CALCULATOR = "distance.action.RUN_CUSTOM_DISTANCE_CALCULATOR";
const ACTION_GET_CUSTOM_DISTANCE = "distance.action.GET_CUSTOM_DISTANCE_CALCULATOR";
const ACTION_SET_LOCATION_LISTENER = "location.action.SET_LOCATION_LISTENER";
const ACTION_SET_STOP_LISTENER = "location.action.SET_STOP_LISTENER";
const ACTION_CANCEL_STOP_LISTENER = "location.action.CANCEL_STOP_LISTENER";
const ACTION_ENABLE_MAPPING_LOCATIONS = "location.action.ACTION_ENABLE_MAPPING_LOCATIONS";
const ACTION_SET_STOP_ID = "hash_map.action.SET_STOP_ID";
const ACTION_GET_LAST_STOP_ID = "hash_map.action.GET_LAST_STOP_ID";
const ACTION_CLEAR_STOP_ID = "hash_map.action.CLEAR_STOP_ID";
const ACTION_GET_KEY_SET_FROM_LOCATIONS_MULTI_MAP = "location.action.GET_KEY_SET_FROM_LOCATIONS_MULTI_MAP";
const ACTION_REGISTER_PROVIDER_LISTENER = "location.action.ACTION_REGISTER_PROVIDER_LISTENER";
const ACTION_UNREGISTER_PROVIDER_LISTENER = "location.action.ACTION_UNREGISTER_PROVIDER_LISTENER";


function LocationPlugin() {
    console.log("LocationPlugin.js has been created");
}

/**
 * Set the minimum accuracy value in order to compare the changed location, whether to store it.
 *
 * @param success - Success callback function
 * @param error - Error callback function
 * @param minAccuracy - The value of the minimum accuracy that is required to store a location
 */
LocationPlugin.prototype.setMinAccuracy = function (minAccuracy, success, error) {
    exec(success, error, FEATURE, ACTION_SET_MIN_ACCURACY, [minAccuracy]);
};

/**
 * Set the maximum value of the age of the location in order to discard it when this value is reached.
 *minAccuracy
 * @param success - Success callback function
 * @param error - Error callback function
 * @param maxAge - The value of the maximum age in "seconds"
 */
LocationPlugin.prototype.setMaxAge = function (maxAge, success, error) {
    exec(success, error, FEATURE, ACTION_SET_MAX_AGE, [maxAge]);
};


/**
 * Update the minimum time of requesting a new location as configuration.
 * NOTE: PLEASE DO NOT UTILIZE!!!!
 *
 *
 * @param success - Success callback function
 * @param error - Error callback function.
 * @param minTime - The value of minimum time to request a new location in ms.
 */
LocationPlugin.prototype.setMinTime = function (minTime, success, error) {
    exec(success, error, FEATURE, ACTION_SET_MIN_TIME, [minTime]);
};

/**
 * set a new stop id as key for the location hash map
 *
 * @param success  - Success callback function
 * @param error rror - Error callback function.
 * @param stopID - stop id to hash it in the location hash map
 */
LocationPlugin.prototype.setStopID = function (success, error, stopID) {
    exec(success, error, FEATURE, ACTION_SET_STOP_ID, [stopID]);
};

/**
 * Clear the stop id key and get the achieved distance to the appropriate stop
 *
 * @param success - Success callback function
 * @param error - Error callback function.
 * @param params -  {stopID: id, clear:false}
 * | stop id to import from the locations multimap
 * | reset - to clear the stopID --> stopID = UNKNOWN
 */
LocationPlugin.prototype.getStopDistance = function (success, error, params) {
    exec(success, error, FEATURE, ACTION_CLEAR_STOP_ID, [params]);
};


/**
 * Enable mapping in order the persist the received locations in the locations'multi map
 *
 * @param success - Success callback function
 * @param error - Error callback function.
 */
LocationPlugin.prototype.enableMapping = function (success, error) {
    exec(success, error, FEATURE, ACTION_ENABLE_MAPPING_LOCATIONS, []);
};

/**
 * Returns a view collection of all distinct keys contained in this multimap.
 *
 * @param success - Success callback function
 * @param error - Error callback function.
 */
LocationPlugin.prototype.getKeySet = function (success, error) {
    exec(success, error, FEATURE, ACTION_GET_KEY_SET_FROM_LOCATIONS_MULTI_MAP, []);
};


/**
 * Get the last stored stop id
 *
 * @param success - Success callback function
 * @param error - Error callback function.
 */
LocationPlugin.prototype.getLastStopID = function (success, error) {
    exec(success, error, FEATURE, ACTION_GET_LAST_STOP_ID, []);
};

/**
 * Get the last good saved location of the device.
 * good means accuracy < min accuracy (per default 20m)
 *
 * @param success - Success callback function
 * @param error - Error callback function
 * @param returnType - 0 for String Location | 1 for JSON Location
 */
LocationPlugin.prototype.getLocation = function (returnType, success, error) {
    exec(success, error, FEATURE, ACTION_GET_LOCATION, [returnType]);
};

//used in chrome for test purposes!
/**
 * Get location of the device and log the result
 *
 * NB: for test purposes!
 *
 * @param returnType - 0 for String Location | 1 for JSON Location
 */
LocationPlugin.prototype.getLocationAsTest = function (returnType) {
    exec(function (location) {
        console.log(location);
    }, function (errorMsg) {
        console.log(errorMsg);
    }, FEATURE, ACTION_GET_LOCATION, [returnType]);
};

/**
 * Get the stored location as JSON ARRAY.
 *
 * @param success - Success callback function.
 * @param error - Error callback function.
 * @param params - "reset" whether to clear after sending the location list (default reset = true)
 */
LocationPlugin.prototype.getLocationsList = function (success, error, params) {
    exec(success, error, FEATURE, ACTION_GET_LOCATION_LIST, [params]);
};

/**
 * NB: only for test purposes
 */
LocationPlugin.prototype.getFullDistance = function () {
    exec(function (distance) {
        console.log("getFullDistance on success");
        if (distance) {
            console.log(distance);
        }
        else {
            console.log("Distance is not available ");
        }
    }, function (msg) {
        console.log(msg);
    }, FEATURE, ACTION_GET_TOTAL_DISTANCE, [])
};

/**
 * Run the mechanism in order to calculate the total achieved distance.
 *
 * @param success - Success callback function
 * @param error - Error callback function.
 */
LocationPlugin.prototype.runTotalDistanceCalculator = function (success, error) {
    exec(success, error, FEATURE, ACTION_RUN_TOTAL_DISTANCE_CALCULATOR, []);
};

/**
 * Run the mechanism in order to calculate a custom achieved distance.
 *
 * @param success - Success callback function
 * @param error - Error callback function.
 */
LocationPlugin.prototype.runCustomDistanceCalculator = function (success, error) {
    exec(success, error, FEATURE, ACTION_RUN_CUSTOM_DISTANCE_CALCULATOR, []);
};

/**
 * Get the calculated total distance.
 *
 * @param success - Success callback function
 * @param error - Error callback function.
 * @param params - {reset: false, clean: true}
 */
LocationPlugin.prototype.getTotalDistance = function (success, error, params) {
    exec(success, error, FEATURE, ACTION_GET_TOTAL_DISTANCE, [params]);
};

/**
 * Get the calculated custom distance.
 *
 * @param success - Success callback function
 * @param error - Error callback function.
 */
LocationPlugin.prototype.getCustomDistance = function (success, error) {
    exec(success, error, FEATURE, ACTION_GET_CUSTOM_DISTANCE, []);
};

/**
 * Store in a file the pending locations that are allocated in the location array list.
 *
 * NB: (for test purposes)!
 * @param success - Success callback function
 * @param error - Error callback function.
 */
LocationPlugin.prototype.storePendingLocations = function (success, error) {
    exec(success, error, FEATURE, ACTION_INTENT_STORE_PENDING_LOCATIONS, []);
};

/**
 * Restore from a file the pending locations.
 *
 * NB: (for test purposes)!
 * @param success - Success callback function
 * @param error - Error callback function.
 */
LocationPlugin.prototype.restorePendingLocations = function (success, error) {
    exec(success, error, FEATURE, ACTION_INTENT_RESTORE_PENDING_LOCATIONS, []);
};

/**
 * Start the location plugins's service. The service will be
 * automatically started on initializing the plugin.
 *
 * @param success - Success callback function
 * @param error - Error callback function
 * @param params - forward params in order to adjust the functionality of the plugin.
 */
LocationPlugin.prototype.startService = function (success, error, params) {
    exec(success, error, FEATURE, ACTION_START_SERVICE, [params]);
};

/**
 * Stop the location service.
 *
 * @param success - Success callback function
 * @param error - Error callback function
 */
LocationPlugin.prototype.stopService = function (success, error) {
    exec(success, error, FEATURE, ACTION_STOP_SERVICE, []);
};

/**
 * Set a location listener in order to receive the newest location.
 *
 * @param success - Success callback function
 * @param error - Error callback function
 * @param returnType -  0 for String Location | 1 for JSON Location
 */
LocationPlugin.prototype.setLocationListener = function (returnType, success, error) {
    exec(success, error, FEATURE, ACTION_SET_LOCATION_LISTENER, [returnType]);
};


/**
 * Set a stop listener that recognize a stop with criteria (min 50m every 30secs - 10 times)
 *
 * @param success - Success callback function
 * @param error - Error callback function
 * @param frequency - how often
 * @param minDistance - the minimum distance that should be achieved
 * @param delay - the delay time between the first and a subsequent reorganization
 */
LocationPlugin.prototype.setStopListener = function (success, error, frequency, minDistance, delay) {
    exec(success, error, FEATURE, ACTION_SET_STOP_LISTENER, [frequency, minDistance, delay]);
};

/**
 * Cancel the stop listener
 *
 * @param success - Success callback function
 * @param error - Error callback function
 */
LocationPlugin.prototype.cancelStopListener = function (success, error) {
    exec(success, error, FEATURE, ACTION_CANCEL_STOP_LISTENER, []);
};


/**
 *
 * Register a gps provider listener. When the provider changes, a broadcast will be fired!
 *
 * @param success - Success callback function
 * @param error - Error callback function
 */
LocationPlugin.prototype.registerGPSProviderListener = function (success, error) {
    exec(success, error, FEATURE, ACTION_REGISTER_PROVIDER_LISTENER, []);

};

/**
 * Unregister the gps provider listener
 *
 * @param success - Success callback function
 * @param error - Error callback function
 */
LocationPlugin.prototype.unregisterGPSProviderListener = function (success, error) {
    exec(success, error, FEATURE, ACTION_UNREGISTER_PROVIDER_LISTENER, []);

};


module.exports = new LocationPlugin();