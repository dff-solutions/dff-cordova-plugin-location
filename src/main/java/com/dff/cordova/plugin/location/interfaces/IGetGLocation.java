package com.dff.cordova.plugin.location.interfaces;

public interface IGetGLocation {

    String getUUID();

    double getLongitude();

    double getLatitude();

    double getAltitude();

    float getAccuracy();

    float getSpeed();

    float getBearing();

    long getTime();
}
