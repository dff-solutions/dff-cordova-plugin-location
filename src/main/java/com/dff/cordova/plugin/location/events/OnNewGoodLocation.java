package com.dff.cordova.plugin.location.events;

import android.location.Location;

/**
 * Created by anahas on 07.07.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 07.07.2017
 */

public class OnNewGoodLocation {

   private Location location;

    public OnNewGoodLocation(Location location) {
        this.location = location;
    }
}
