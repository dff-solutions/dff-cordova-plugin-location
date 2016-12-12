/**
 * JavaScript that consist of 8 Methods in order to call the functionality of
 * the location plugin, the Java native code.
 *
 * @author Anthony Nahas
 * @version 1.5
 * @since 28.11.2016
 */
var exec = require('cordova/exec');

const FEATURE = "LocationPlugin";

const ACTION_SET_MIN_ACCURACY = "location.action.SET_MIN_ACCURACY";
const ACTION_SET_MAX_AGE = "location.action.SET_MAX_AGE";
const ACTION_START_SERVICE = "location.action.START_SERVICE";
const ACTION_STOP_SERVICE = "location.action.STOP_SERVICE";
const ACTION_GET_LOCATION = "location.action.GET_LOCATION";
const ACTION_GET_LOCATION_JSON = "location.action.GET_LOCATION_JSON";
const ACTION_GET_LOCATION_LIST = "location.action.GET_LOCATION_LIST";
const ACTION_INTENT_STORE_PENDING_LOCATIONS = "location.action.intent.STORE_PENDING_LOCATIONS";
const ACTION_INTENT_RESTORE_PENDING_LOCATIONS = "location.action.intent.RESTORE_PENDING_LOCATIONS";

function LocationPlugin() {
    console.log("LocationPlugin.js has been created");
}

LocationPlugin.prototype.setMinAccuracy = function (success, error, minAccuracy) {
    exec(success, error, FEATURE, ACTION_SET_MIN_ACCURACY, [minAccuracy])
};

LocationPlugin.prototype.setMaxAge = function (success, error, maxAge) {
    exec(success, error, FEATURE, ACTION_SET_MAX_AGE, [maxAge])
};


LocationPlugin.prototype.getLocation = function (success, error) {
    exec(success, error, FEATURE, ACTION_GET_LOCATION, []);
};

//used in chrome for test purposes!
LocationPlugin.prototype.getLocationAsTest = function (returnType) {
    exec(function (location) {
        console.log(location);
    }, function (errorMsg) {
        console.log(errorMsg);
    }, FEATURE, ACTION_GET_LOCATION, [returnType]);
};

LocationPlugin.prototype.getLocationAsJson = function (success, error) {
    exec(success, error, FEATURE, ACTION_GET_LOCATION_JSON, []);
};

LocationPlugin.prototype.getLocationList = function (success, error) {
    exec(success, error, FEATURE, ACTION_GET_LOCATION_LIST, []);
};

LocationPlugin.prototype.storePendingLocations = function () {
    exec(function () {

    }, function () {

    }, FEATURE, ACTION_INTENT_STORE_PENDING_LOCATIONS, []);
};

LocationPlugin.prototype.restorePendingLocations = function () {
    exec(function () {

    }, function () {

    }, FEATURE, ACTION_INTENT_RESTORE_PENDING_LOCATIONS, []);
};

LocationPlugin.prototype.startService = function (success, error) {
    exec(success, error, FEATURE, ACTION_START_SERVICE, []);
};

LocationPlugin.prototype.stopService = function (success, error) {
    exec(success, error, FEATURE, ACTION_STOP_SERVICE, []);
};


module.exports = new LocationPlugin();