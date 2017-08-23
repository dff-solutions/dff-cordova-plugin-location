package com.dff.cordova.plugin.location.events;

import com.dff.cordova.plugin.location.resources.Res;

/**
 * On changed location --> emit res object
 */
public class OnChangedLocation {

    private Res mRes;

    public OnChangedLocation(Res mRes) {
        this.mRes = mRes;
    }

    public Res getRes() {
        return mRes;
    }

    public void setRes(Res mRes) {
        this.mRes = mRes;
    }
}
