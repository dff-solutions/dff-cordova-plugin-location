# Usage Location

<!-- TOC depthFrom:2 -->

- [Constants](#constants)
- [Actions](#actions)
    - [deleteLocations](#deletelocations)
    - [deleteLocationUpdateRequest](#deletelocationupdaterequest)
    - [deleteProximityAlert](#deleteproximityalert)
    - [deleteProximityAlertEvents](#deleteproximityalertevents)
    - [getAllProvider](#getallprovider)
    - [getConfig](#getconfig)
    - [getLastKnownLocation](#getlastknownlocation)
    - [getLocations](#getlocations)
    - [getLocationUpdateRequests](#getlocationupdaterequests)
    - [getProvider](#getprovider)
    - [getProximityAlertEvents](#getproximityalertevents)
    - [getProximityAlerts](#getproximityalerts)
    - [isLocationEnabled](#islocationenabled)
    - [isProviderEnabled](#isproviderenabled)
    - [onLocationStatusChanged](#onlocationstatuschanged)
    - [onLocationUpdate](#onlocationupdate)
    - [onProviderEnabled](#onproviderenabled)
    - [onProximityAlert](#onproximityalert)
    - [pause](#pause)
    - [readDiagnostics](#readdiagnostics)
    - [requestLocationUpdate](#requestlocationupdate)
    - [requestProximityAlert](#requestproximityalert)
    - [resume](#resume)
    - [setConfig](#setconfig)
    - [startForeground](#startforeground)
    - [stopForeground](#stopforeground)

<!-- /TOC -->

This plugin is available via the global `LocationPlugin`.
For more details read the [LocationPlugin Wiki](https://github.com/dff-solutions/dff-cordova-plugin-location/wiki)

## Constants

Constants are used in the [LocationCriteria].

```ts
/**
 * A constant indicating an approximate accuracy requirement.
 */
const ACCURACY_COARSE: number = 2; // (0x00000002)

/**
 * A constant indicating a finer location accuracy requirement.
 */
const ACCURACY_FINE: number = 1; // (0x00000001)

/**
 * A constant indicating that the application does not choose
 * to place requirement on a particular feature.
 */
const NO_REQUIREMENT: number = 0; // (0x00000000)

/**
 * A constant indicating a low location accuracy requirement -
 * may be used for horizontal, altitude, speed or bearing accuracy.
 * For horizontal and vertical position this corresponds
 * roughly to an accuracy of greater than 500 meters.
 */
const ACCURACY_LOW: number =  1; // (0x00000001)

/**
 * A constant indicating a medium accuracy requirement -
 * currently used only for horizontal accuracy.
 * For horizontal position this corresponds roughly to an accuracy of
 * between 100 and 500 meters.
 */
const ACCURACY_MEDIUM: number = 2; // (0x00000002)

/**
 * A constant indicating a high accuracy requirement -
 * may be used for horizontal, altitude, speed or bearing accuracy.
 * For horizontal and vertical position this corresponds roughly to an
 * accuracy of less than 100 meters.
 */
const ACCURACY_HIGH: number = 3; // (0x00000003)

/**
 * A constant indicating a low power requirement.
 */
const POWER_LOW: number = 1; // (0x00000001)

/**
 * A constant indicating a medium power requirement.
 */
const POWER_MEDIUM: number =  2; // (0x00000002)

/**
 * A constant indicating a high power requirement.
 */
const POWER_HIGH: number = 3; // (0x00000003)
```

## Actions

All actions are queued and executed in a single thread one after the other.
Before an action is executed permissions are checked.
If not all required permissions are granted the error callback is called with
an exception.
To avoid dataloss the using app of the plugin has to prepare for those and other
errors. It should rollback or provide a backup for affected data.

### deleteLocations

Please see [LocationFilter].

```ts
/**
 * Delete locations filtered by parameters.
 *
 * @param {LocationFilter} args for filtering locations to delete
 */
LocationPlugin
    .deleteLocations(
        (success) => console.info(!!success),
        console.error,
        {
            ids: [ "one" ]
            limit: 1000
            time: 1234
            timeFrom: 1
            timeTo: 1000
        }
    );
```

### deleteLocationUpdateRequest

Please see [LocationUpdateRequest].

```ts
/**
 * Delete a specific location update request.
 * 
 * @param {string} id of the location update request
 * @return {LocationUpdateRequest} deleted request or null
 */
LocationPlugin
    .deleteLocationUpdateRequest(
        (request) => console.info(request),
        console.error,
        {
            id: "one" 
        }
    );
```

### deleteProximityAlert

Please see [ProximityAlert].

```ts
/**
 * Delete a specific proximity alert.
 * 
 * @param {string} id of proximity alert
 * @return {ProximityAlert} deleted proximity alert or null
 */
LocationPlugin
    .deleteProximityAlert(
        (alert) => console.info(alert),
        console.error,
        {
            id: "one" 
        }
    );
```

### deleteProximityAlertEvents

Please see [ProximityAlertEventFilter].

```ts
/**
 * Delete proximity alert events filtered by parameters.
 *
 * @param {ProximityAlertEventFilter} args for filtering proximity alert events
 * @return {number} 1 if success, 0 otherwise
 */
LocationPlugin
    .deleteProximityAlertEvents(
        (success) => console.info(!!success),
        console.error,
        {
            ids: [ "one" ]
            limit: 1000
            time: 1234
            timeFrom: 1
            timeTo: 1000
            entering: false
        }
    );
```

### getAllProvider

Please see [LocationProvider].

```ts
/**
 * Returns all available location providers.
 * 
 * @return {LocationProvider[]} array of available providers
 */
LocationPlugin
    .getAllProvider(
        console.info,
        console.error
    );
```

### getConfig

Pleasse see [LocationConfig].

```ts
/**
 * Get current configuration.
 *
 * @return {LocationConfig} current configuration
 */
LocationPlugin
    .getConfig(
        (config) => console.info(config),
        console.error
    );
```

### getLastKnownLocation

Please see [Location].

```ts
/**
 * Returns a Location indicating the data from the last known location fix 
 * obtained from the given provider.
 * 
 * @param {string} name of the provider
 * @return {Location} last known location
 */
LocationPlugin
    .getLastKnownLocation(
        console.info,
        console.error,
        {
            provider: "gps"
        }
    );
```

### getLocations

Please see [Location] and [LocationFilter].

```ts
/**
 * Returns Locations filtered by given arguments.
 * 
 * @param {LocationFilter} args for filtering locations
 * @return {Location[]} list of locations
 */
LocationPlugin
    .getLocations(
        console.info,
        console.error,
        {
            ids: [ "one" ]
            limit: 1000
            time: 1234
            timeFrom: 1
            timeTo: 1000
        }
    );
```

### getLocationUpdateRequests

Please see [LocationUpdateRequest]

```ts
/**
 * Returns map of all known location update requests.
 * 
 * @returns {Map<String, LocationUpdateRequests>} map of location update requests
 */
LocationPlugin
    .getLocationUpdateRequests(
        console.info,
        console.error
    );
```

### getProvider

Please see [LocationProvider].

```ts
/**
 * Returns a provider based on given provider name.
 * 
 * @param {string} name of a specific provider
 * @return {LocationProvider} requested provider
 */
LocationPlugin
    .getProvider(
        console.info,
        console.error,
        {
            provider: "gps"
        }
    );
```

### getProximityAlertEvents

Please see [ProximityAlertEventFilter] and [ProximityAlertEvent].

```ts
/**
 * Returns an array of proximity alert events filtered by given parameters.
 * 
 * @param {ProximityAlertEventFilter} args for filtering proximity alert events 
 * @return {ProximityAlertEvents[]} array of proximity alert events
 */
LocationPlugin
    .getProximityAlertEvents(
        console.info,
        console.error,
        {
            ids: [ "one" ]
            alertIds: ["two"]
            limit: 1000
            time: 1234
            timeFrom: 1
            timeTo: 1000
            entering: false
        }
    );
```

### getProximityAlerts

Please see [ProximityAlert].

```ts
/**
 * Returns a map of all known proximity alerts.
 * 
 * @return {Map<String, ProximityAlert>} map of proximity alerts
 */
LocationPlugin
    .getProximityAlerts(
        console.info,
        console.error
    );
```

### isLocationEnabled

```ts
/**
 * Get current enabled/disabled state of location mode.
 * Action requires SDK 28 or higher.
 *
 * @return {number} 1 if success, 0 otherwise
 */
LocationPlugin
    .isLocationEnabled(
        (success) => console.info(!!success),
        console.error
    );
```

### isProviderEnabled

Please see [LocationProvider].

```ts
/**
 * Checks if the given provider is enabled.
 *
 * @param {string} name of the provider
 * @return {number} 1 if success, 0 otherwise
 */
LocationPlugin
    .isProviderEnabled(
        (success) => console.info(!!success),
        console.error,
        {
            provider = "gps"
        }
    );
```

### onLocationStatusChanged

Please see [LocationStatusChangedEvent].

```ts
/**
 * Listen to location mode changes.
 *
 * @return {LocationStatusChangedEvent} event for status changed
 */
LocationPlugin
    .onLocationStatusChanged(
        (event) => console.info(event),
        console.error
    );
```

### onLocationUpdate

Please see [LocationUpdateEvent].

```ts
/**
 * Listen to location updates.
 *
 * @return {LocationUpdateEvent} event that contains new location
 */
LocationPlugin
    .onLocationUpdate(
        (event) => console.info(event),
        console.error
    );
```

### onProviderEnabled

Please see [ProviderEnabledEvent].

```ts
/**
 * Listen to location providers.
 *
 * @return {ProviderEnabledEvent} Event when a provider gets enabled/disabled
 */
LocationPlugin
    .onProviderEnabled(
        (event) => console.info(event),
        console.error
    );
```

### onProximityAlert

Please see [ProximityAlertEvent].

```ts
/**
 * Listen to ProximityAlerts.
 *
 * @return {ProximityAlertEvent} Event when a proximity alert is triggered
 */
LocationPlugin
    .onProximityAlert(
        (event) => console.info(event),
        console.error
    );
```

### pause

```ts
/**
 * Pause location updates and proximity alerts.
 */
LocationPlugin
    .pause(
        console.info,
        console.error
    );
```

### readDiagnostics

Please see [LocationDiagnostics].

```ts
/**
 * Read diagnostics.
 * 
 * @return {LocationDiagnostics}
 */
LocationPlugin
    .readDiagnostics(
        (diagnostic) => console.info(diagnostic),
        console.error
    );
```

### requestLocationUpdate

Please see [LocationUpdateRequest], [LocationProvider] and [LocationCriteria].

```ts
/**
 * Request location updates for given criteria or provider.
 * Provider or criteria must be set.
 * 
 * @param {LocationUpdateRequest} request wanted location request
 * @return {string} id of the created request
 */
LocationPlugin
    .requestLocationUpdate(
        console.info,
        console.error,
        {
            id: "one"
            minTime: 1234
            minDistance: 1.0
            provider: "gps"
            single: false
        }
    );
```

### requestProximityAlert

Please see [ProximityAlert].

```ts
/**
 * Request proximity alert for given arguments.
 * 
 * @param {ProximityAlert} alert requested alert
 * @return {string} id of the created alert
 */
LocationPlugin
    .requestProximityAlert(
        console.info,
        console.error,
        {
            id: "one"
            expiration: 1234
            latitude: 1.0
            longitude: 1.0
            radius: 1.0
        }
    );
```

### resume

```ts
/**
 * Resume location updates and proximity alerts.
 */
LocationPlugin
    .resume(
        console.info,
        console.error
    );
```

### setConfig

Please see [LocationConfig].

```ts
/**
 * Set config for location component. Without configuration no locations
 * are requested.
 *
 * @param {LocationConfig} Config location config
 */
LocationPlugin
    .setConfig(
        console.info,
        console.error,
        config
    );
```

### startForeground

```ts
/**
 * Start foreground service with notifcation to keep on receiving location
 * updates.
 */
LocationPlugin
    .startForeground(
        console.info,
        console.error
    );
```

### stopForeground

```ts
/**
 * Stop foreground service.
 */
LocationPlugin
    .stopForeground(
        console.info,
        console.error
    );
```

[Location]: https://github.com/dff-solutions/dff-cordova-plugin-location/wiki/Location
[LocationFilter]: https://github.com/dff-solutions/dff-cordova-plugin-location/wiki/LocationPlugin#locationfilter
[ProviderStatus]: https://github.com/dff-solutions/dff-cordova-plugin-location/wiki/LocationPlugin#providerstatus
[LocationUpdateEvent]: https://github.com/dff-solutions/dff-cordova-plugin-location/wiki/LocationPlugin#locationupdateevent
[LocationStatusChangedEvent]: https://github.com/dff-solutions/dff-cordova-plugin-location/wiki/LocationPlugin#locationstatuschangedevent
[LocationUpdateRequest]: https://github.com/dff-solutions/dff-cordova-plugin-location/wiki/LocationPlugin#locationupdaterequest
[ProximityAlert]: https://github.com/dff-solutions/dff-cordova-plugin-location/wiki/LocationPlugin#proximityalert
[ProximityAlertEventFilter]: https://github.com/dff-solutions/dff-cordova-plugin-location/wiki/LocationPlugin#proximityalerteventfilter
[ProximityAlertEvent]: https://github.com/dff-solutions/dff-cordova-plugin-location/wiki/LocationPlugin#proximityalertevent
[LocationConfig]: https://github.com/dff-solutions/dff-cordova-plugin-location/wiki/LocationPlugin#locationconfig
[ProviderEnabledEvent]: https://github.com/dff-solutions/dff-cordova-plugin-location/wiki/LocationPlugin#providerenabledevent
[LocationDiagnostics]: https://github.com/dff-solutions/dff-cordova-plugin-location/wiki/LocationPlugin#diagnostics
[LocationProvider]: https://github.com/dff-solutions/dff-cordova-plugin-location/wiki/LocationPlugin#locationprovider
[LocationCriteria]: https://github.com/dff-solutions/dff-cordova-plugin-location/wiki/LocationPlugin#locationcriteria
