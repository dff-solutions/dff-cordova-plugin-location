const cordovaExec = require("dff-cordova-plugin-location.cordova-exec");

const feature = "LocationPlugin";
const ACTION_PREFIX_REALM = "realm.";

const LocationPlugin = cordovaExec(feature, [
    "deleteLocations",
    "deleteLocationUpdateRequest",
    "deleteProximityAlert",
    "deleteProximityAlertEvents",
    "getAllProviders",
    "getConfig",
    "getLastKnownLocation",
    "getLocations",
    "getLocationUpdateRequests",
    "getProvider",
    "getProximityAlertEvents",
    "getProximityAlerts",
    "isLocationEnabled",
    "isProviderEnabled",
    "onLocationStatusChanged",
    "onLocationUpdate",
    "onProviderEnabled",
    "onProximityAlert",
    "pause",
    "readDiagnostics",
    "requestLocationUpdate",
    "requestProximityAlert",
    "resume",
    "setConfig",
    "startForeground",
    "stopForeground"
]);

LocationPlugin.realm = cordovaExec(feature, [
    "compact",
    "delete",
    "deleteAll",
    "getDatabaseInformation"
]
.map((action) => ({
    key: action,
    value: ACTION_PREFIX_REALM + action
})));

module.exports = LocationPlugin;
