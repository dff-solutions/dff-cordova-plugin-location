package com.dff.cordova.plugin.location.classes;

import android.content.Context;
import android.location.Location;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;

/**
 * Class calculate distance appropriate to two location: a start and an end locations.
 *
 * @author Anthony Nahas
 * @version 4
 * @since 12.12.2016
 */
public class DistanceCalculator {

    private static final String TAG = "DistanceCalculator";


    private Location mStartLocation;
    private Location mEndLocation;
    private float mDistance;

    /**
     * Default constructor: properties are initialized with null or respectively 0.
     */
    public DistanceCalculator() {
        reset();
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

    /**
     * First initialize of the object.
     * In this case the start location is equal the end location.
     *
     * @param firstLocation - The first received location
     */
    public void init(Location firstLocation) {
        mStartLocation = firstLocation;
        mEndLocation = firstLocation;
    }

    /**
     * Update the properties on receiving a new location
     *
     * @param newLocation - The new location received
     */
    public void update(Location newLocation) {
        mStartLocation = mEndLocation;
        mEndLocation = newLocation;
        mDistance += mStartLocation.distanceTo(mEndLocation);
    }

    public float getAchievedDistance(Location newLocation) {
        mStartLocation = mEndLocation;
        mEndLocation = newLocation;
        mDistance = mStartLocation.distanceTo(mEndLocation);
        return mDistance;
    }

    /**
     * Reset the properties with null or respectively 0.
     */
    public void reset() {
        mStartLocation = null;
        mEndLocation = null;
        mDistance = 0;
    }

    /**
     * Try to restore the distance property from the shared preference if are available
     *
     * @param context - The context of the application
     * @param type    - The type of the request: 0 for fetching the total distance - 1 for the custom one.
     */
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

    /**
     * String representation for the distance calculator
     *
     * @return - The Representation
     */
    @Override
    public String toString() {
        return "start: " + mStartLocation + " end: " + mEndLocation + " distance = " + mDistance + " m";
    }
}
