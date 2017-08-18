package com.dff.cordova.plugin.location.interfaces;

public interface IGLocation extends IGetGLocation {

    void setLongitude(double longitude);

    void setLatitude(double latitude);

    void setAltitude(double altitude);

    void setAccuracy(float accuracy);

    void setSpeed(float speed);

    void setBearing(float bearing);

    void setTime(long time);
}
