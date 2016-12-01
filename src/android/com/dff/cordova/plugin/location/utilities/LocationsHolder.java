package com.dff.cordova.plugin.location.utilities;

import com.dff.cordova.plugin.location.resources.LocationResources;

/**
 * Created by anahas on 01.12.2016.
 *
 * @author Anthony Nahas
 * @version 0.1
 * @since 01.12.2016
 */
public class LocationsHolder implements Runnable {
    @Override
    public void run() {
        String location = LocationResources.getLastGoodLocationToString() +
                LocationResources.getLastGoodLocation().getTime();
        LocationResources.addLocationToList(location);
    }
}
