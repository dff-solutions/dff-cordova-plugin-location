package com.dff.cordova.plugin.location.resources;

import android.location.Location;

/**
 * Created by anahas on 30.11.2016.
 *
 * @author Anthony Nahas
 * @version 0.7
 * @since 30.11.2016
 */
public class LocationResources {

    private static Location LAST_GOOD_LOCATION = null;

    //settings
    public static final int LOCATION_MAX_ACCURAYCY = 20; //20 in production

    //what
    public static final int ACTION_GET_LOCATION = 1;


    //data keys
    public static final String DATA_LOCATION_KEY = "data_location_key";

    public static void setLastGoodLocation(Location location) {
        LAST_GOOD_LOCATION = location;
    }

    public static Location getLastGoodLocation() {
        return LAST_GOOD_LOCATION;
    }

    public static String getLastGoodLocationToString() {
        return LAST_GOOD_LOCATION.getLongitude() + "|" +
                LAST_GOOD_LOCATION.getLatitude() + "|" +
                getSpeedOfLastGoodLocation() + "|" +
                LAST_GOOD_LOCATION.getBearing() + "| ACC = " +
                LAST_GOOD_LOCATION.getAccuracy();
    }

    private static double getSpeedOfLastGoodLocation() {
        if (LAST_GOOD_LOCATION.hasSpeed() && LAST_GOOD_LOCATION.getSpeed() > 0) {
            return Math.round(LAST_GOOD_LOCATION.getSpeed() * 3.6);
        }
        return 0;
    }

}