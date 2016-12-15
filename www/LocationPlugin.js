/**
 * JavaScript that consist of 8 Methods in order to call the functionality of
 * the location plugin, the Java native code.
 *
 * @author Anthony Nahas
 * @version 2.5.0
 * @since 28.11.2016
 */
var exec = require('cordova/exec');

const FEATURE = "LocationPlugin";

const ACTION_SET_MIN_ACCURACY = "location.action.SET_MIN_ACCURACY";
const ACTION_SET_MAX_AGE = "location.action.SET_MAX_AGE";
const ACTION_START_SERVICE = "location.action.START_SERVICE";
const ACTION_STOP_SERVICE = "location.action.STOP_SERVICE";
const ACTION_GET_LOCATION = "location.action.GET_LOCATION";
const ACTION_GET_LOCATION_LIST = "location.action.GET_LOCATION_LIST";
const ACTION_INTENT_STORE_PENDING_LOCATIONS = "location.action.intent.STORE_PENDING_LOCATIONS";
const ACTION_INTENT_RESTORE_PENDING_LOCATIONS = "location.action.intent.RESTORE_PENDING_LOCATIONS";
const ACTION_RUN_TOTAL_DISTANCE_CALCULATOR = "distance.action.RUN_TOTAL_DISTANCE_CALCULATOR";
const ACTION_GET_TOTAL_DISTANCE = "distance.action.GET_TOTAL_DISTANCE";
const ACTION_RUN_CUSTOM_DISTANCE_CALCULATOR = "distance.action.RUN_CUSTOM_DISTANCE_CALCULATOR";
const ACTION_GET_CUSTOM_DISTANCE = "distance.action.GET_CUSTOM_DISTANCE";

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
LocationPlugin.prototype.setMinAccuracy = function (success, error, minAccuracy) {
    exec(success, error, FEATURE, ACTION_SET_MIN_ACCURACY, [minAccuracy])
};

/**
 * Set the maximum value of the age of the location in order to discard it when this value is reached.
 *
 * @param success - Success callback function
 * @param error - Error callback function
 * @param maxAge - The value of the maximum age in "seconds"
 */
LocationPlugin.prototype.setMaxAge = function (success, error, maxAge) {
    exec(success, error, FEATURE, ACTION_SET_MAX_AGE, [maxAge])
};


/**
 * get the last good saved location of the device.
 * good means accuracy < min accuracy (per default 20m)
 *
 * @param success - Success callback function
 * @param error - Error callback function
 * @param returnType - 0 for String Location | 1 for JSON Location
 */
LocationPlugin.prototype.getLocation = function (success, error, returnType) {
    exec(success, error, FEATURE, ACTION_GET_LOCATION, [returnType]);
};

//used in chrome for test purposes!
/**
 * get location of the device and log the result
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
 * get the stored location as JSON ARRAY
 *
 * @param success - Success callback function
 * @param error - Error callback function
 */
LocationPlugin.prototype.getLocationList = function (success, error) {
    exec(success, error, FEATURE, ACTION_GET_LOCATION_LIST, []);
};


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

LocationPlugin.prototype.runTotalDistanceCalculator = function (success, error) {
    exec(success, error, FEATURE, ACTION_RUN_TOTAL_DISTANCE_CALCULATOR, [])
};

LocationPlugin.prototype.runCustomDistanceCalculator = function (success, error) {
    exec(success, error, FEATURE, ACTION_RUN_CUSTOM_DISTANCE_CALCULATOR, [])
};


LocationPlugin.prototype.getTotalDistance = function (success, error) {
    exec(success, error, FEATURE, ACTION_GET_TOTAL_DISTANCE, []);
};

LocationPlugin.prototype.getCustomDistance = function (success, error) {
    exec(success, error, FEATURE, ACTION_GET_CUSTOM_DISTANCE, [])
};

/**
 * Store in a file the pending locations that are allocated in the location array list.
 * (for test purposes)
 */
LocationPlugin.prototype.storePendingLocations = function (success, error) {
    exec(success, error, FEATURE, ACTION_INTENT_STORE_PENDING_LOCATIONS, []);
};

/**
 * Restore from a file the pending locations.
 * (for test purposes)
 */
LocationPlugin.prototype.restorePendingLocations = function (success, error) {
    exec(success, error, FEATURE, ACTION_INTENT_RESTORE_PENDING_LOCATIONS, []);
};

/**
 * Start the location service. The service will be
 * automatically started on initializing the plugin.
 *
 * @param success - Success callback function
 * @param error - Error callback function
 */
LocationPlugin.prototype.startService = function (success, error) {
    exec(success, error, FEATURE, ACTION_START_SERVICE, []);
};

/**
 * Stop the location service
 *
 * @param success - Success callback function
 * @param error - Error callback function
 */
LocationPlugin.prototype.stopService = function (success, error) {
    exec(success, error, FEATURE, ACTION_STOP_SERVICE, []);
};


module.exports = new LocationPlugin();