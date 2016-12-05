/**
 * Created by anahas on 28.11.2016.
 */
var exec = require('cordova/exec');

const PLUGIN_NAME = "LocationPlugin";

const ACTION_SET_MIN_ACCURACY = "location.action.SET_MIN_ACCURACY";
const ACTION_SET_MAX_AGE = "location.action.SET_MAX_AGE";
const ACTION_START_SERVICE = "location.action.START_SERVICE";
const ACTION_STOP_SERVICE = "location.action.STOP_SERVICE";
const ACTION_GET_LOCATION = "location.action.GET_LOCATION";
const ACTION_INTENT_STORE_PENDING_LOCATIONS = "location.action.intent.STORE_PENDING_LOCATIONS";
const ACTION_INTENT_RESTORE_PENDING_LOCATIONS = "location.action.intent.RESTORE_PENDING_LOCATIONS";

function LocationPlugin() {
    console.log("LocationPlugin.js has been created");
}

LocationPlugin.prototype.setMinAccuracy = function (success, error, minAccuracy) {
    exec(success, error, PLUGIN_NAME, ACTION_SET_MIN_ACCURACY, [minAccuracy])
};

LocationPlugin.prototype.setMaxAge = function (success, error, maxAge) {
    exec(success, error, PLUGIN_NAME, ACTION_SET_MAX_AGE, [maxAge])
};

//not done
LocationPlugin.prototype.getLocation = function () {
    exec(function (location) {
        console.log('Location = ' + location);
    }, function () {
        console.log('error on getLocation()');
    }, PLUGIN_NAME, ACTION_GET_LOCATION);
};

LocationPlugin.prototype.storePendingLocations = function () {
    exec(function () {

    }, function () {

    }, PLUGIN_NAME, ACTION_INTENT_STORE_PENDING_LOCATIONS);
};

LocationPlugin.prototype.restorePendingLocations = function () {
    exec(function () {

    }, function () {

    }, PLUGIN_NAME, ACTION_INTENT_RESTORE_PENDING_LOCATIONS);
};

LocationPlugin.prototype.startService = function (success, error) {
    exec(success, error, PLUGIN_NAME, ACTION_START_SERVICE);
};

LocationPlugin.prototype.stopService = function (success, error) {
    exec(success, error, PLUGIN_NAME, ACTION_STOP_SERVICE);
};


module.exports = new LocationPlugin();