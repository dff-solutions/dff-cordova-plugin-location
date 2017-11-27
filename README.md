# dff-cordova-plugin-location
# & 
# Location Android Module

Location based tracking system

## Supported platforms

- Android 

#### supported dff-web-core - 1.3.0

## Plugin@latest

- Android: 9.1.0-beta.4

## Plugin@stable

- Android: 9.0.0

## Releases:
-  9.1.0-beta.4: Ref: supporting stop location service to re-init the location manager
- 9.0.1: Feat: using apache common lang lib for eql and hash methods + Test @GLocation class
- 9.0.0 Fix: @service cnx --> on unbind service | Feat: @Override clone, toString, hash and equal
| Feat: integration GLocation objs in location's arraylist
| Feat: implemented GLocation class --> @js and @server
| Feat: changed the software architect design - using DI with dagger2 - integration of the EVENT_BUS -
  start location service @param removed return type - stp service action has success callback - 
  setLocationListener and getLocation methods do not accept any params any more --> return type is always json - 
  properties of the json location changed to the native properties of a location object - getLocation method will not
  reset/clear the location list anymore --> implemented a new action: clear location list action - 
  enable mapping action has a optional boolean param in order to enable/disable location's mapping - 
  get total distance and getStopDistance have a new success callback that contains a json obj with (keyset/stopID and distance)
- 8.0.1: Fix: @getTotalDistance 
- 8.0.0: Ref: converted the Location plugin as well as android module library @Instrumental Tests! RELEASE 8.0.0
- 7.2.4: Ref: updated git url in package.json
- 7.2.3: Fix: json args key refactored: clear --> reset
- 7.2.2: Fix: clear after getTotalDistance
- 7.2.1: Fix: get locations list fixed
- 7.2.0: Feat: GPS Provider Listener
- 7.1.0: Fix: restoring locations'multimap fixed
- 7.0.2: Fix: error callback if stopdID was not found or the appropriate locationsList is empty
- 7.0.1: Fix: added a callback after enabling locations'mapping
- 7.0.0: Ref: new algorithm to calculate the achieved distance using the "DistanceSimulator" class
- 6.0.0: Feat: added distance simulator and refactored action parsing process | added mock locations for the simulation
- 5.0.3: Fix: correction of adding a new location in the array lists
- 5.0.2: Fix: remove updates of the location manager only if it has been already initialized
- 5.0.1: Ref: requesting permissions will be performed by the common plugin @TargetAPI(21)
- 5.0.0: Feat: properties will be stored in the shared preference | recognize whether the user has already started the 
service and initialized the location manager | Fix: initialize the location manager after respawn a new process | 
 Fix: using preference helper to verify whether the location manager has been initialized | Ref: setting is service started take place in the location service handler class and not in the executor class
| Feat: store/restore function for properties in the preference helper class | Feat: handling the location manager on crash 
| Fix: switch location return type in location holder class | Fix: file helper class supports now storing location in string as well as in json format (json format not completely done)
| Fix: supporting writing json objects in a file | Fix: handling EOF exception :) |Feat: supporting optional param to clear the list after forwarding it to JS/TS
- 4.5.7: Fix: getLocationList method fixed after refactoring returnType property 
- 4.5.6: Fix: added fallbacks to params in start service method 
- 4.5.5: Feat: supporting storing/restoring location list as json on crash
- 4.5.4: HOT-Fix: refactoring errors fixed (clear lists)
- 4.5.3: Ref: json keys of a location obj
- 4.5.2: Feat: supporting get location list for json obj locations.
- 4.5.1: Feat: enhanced the location resources in order to allocate location in json format
- 4.5.0: Feat: starting the location service supports now params (returnType,minTime,minDistance,minAccuracy,locationMaxAge,locationRequestDelay)
- 4.4.0: Fix: @Target Android - broadcast will be only sent and received within the installed package (local)
- 4.3.3: Feat: (minDistance) minimum distance between location updates, in meters added as param to the startService method
- 4.3.2: Feat: targetCompatibility JavaVersion.VERSION_1_7 - added build-extras-gradle
- 4.3.1: Feat: string switch statement (cordova)
- 4.3.0: FEAT: @Target Android version starting with API 23: Requesting Locations Permission!
- 4.2.0: (Feat) the ability now to configure several methods
- 4.1.4: (Fix) stop listener improved
- 4.1.3: (Ref) actions'name refactored
- 4.1.2: (Ref/Fix) actions'name changed and fixed
- 4.1.1: (Fix) plugin.xml updated and stopHolder class improved
- 4.1.0: (Fix) update of the plugin.xml and LocationPlugin.js
- 4.0.4: (Feat) ability to store custom and total distance frequently
- 4.0.3: (Ref) changed removecallback's method for dist calculators 
- 4.0.2: //to ignore :)
- 4.0.0: dff-cordova-plugin-location        

