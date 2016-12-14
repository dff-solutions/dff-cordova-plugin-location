package com.dff.cordova.plugin.location.classes;

import android.util.Log;

/**
 * Created by anahas on 14.12.2016.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 14.12.2016
 */
public class RouteCalculator {

    private static final String TAG = "RouteCalculator";

    private DistanceCalculator mOldDistanceCalculator;
    private DistanceCalculator mNewDistanceCalculator;
    private float mRoute;

    public RouteCalculator() {
        reset();
    }

    public DistanceCalculator getOldDistanceCalculator() {
        return mOldDistanceCalculator;
    }

    public void setOldDistanceCalculator(DistanceCalculator mOldDistanceCalculator) {
        this.mOldDistanceCalculator = mOldDistanceCalculator;
    }

    public DistanceCalculator getNewDistanceCalculator() {
        return mNewDistanceCalculator;
    }

    public void setNewDistanceCalculator(DistanceCalculator mNewDistanceCalculator) {
        this.mOldDistanceCalculator = this.mNewDistanceCalculator;
        this.mNewDistanceCalculator = mNewDistanceCalculator;
        calculateRoute();
    }

    public float getRoute() {
        return mRoute;
    }

    public void setRoute(float mRoute) {
        this.mRoute = mRoute;
    }

    public void setInitialDistance(DistanceCalculator initialDistanceCalculator) {
        this.mOldDistanceCalculator = initialDistanceCalculator;
        this.mNewDistanceCalculator = initialDistanceCalculator;
        calculateRoute();
    }

    public void reset() {
        mOldDistanceCalculator = null;
        mNewDistanceCalculator = null;
        mRoute = 0;
    }

    public void calculateRoute() {
        if (mOldDistanceCalculator != null && mNewDistanceCalculator != null) {
            mRoute = mOldDistanceCalculator.getDistance() + mNewDistanceCalculator.getDistance();
            Log.d(TAG, "route = " + mRoute + "m");
        }
    }

}
