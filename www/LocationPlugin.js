/**
 * JavaScript that consist of 14 Methods in order to call the functionality of
 * the location plugin, the Java native code.
 *
 * @author Anthony Nahas
 * @version 4.2.0
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
const ACTION_GET_TOTAL_DISTANCE = "distance.action.GET_TOTAL_DISTANCE";
const ACTION_RUN_CUSTOM_DISTANCE_CALCULATOR = "distance.action.RUN_CUSTOM_DISTANCE_CALCULATOR";
const ACTION_GET_CUSTOM_DISTANCE = "distance.action.GET_CUSTOM_DISTANCE";
const ACTION_SET_LOCATION_LISTENER = "location.action.SET_LOCATION_LISTENER";
const ACTION_SET_STOP_LISTENER = "location.action.SET_STOP_LISTENER";
const ACTION_CANCEL_STOP_LISTENER = "location.action.CANCEL_STOP_LISTENER";

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
 * //todo dynamically set and remove listener
 *
 * @param success - Success callback function
 * @param error - Error callback function.
 * @param minTime - The value of minimum time to request a new location in ms.
 */
LocationPlugin.prototype.setMinTime = function (minTime, success, error) {
    exec(success, error, FEATURE, ACTION_SET_MIN_TIME, [minTime]);
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
 */
LocationPlugin.prototype.getLocationsList = function (success, error) {
    exec(success, error, FEATURE, ACTION_GET_LOCATION_LIST, []);
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
 */
LocationPlugin.prototype.getTotalDistance = function (success, error) {
    exec(success, error, FEATURE, ACTION_GET_TOTAL_DISTANCE, []);
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
 * @param mintTime - minimum time interval between location updates, in milliseconds
 */
LocationPlugin.prototype.startService = function (success, error, mintTime) {
    exec(success, error, FEATURE, ACTION_START_SERVICE, [mintTime]);
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
 * Set a stop listener that recognize a stop with criteria (min 50m every 30secs)
 *
 * @param success - Success callback function
 * @param error - Error callback function
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
LocationPlugin.prototype.canelStopListener = function (success, error) {
    exec(success, error, FEATURE, ACTION_CANCEL_STOP_LISTENER, []);
};


module.exports = new LocationPlugin();