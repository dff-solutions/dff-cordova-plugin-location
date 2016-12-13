package com.dff.cordova.plugin.location.classes;

import android.location.Location;

/**
 * Created by anahas on 12.12.2016.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 12.12.2016
 */
public class DistanceCalculator {

    private static final String TAG = "DistanceCalculator";

    private Location mStartLocation;
    private Location mEndLocation;
    private float mDistance;

    public DistanceCalculator() {
        mStartLocation = null;
        mEndLocation = null;
        mDistance = 0;
    }

    public DistanceCalculator(Location mStartLocation, Location mEndLocation) {
        this.mStartLocation = mStartLocation;
        this.mEndLocation = mEndLocation;
        mDistance = mStartLocation.distanceTo(mEndLocation);
    }

    public Location getEndLocation() {
        return mEndLocation;
    }

    public float getDistance() {
        return mDistance;
    }

    @Override
    public String toString() {
        return "start: " + mStartLocation + " end: " + mEndLocation + " distance = " + mDistance;
    }
}
