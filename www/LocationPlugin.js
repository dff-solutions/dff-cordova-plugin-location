/**
 * JavaScript that consist of 14 Methods in order to call the functionality of
 * the location plugin, the Java native code.
 *
 * @author Anthony Nahas
 * @version 9.1.0-rc2
 * @since 28.11.2016
 */
const exec = require('cordova/exec');

const FEATURE = "LocationPlugin";


const ACTION_START_SERVICE = "location.action.START_SERVICE";
const ACTION_STOP_SERVICE = "location.action.STOP_SERVICE";
const ACTION_GET_LOCATION = "location.action.GET_LOCATION";
const ACTION_GET_LOCATION_LIST = "location.action.GET_LOCATION_LIST";
const ACTION_GET_TOTAL_DISTANCE = "distance.action.GET_TOTAL_DISTANCE_CALCULATOR";
const ACTION_CLEAR_LOCATION_LIST = "location.action.CLEAR_LOCATION_LIST";
const ACTION_INTENT_STORE_PENDING_LOCATIONS = "location.action.intent.STORE_PENDING_LOCATIONS";
const ACTION_INTENT_RESTORE_PENDING_LOCATIONS = "location.action.intent.RESTORE_PENDING_LOCATIONS";
const ACTION_RUN_TOTAL_DISTANCE_CALCULATOR = "distance.action.RUN_TOTAL_DISTANCE_CALCULATOR";
const ACTION_ENABLE_MAPPING_LOCATIONS = "location.action.ACTION_ENABLE_MAPPING_LOCATIONS";
const ACTION_SET_STOP_ID = "hash_map.action.SET_STOP_ID";
const ACTION_GET_LAST_STOP_ID = "hash_map.action.GET_LAST_STOP_ID";
const ACTION_CLEAR_STOP_ID = "hash_map.action.CLEAR_STOP_ID";
const ACTION_GET_KEY_SET_FROM_LOCATIONS_MULTI_MAP = "location.action.GET_KEY_SET_FROM_LOCATIONS_MULTI_MAP";
const ACTION_REGISTER_LOCATION_LISTENER = "location.action.ACTION_REGISTER_LOCATION_LISTENER";
const ACTION_REGISTER_STOP_LISTENER = "location.action.ACTION_REGISTER_STOP_LISTENER";
const ACTION_REGISTER_PROVIDER_LISTENER = "location.action.ACTION_REGISTER_PROVIDER_LISTENER";
const ACTION_UNREGISTER_LOCATION_LISTENER = "location.action.ACTION_UNREGISTER_LOCATION_LISTENER";
const ACTION_UNREGISTER_STOP_LISTENER = "location.action.ACTION_UNREGISTER_STOP_LISTENER";
const ACTION_UNREGISTER_PROVIDER_LISTENER = "location.action.ACTION_UNREGISTER_PROVIDER_LISTENER";


function LocationPlugin() {
    console.log("LocationPlugin.js has been created");
}

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
 * Get the last good saved location of the device.
 * good means accuracy < min accuracy (per default 20m)
 *
 * @param success - Success callback @param error - Error callback function
 * @param returnType - 0 for String Location | 1 for JSON Location
 */
LocationPlugin.prototype.getLocation = function (success, error) {
    exec(success, error, FEATURE, ACTION_GET_LOCATION, []);
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

/*
* Clear the location list
*
* @param success - Success callback function.
* @param error - Error callback function.
*/
LocationPlugin.prototype.clearLocationsList = function (success, error) {
    exec(success, error, FEATURE, ACTION_CLEAR_LOCATION_LIST);
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
 * @param enable? - optional boolean param in order to enable/disable locations'mapping
 */
LocationPlugin.prototype.enableMapping = function (success, error, enable) {
    exec(success, error, FEATURE, ACTION_ENABLE_MAPPING_LOCATIONS, [enable]);
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
 * Set a location listener in order to receive the newest location.
 *
 * @param success - Success callback function
 * @param error - Error callback function
 */
LocationPlugin.prototype.registerLocationListener = function (success, error) {
    exec(success, error, FEATURE, ACTION_REGISTER_LOCATION_LISTENER, []);
};

LocationPlugin.prototype.unregisterLocationListener = function (success, error) {
    exec(success, error, FEATURE, ACTION_UNREGISTER_LOCATION_LISTENER, []);
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
LocationPlugin.prototype.registerStopListener = function (success, error, frequency, minDistance, delay) {
    exec(success, error, FEATURE, ACTION_REGISTER_STOP_LISTENER, [frequency, minDistance, delay]);
};


/**
 * Cancel the stop listener
 *
 * @param success - Success callback function
 * @param error - Error callback function
 */
LocationPlugin.prototype.unregisterStopListener = function (success, error) {
    exec(success, error, FEATURE, ACTION_UNREGISTER_STOP_LISTENER, []);
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