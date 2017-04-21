package com.dff.cordova.plugin.location.handlers;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.*;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.dff.cordova.plugin.common.log.CordovaPluginLog;
import com.dff.cordova.plugin.location.resources.LocationResources;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;
import com.dff.cordova.plugin.location.utilities.helpers.TimeHelper;
import com.dff.cordova.plugin.location.utilities.holders.DistanceCalculatorCustomHolder;
import com.dff.cordova.plugin.location.utilities.holders.DistanceCalculatorFullHolder;
import com.dff.cordova.plugin.location.utilities.holders.LocationsHolder;

import java.util.List;

/**
 * Class to handle the communication between the user's request and the location service.
 * The request will be processed and the result will be forward to the location request handler.
 *
 * @author Anthony Nahas
 * @version 6.0.0
 * @since 29.11.2016
 */
public class LocationServiceHandler extends Handler {

    private static final String TAG = "LocationServiceHandler";

    public static boolean isListening = false;

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private Message mAnswer;
    private Context mContext;
    private Handler mLocationsListHandler;
    private Handler mTotalDistanceCalculatorHandler;
    private Handler mCustomDistanceCalculatorHandler;
    private DistanceCalculatorFullHolder mDistanceCalculatorFullHolder;
    private DistanceCalculatorCustomHolder mDistanceCalculatorCustomHolder;
    private PreferencesHelper mPreferencesHelper;

    /**
     * Custom constructor.
     *
     * @param looper  - The used looper.
     * @param context - The application/service context.
     */
    public LocationServiceHandler(Looper looper, Context context) {
        super(looper);
        mContext = context;
        mPreferencesHelper = new PreferencesHelper(mContext);
    }

    /**
     * Handle the received message.
     *
     * @param msg - The message sent by the location service handler.
     */
    @Override
    public void handleMessage(Message msg) {
        Bundle result = new Bundle();
        LocationResources.WHAT msg_what = LocationResources.WHAT.values()[msg.what];

        switch (msg_what) {
            case START_LOCATION_SERVICE:
                mAnswer = Message.obtain(null, msg.what);
                isListening = mPreferencesHelper.isServiceStarted() ||
                    initializeLocationManager(msg.getData().getLong(LocationResources.LOCATION_MIN_TIME_KEY),
                        msg.getData().getFloat(LocationResources.LOCATION_MIN_DISTANCE_KEY));
                result.putBoolean(LocationResources.IS_LOCATION_MANAGER_LISTENING, isListening);
                mPreferencesHelper.setIsServiceStarted(isListening);
                mAnswer.setData(result);
                try {
                    if (msg.replyTo != null) {
                        msg.replyTo.send(mAnswer);
                    }
                } catch (RemoteException | NullPointerException e) {
                    Log.e(TAG, "Error: ", e);
                }
                break;
            case STOP_LOCATION_SERVICE:
                stopLocationHolder();
                if (mLocationListener != null) {
                    mLocationManager.removeUpdates(mLocationListener);
                }
                mAnswer = Message.obtain(null, msg.what);
                try {
                    msg.replyTo.send(mAnswer);
                } catch (RemoteException e) {
                    Log.e(TAG, "Error: ", e);
                }
                break;
            case GET_LOCATION:
                mAnswer = Message.obtain(null, msg.what);
                Bundle params = msg.getData();
                int returnType = params.getInt(LocationResources.LOCATION_RETURN_TYPE_KEY);
                Log.d(TAG, "return type = " + returnType);
                if (LocationResources.getLastGoodLocation() != null) {
                    if (TimeHelper.getTimeAge(LocationResources.getLastGoodLocation().getTime()) <= LocationResources.LOCATION_MAX_AGE) {
                        switch (returnType) {
                            case 0:
                                Log.d(TAG, "lastGoodLocation as string = " + LocationResources.getLastGoodLocationAsString());
                                result.putInt(LocationResources.LOCATION_RETURN_TYPE_KEY, 0);
                                break;
                            case 1:
                                Log.d(TAG, "lastGoodLocation as JSON = " + LocationResources.getLastGoodLocationAsJson());
                                result.putInt(LocationResources.LOCATION_RETURN_TYPE_KEY, 1);
                                break;
                        }
                        mAnswer.setData(result);
                    } else {
                        LocationResources.setLastGoodLocation(null);
                        Log.d(TAG, "setLastGoodLocation --> null");
                    }
                }
                try {
                    msg.replyTo.send(mAnswer);
                } catch (RemoteException e) {
                    Log.e(TAG, "Error: ", e);
                }
                break;
            case RUN_TOTAL_DISTANCE_CALCULATOR:
                runDistanceCalculatorFullHolder();
                Log.d(TAG, "run distance calc full holder");
                break;
            case RUN_CUSTOM_DISTANCE_CALCULATOR:
                runDistanceCalculatorCustomHolder();
                Log.d(TAG, "run distance calc custom holder");
                break;
            case GET_TOTAL_DISTANCE_CALCULATOR:
                replyToRequestHandler(msg);
                stopDistanceCalculatorFullHolder();
                break;
            case GET_CUSTOM_DISTANCE_CALCULATOR:
                replyToRequestHandler(msg);
                stopDistanceCalculatorCustomHolder();
                break;
            default:
                Log.w(TAG, "No what of a msg found!");
                break;
        }
        super.handleMessage(msg);
    }

