# dff-cordova-plugin-location

Location based tracking system

## Supported platforms

- Android

##Installation

- cordova plugin add [https://github.com/dff-solutions/dff-cordova-plugin-location.git]()
- or
- ionic plugin add [ https://github.com/dff-solutions/dff-cordova-plugin-location.git]()


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

##--------------------------------------------------------------------------------

1. #### startService
```js
/**
 * Start the location plugins's service. The service will be
 * automatically started on initializing the plugin.
 *
 * @param success - Success callback function
 * @param error - Error callback function
 */
LocationPlugin.startService(success, error);
```
2. #### stopService
```js
/**
 * Stop the location service.
 *
 * @param success - Success callback function
 * @param error - Error callback function
 */
LocationPlugin.stopService(success, error);
```
3. #### setMinAccuracy
```js
/**
 * Set the minimum accuracy value in order to compare the changed location, whether to store it.
 *
 * @param success - Success callback function
 * @param error - Error callback function
 * @param minAccuracy - The value of the minimum accuracy that is required to store a location
 */
LocationPlugin.setMinAccuracy(success, error, minAccuracy);
```
4. #### setMaxAge
```js
/**
 * Set the maximum value of the age of the location in order to discard it when this value is reached.
 *
 * @param success - Success callback function
 * @param error - Error callback function
 * @param maxAge - The value of the maximum age in "seconds"
 */
LocationPlugin.setMaxAge(success, error, maxAge);
```
5. #### getLocation
```js
/**
 * Get the last good saved location of the device.
 * good means accuracy < min accuracy (per default 20m)
 *
 * @param success - Success callback function
 * @param error - Error callback function
 * @param returnType - 0 for String Location | 1 for JSON Location
 */
LocationPlugin.getLocation(function(location) {
  console.log("location");
  //example for string location
  //9.92885613|51.53705706|0.0|0.0
  
  //example for json location
  //todo
  
}, error, returnType);
```
6. #### getLocationList
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
7. #### runTotalDistanceCalculator
```js
/**
 * Run the mechanism in order to calculate the total achieved distance.
 *
 * @param success - Success callback function
 * @param error - Error callback function.
 */
LocationPlugin.runTotalDistanceCalculator(success, error);
```
8. #### runCustomDistanceCalculator
```js
/**
 * Run the mechanism in order to calculate a custom achieved distance.
 *
 * @param success - Success callback function
 * @param error - Error callback function.
 */
LocationPlugin.runCustomDistanceCalculator(success, error);
```
9. #### getTotalDistance
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
10. #### getCustomDistance
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

