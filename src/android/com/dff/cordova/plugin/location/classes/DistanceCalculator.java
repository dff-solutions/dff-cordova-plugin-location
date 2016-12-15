package com.dff.cordova.plugin.location.classes;

import android.content.Context;
import android.location.Location;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;

/**
 * Created by anahas on 12.12.2016.
 *
 * @author Anthony Nahas
 * @version 3.0
 * @since 12.12.2016
 */
public class DistanceCalculator {

    private static final String TAG = "DistanceCalculator";

    private Location mStartLocation;
    private Location mEndLocation;
    private float mDistance;

    public DistanceCalculator() {
        reset();
    }

    public DistanceCalculator(Location mStartLocation, Location mEndLocation) {
        this.mStartLocation = mStartLocation;
        this.mEndLocation = mEndLocation;
        mDistance = mStartLocation.distanceTo(mEndLocation);
    }

    public Location getStartLocation() {
        return mStartLocation;
    }

    public Location getEndLocation() {
        return mEndLocation;
    }

    public float getDistance() {
        return mDistance;
    }

    public void setDistance(float mDistance) {
        this.mDistance = mDistance;
    }

    public void init(Location firstLocation) {
        mStartLocation = firstLocation;
        mEndLocation = firstLocation;
    }

    public void update(Location newLocation) {
        mStartLocation = mEndLocation;
        mEndLocation = newLocation;
        mDistance += mStartLocation.distanceTo(mEndLocation);
    }

    public void reset() {
        mStartLocation = null;
        mEndLocation = null;
        mDistance = 0;
    }

    public void restore(Context context, int type) {
        PreferencesHelper preferencesHelper = new PreferencesHelper(context);

        switch (type) {
            case 0:
                mDistance = preferencesHelper.getStoredTotalDistance();
                break;
            case 1:
                mDistance = preferencesHelper.getStoreCustomDistance();
                break;
        }
    }

    @Override
    public String toString() {
        return "start: " + mStartLocation + " end: " + mEndLocation + " distance = " + mDistance + " m";
    }
}
