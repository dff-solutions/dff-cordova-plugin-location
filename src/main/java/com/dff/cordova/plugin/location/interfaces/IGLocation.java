package com.dff.cordova.plugin.location.interfaces;

import com.dff.cordova.plugin.location.classes.GLocation;
import org.json.JSONObject;

public interface IGLocation extends IGetGLocation {

    void setLongitude(double longitude);

    void setLatitude(double latitude);

    void setAltitude(double altitude);

    void setAccuracy(float accuracy);

    void setSpeed(float speed);

    void setBearing(float bearing);

    void setTime(long time);

    JSONObject toJson();

    GLocation fromJson(String json);
}
