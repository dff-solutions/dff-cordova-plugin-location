package com.dff.cordova.plugin.location.utilities.holders;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.util.Log;
import com.dff.cordova.plugin.common.log.CordovaPluginLog;
import com.dff.cordova.plugin.location.resources.LocationResources;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;

import java.util.ArrayList;

/**
 * Created by anahas on 18.04.2017.
 *
 * @author Anthony Nahas
 * @version 1.1
 * @since 18.04.2017
 */

public class DistanceSimulatorHolder implements Runnable {

    private static final String TAG = DistanceSimulatorHolder.class.getSimpleName();

    private ArrayList<Location> mLocationList;
    private Handler mHandler;
    private int mDelay;
    private int mCounter = 0;

    private Handler mTotalDistanceCalculatorHandler;
    private Handler mCustomDistanceCalculatorHandler;
    private DistanceCalculatorFullHolder mDistanceCalculatorFullHolder;
    private DistanceCalculatorCustomHolder mDistanceCalculatorCustomHolder;
    private PreferencesHelper mPreferencesHelper;

    private int customDelay = 1500;
    private int fullDelay = 1500;

    public DistanceSimulatorHolder(Context context, ArrayList<Location> mLocationList, Handler mHandler, int mDelay) {
        mPreferencesHelper = new PreferencesHelper(context);
        this.mLocationList = mLocationList;
        this.mHandler = mHandler;
        this.mDelay = mDelay;
    }

    @Override
    public void run() {

        if (LocationResources.getLastGoodLocation() == null) {
            LocationResources.setLastGoodLocation(mLocationList.get(0));
            Log.d(TAG, "starting with the first mock location");
        } else {
            LocationResources.setLastGoodLocation(mLocationList.get(mCounter));
            Log.d(TAG, "Mock Location " + mCounter);
        }

        mCounter++;

        if (mCounter == mLocationList.size() - 1) {
            mHandler.removeCallbacks(this);
            stopDistanceCalculatorFullHolder();
        } else {
            mHandler.postDelayed(this, mDelay);
        }
    }

    /**
     * Run the total distance calculator.
     */
    private void runDistanceCalculatorFullHolder() {
        Log.d(TAG, "run DistanceCalc Full Holder");
        mTotalDistanceCalculatorHandler = new Handler();
        mDistanceCalculatorFullHolder = new DistanceCalculatorFullHolder(mPreferencesHelper,
            mTotalDistanceCalculatorHandler, fullDelay);
        mTotalDistanceCalculatorHandler.postDelayed(mDistanceCalculatorFullHolder, fullDelay);
    }

    /**
     * Stop the total distance calculator.
     */
    private void stopDistanceCalculatorFullHolder() {
        Log.d(TAG, "stop distance calc full holder");
        try {
            if (mTotalDistanceCalculatorHandler != null) {
                mTotalDistanceCalculatorHandler.removeCallbacksAndMessages(null);
            }
        } catch (NullPointerException e) {
            CordovaPluginLog.e(TAG, "Error: ", e);
        }
    }

    /**
     * Run the custom distance calculator.
     */
    private void runDistanceCalculatorCustomHolder() {
        Log.d(TAG, "run distance calc custom holder");
        mCustomDistanceCalculatorHandler = new Handler();
        mDistanceCalculatorCustomHolder = new DistanceCalculatorCustomHolder(mPreferencesHelper,
            mCustomDistanceCalculatorHandler, customDelay);
        mCustomDistanceCalculatorHandler.postDelayed(mDistanceCalculatorCustomHolder, customDelay);
    }

    /**
     * Stop the custom distance calculator.
     */
    private void stopDistanceCalculatorCustomHolder() {
        Log.d(TAG, "stop distance calc custom holder");
        try {
            if (mCustomDistanceCalculatorHandler != null) {
                //mCustomDistanceCalculatorHandler.removeCallbacks(mDistanceCalculatorCustomHolder);
                mCustomDistanceCalculatorHandler.removeCallbacksAndMessages(null);
            }
        } catch (NullPointerException e) {
            CordovaPluginLog.e(TAG, "Error: ", e);
        }
    }

    private void logDistance() {
        float totalDistance = LocationResources.TOTAL_DISTANCE_CALCULATOR.getDistance();
        Log.d(TAG, "Total: " + LocationResources.TOTAL_DISTANCE_CALCULATOR.getDistance() + "m");
    }
}
