package com.dff.cordova.plugin.location.events;

import com.dff.cordova.plugin.location.classes.GLocation;

/**
 * Created by anahas on 07.07.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 07.07.2017
 */

public class OnNewGoodLocation {

    private GLocation location;

    public GLocation getLocation() {
        return location;
    }

    public OnNewGoodLocation(GLocation location) {
        this.location = location;
    }
}
