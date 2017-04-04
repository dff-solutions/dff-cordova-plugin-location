# dff-cordova-plugin-location

Location based tracking system

## Supported platforms

- Android 

## Plugin@latest

- Android: 4.5.0

## Releases:
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

## Documentation
- <a href="https://dff-solutions.github.io/dff-cordova-plugin-location/" target="_blank" >JAVA DOC</a>


## Usage

The plugin is available via the global variable `**LocationPlugin**`.


## Methods

###### Assumption

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
```

#### params:
```js
params {
    returnType: 'dffString' || 'json', //default json
    minTIme: 15000, // 15sec - default 0
    minDistance: 50, // 50m - default 0
    minAccuracy: 20, // 20m - default 20
    locationMaxAge: 30, // 30sec - default 30
    locationRequestDelay: 20000 // 50sec - default 50k
}
```

```js
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
#### setMinAccuracy
```js
/**
 * Set the minimum accuracy value in order to compare the changed location, whether to store it.
 *
 * @param success - Success callback function
 * @param error - Error callback function
 * @param minAccuracy - The value of the minimum accuracy that is required to store a location
 */
LocationPlugin.setMinAccuracy(minAccuracy, success, error);
```
----
#### setMaxAge
```js
/**
 * Set the maximum value of the age of the location in order to discard it when this value is reached.
 *
 * @param success - Success callback function
 * @param error - Error callback function
 * @param maxAge - The value of the maximum age in "seconds"
 */
LocationPlugin.setMaxAge(maxAge, success, error);
```
----
#### setMinTime
```js
/**
 *
 * @param minTime - The value of minimum time to request a new location in ms.
 * @param success - Success callback function
 * @param error - Error callback function.
 */
LocationPlugin.setMinTime = function (minTime, success, error);
```
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
LocationPlugin.getLocation(returnType, function(location) {
  console.log("location");
  //example for string location
  //9.92885613|51.53705706|0.0|0.0
  
  //example for json location
  // Object {Accuracy: 12, Latitude: 51.53692106, Speed: 0, Altitude: 205, Longitude: 9.92879299…}
   ```json
    Accuracy: 8,
    Altitude: 203,
    Bearing: 262.5,
    Latitude: 51.53701007,
    Longitude: 9.9288611,
    Speed: 4,
    Time: 1482827340101
    ```
  
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
  */
 LocationPlugin.getLocationsList(function(locations) {
   //example
    //location 0...N = 9.92885613|51.53705706|0.0|0.0|1481283812139
 }, error);
 ```
 ----
#### runTotalDistanceCalculator
```js
/**
 * Run the mechanism in order to calculate the total achieved distance.
 *
 * @param success - Success callback function
 * @param error - Error callback function.
 */
LocationPlugin.runTotalDistanceCalculator(success, error);
```
----
#### runCustomDistanceCalculator
```js
/**
 * Run the mechanism in order to calculate a custom achieved distance.
 *
 * @param success - Success callback function
 * @param error - Error callback function.
 */
LocationPlugin.runCustomDistanceCalculator(success, error);
```
----
#### getTotalDistance
```js
/**
 * Get the calculated total distance.
 *
 * @param success - Success callback function
 * @param error - Error callback function.
 */
LocationPlugin.getTotalDistance(function(distnace){
      console.log(distnace);
}, error);
```
----
#### getCustomDistance
```js
/**
 * Get the calculated custom distance.
 *
 * @param success - Success callback function
 * @param error - Error callback function.
 */
LocationPlugin.getCustomDistance(function(distance) {
  console.log(distance);
}, error);
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
 * @param returnType -  0 for String Location | 1 for JSON Location
 * @param success - Success callback function
 * @param error - Error callback function
 */
LocationPlugin.setLocationListener = function (returnType, success, error);
```
----
#### setStopListener
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