    /**
     * Initialize the location manager and set the location listener.
     */
    private boolean initializeLocationManager(long minTime, float minDistance) {
        //get a reference of the system location manager
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            /**
             * Get a new location when it changes.
             *
             * @param location - The new location.
             */
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "onLocationChanged: " + location);
                Log.d(TAG, "hasAccuracy = " + location.hasAccuracy());
                if (location.hasAccuracy() && location.getAccuracy() <= LocationResources.LOCATION_MIN_ACCURACY) {
                    //mLastGoodLocation = location;
                    Log.d(TAG, "accuracy = " + location.getAccuracy());
                    location.setTime(System.currentTimeMillis());
                    LocationResources.setLastGoodLocation(location);
                    notifyOnChangedLocation();
                    Log.d(TAG, "setLastGoodLocation --> " + location);
                    if (LocationResources.IS_TO_CALCULATE_DISTANCE) {
                        LocationResources.addLocationToMultimap(location);
                        LocationResources.logLocationsMultimap();
                    }
                }
                //Toast.makeText(LocationService.this, location.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                //ignore
                Log.d(TAG, "on status changed: " + s);
            }

            @Override
            public void onProviderEnabled(String s) {
                //ignore
                Log.d(TAG, "onProviderEnabled with " + s);
            }

            @Override
            public void onProviderDisabled(String s) {
                //ignore
                Log.d(TAG, "onProviderDisabled with " + s);
            }
        };

        try {
            String provider = LocationManager.GPS_PROVIDER;
            if (isProviderAvailable(provider)) {
                mLocationManager.requestLocationUpdates(provider, minTime, minDistance, mLocationListener);
                Log.d(TAG, "Location Manager is listening...");
                runLocationsHolder();
                return true;
            } else {
                Log.e(TAG, "Location Manager: provider unavailable");
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while requesting location updates", e);
            listAllProviders();
            return false;
        }
    }

    /**
     * List all providers that the device supports.
     */
    private void listAllProviders() {
        List<String> allProviders = mLocationManager.getAllProviders();
        int count = 0;
        for (String provider : allProviders) {
            Log.d(TAG, "Provider " + count++ + ": " + provider);
        }
    }

    /**
     * Check if a specific provided is supported by the device.
     *
     * @param provider - The provider to check.
     * @return - Whether the provider is supported.
     */
    private boolean isProviderAvailable(String provider) {
        return mLocationManager.getAllProviders().contains(provider);
    }

    /**
     * Run the location list handler in order to hold the last good location every interval of time.
     */
    private void runLocationsHolder() {
        Log.d(TAG, "runLocationsHolder");
        mLocationsListHandler = new Handler();
        mLocationsListHandler.postDelayed(new LocationsHolder(mLocationsListHandler), LocationResources.LOCATION_DELAY);
    }

    private void stopLocationHolder() {
        Log.d(TAG, "stop location holder");
        try {
            if (mLocationsListHandler != null) {
                mLocationsListHandler.removeCallbacksAndMessages(null);
            }
        } catch (NullPointerException e) {
            CordovaPluginLog.e(TAG, "Error: ", e);
        }
    }

    /**
     * Run the total distance calculator.
     */
    private void runDistanceCalculatorFullHolder() {
        Log.d(TAG, "run DistanceCalc Full Holder");
        LocationResources.IS_TO_CALCULATE_DISTANCE = true;
        mTotalDistanceCalculatorHandler = new Handler();
        mDistanceCalculatorFullHolder = new DistanceCalculatorFullHolder(mPreferencesHelper, mTotalDistanceCalculatorHandler, LocationResources.DISTANCE_CALCULATOR_FULL_DELAY);
        mTotalDistanceCalculatorHandler.postDelayed(mDistanceCalculatorFullHolder, LocationResources.DISTANCE_CALCULATOR_FULL_DELAY);
    }

    /**
     * Stop the total distance calculator.
     */
    private void stopDistanceCalculatorFullHolder() {
        Log.d(TAG, "stop distance calc full holder");
        try {
            if (mTotalDistanceCalculatorHandler != null) {
                //mTotalDistanceCalculatorHandler.removeCallbacks(mDistanceCalculatorFullHolder);
                mTotalDistanceCalculatorHandler.removeCallbacksAndMessages(null);
            }
        } catch (NullPointerException e) {
            CordovaPluginLog.e(TAG, "Error: ", e);
        } finally {
            mPreferencesHelper.storeTotalDistance(0);
            LocationResources.IS_TO_CALCULATE_DISTANCE = false;
            LocationResources.STOP_ID = "UNKNOWN";
        }
    }

    /**
     * Run the custom distance calculator.
     */
    private void runDistanceCalculatorCustomHolder() {
        Log.d(TAG, "run distance calc custom holder");
        mCustomDistanceCalculatorHandler = new Handler();
        mDistanceCalculatorCustomHolder = new DistanceCalculatorCustomHolder(mPreferencesHelper,
            mCustomDistanceCalculatorHandler, LocationResources.DISTANCE_CALCULATOR_CUSTOM_DELAY);
        mCustomDistanceCalculatorHandler.postDelayed(mDistanceCalculatorCustomHolder,
            LocationResources.DISTANCE_CALCULATOR_CUSTOM_DELAY);
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
        } finally {
            mPreferencesHelper.storeCustomDistance(0);
        }
    }

    /**
     * Forward/reply to the request handle in order to close the action requested.
     *
     * @param msg - The message to forward.
     */
    private static void replyToRequestHandler(Message msg) {
        Log.d(TAG, "get: reply to request handler");
        Message answer = Message.obtain(null, msg.what);
        try {
            msg.replyTo.send(answer);
        } catch (RemoteException | NullPointerException e) {
            CordovaPluginLog.e(TAG, "Error: ", e);
        }
    }

    /**
     * Notify the new location receiver about the new location ;)
     */
    private void notifyOnChangedLocation() {
        LocalBroadcastManager
            .getInstance(mContext)
            .sendBroadcast(new Intent().setAction(LocationResources.BROADCAST_ACTION_ON_NEW_LOCATION));
    }

    /**
     * On uncaught exception - the process will be respawn
     *
     * @return whether the location manager has been initialized or not
     */
    public boolean initializeLocationManagerOnRespawn() {
        return initializeLocationManager(mPreferencesHelper.getMinTime(), mPreferencesHelper.getMinDistance());
    }
}