##Installation

- cordova plugin add [https://github.com/dff-solutions/dff-cordova-plugin-location.git]()
- or
- ionic plugin add [https://github.com/dff-solutions/dff-cordova-plugin-location.git]()

## Plugin Migration from <9.0.0 to > 9.0.0
- getLocation has no params (@returnType)
- getLocationList has params without returnType key property
- new: clearLocationList action
- new: get key set
- no params for getLastStopID
- setLocationListener has no params (@returnType)
- location is only in json format


## Documentation
- <a href="https://dff-solutions.github.io/dff-cordova-plugin-location/" target="_blank" >JAVA DOC</a>


## Usage

The plugin is available via the global variable `**LocationPlugin**`.

## @Deprecated
+ setMaxAge()
+ setMinTime()
+ setMAxAccuracy()
+ runTotalDistanceCalculator()
+ runCustomDistanceCalculator()

## Methods

###### Assumption

- Success Callback Function
- Success Callback Function
```js
var success = function() {
  // (Y) do what every you want...! 
}
```

- Error Callback Function
```js
var error = function(errorMsg) {
    console.log(errorMsg);
  // (Y) do what every you want...! 
}
```
----

#### startService
```js
/**
 * Start the location plugins's service. The service will **`not`** be
 * automatically started on initializing the plugin.
 *
 * @param success - Success callback function
 * @param error - Error callback function
 * @param params - forward params in order to adjust the functionality of the plugin.
 */
LocationPlugin.startService(success, error, params);

params {
    minTime: 15000, // 15sec - default 0
    minDistance: 50, // 50m - default 0
    minAccuracy: 20, // 20m - default 20
    locationMaxAge: 30 // 30sec - default 30
}

minTime| long: minimum time interval between location updates, in milliseconds
minDistance| float: minimum distance between location updates, in meters
minAccuracy| int: minimum accuracy to allocation a good location 
locationMaxAfe| int: the max age to delete the last allocated good location
locationRequestDelay| int: the delay time to allocate a new good location
```

----
#### stopService
```js
/**
 * Stop the location service.
 *
 * @param success - Success callback function
 * @param error - Error callback function
 */
LocationPlugin.stopService(success, error);
```
----


----
#### getLocation
```js
/**
 * Get the last good saved location of the device.
 * good means accuracy < min accuracy (per default 20m)
 *
 * @param returnType - 0 for String Location | 1 for JSON Location
 * @param success - Success callback function
 * @param error - Error callback function
 */
LocationPlugin.getLocation(function(location) {
  console.log("location");
  //example for string location
  //9.92885613|51.53705706|0.0|0.0
  
  //example for json location
  // Object {Accuracy: 12, Latitude: 51.53692106, Speed: 0, Altitude: 205, Longitude: 9.92879299…}
   {
    accuracy: 9401
    altitude: 198
    bearing: 0
    latitude: 51.537010073661804
    longitude: 9.929113984107971
    speed: 0
    time: 1502787852011
    }
  
}, error);
```
----
#### getLocationList
 ```js
 /**
  * Get the stored location as JSON ARRAY.
  *
  * @param success - Success callback function.
  * @param error - Error callback function.
  * @param (params) - "reset" whether to clear after sending the location list (default reset = true)
  */
 LocationPlugin.getLocationsList( 
     function(locations) {
   //example
    //location 0...N = 9.92885613|51.53705706|0.0|0.0|1481283812139
 },
  error, {  reset: false });
 ```
 ----
 
 #### clearLocationList
 ```js
 /*
 * Clear the location list
 *
 * @param success - Success callback function.
 * @param error - Error callback function.
 */
 LocationPlugin.prototype.clearLocationsList = function (success, error) {
     exec(success, error, FEATURE, CLEAR_LOCATION_LIST);
 };
  ```
  ----
----
 
#### enableMapping
  ```js
  /**
   * Enable mapping in order the persist the received locations in the locations'multi map
   *
   * @param success - Success callback function
   * @param error - Error callback function.
   * @param enable? - optional boolean param in order to enable/disable locations'mapping
   * The default value is true
   */
  LocationPlugin.enableMapping = function (success, error,enable?) {
      exec(success, error, FEATURE, ACTION_ENABLE_MAPPING_LOCATIONS, [enable]);
  };
  ```
  
  
  #### getKeySet
  ```js
  /**
   * Returns a view collection of all distinct keys contained in this multimap.
   * 
   * note: keyset is typeof json array:D
   *
   * @param success - Success callback function
   * @param error - Error callback function.
   */
  LocationPlugin.getKeySet = function (function (keySet)
  {console.log(keyset)}, error);
  ```
  
  ----
  #### setStopID
  ```js
  /**
   * set a new stop id as key for the location hash map
   *
   * @param success  - Success callback function
   * @param error rror - Error callback function.
   * @param params- stop id to hash it in the location hash map --> {stopID: id}
   */
  LocationPlugin.setStopID = function (success, error, params) {
  };
  ```
  ----
  #### getLastStopID
  ```js
  /**
   * Get the last stored stop id
   *
   * @param success - Success callback function
   * @param error - Error callback function.
   */
  LocationPlugin.getLastStopID = function (function(stopID) {console.log(stopID)}, error) {
  };
  ```
  ----
  #### getStopDistance
  ```js
  /**
   * Clear the stop id key and get the achieved distance to the appropriate stop
   *
   * @param success - Success callback function
   * @param error - Error callback function.
   * @param params -  {stopID: id, clear:false}
   * | stop id to import from the locations multimap
   * | reset - to clear the stopID --> stopID = UNKNOWN
   */
  LocationPlugin.getStopDistance = function (function(result) {
      console.log(resul.stopdID); 
      console.log(resul.distance);
      }, error, params) {
   
  };
  ```
  
----
#### getTotalDistance
```js
/**
 * Get the calculated total distance.
 *
 * Reset: whether to reset the multimap at the end or not
 * Clean: whether with stopID "UNKNOWN" or not
 *
 * @param success - Success callback function
 * @param error - Error callback function.
 * @param params - {reset: false, clean: true}
 */
LocationPlugin.getTotalDistance(function(result){
      console.log(resul.stopdID);
      console.log(resul.distance);
}, error, {reset: false, clean: true});
```
----

#### restorePendingLocations
```js
/**
 * Restore from a file the pending locations.
 *
 * NB: (for test purposes)!
 * @param success - Success callback function
 * @param error - Error callback function.
 */
LocationPlugin.restorePendingLocations = function (success, error);
```
----
#### setLocationListener
```js
/**
 * Set a location listener in order to receive the newest location.
 *
 * @param success - Success callback function
 * @param error - Error callback function
 */
LocationPlugin.setLocationListener = function (success, error);
```
----
#### setStopListener (todo: need core update for GLocation class!!)
```js
/**
 * Set a stop listener that recognize a stop with criteria (min 50m every 30secs - 10 times)
 *
 * @param success - Success callback function
 * @param error - Error callback function
 * @param frequency - how often
 * @param minDistance - the minimum distance that should be achieved
 * @param delay - the delay time between the first and a subsequent reorganization
 */
LocationPlugin.setStopListener = function (success, error, frequency, minDistance, delay);
```
----
#### cancelStopListener
```js
/**
 * Cancel the stop listener
 *
 * @param success - Success callback function
 * @param error - Error callback function
 */
LocationPlugin.cancelStopListener = function (success, error);
```

#### registerGPSProviderListener
```js
/**
 *
 * Register a gps provider listener. When the provider changes, a broadcast will be fired!
 *
 * @param success - Success callback function @Rerturn: whether the GPS provider is enbaled or not
 * @param error - Error callback function
 */
LocationPlugin.registerGPSProviderListener(function(isLocationEnabled) {
    console.log(isLocationEnabled);
}, error) {

};
```


#### unregisterGPSProviderListener
```js
/**
 * Unregister the gps provider listener
 *
 * @param success - Success callback function
 * @param error - Error callback function
 */
LocationPlugin.unregisterGPSProviderListener = function (success, error) {
};
```


# test
```
./gradlew runTest
```