package com.dff.cordova.plugin.location.handlers;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.*;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.dagger.annotations.LocationServiceLooper;
import com.dff.cordova.plugin.location.dagger.annotations.Shared;
import com.dff.cordova.plugin.location.events.OnChangedLocation;
import com.dff.cordova.plugin.location.resources.Res;
import com.dff.cordova.plugin.location.resources.Resources;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;
import com.dff.cordova.plugin.location.utilities.helpers.TimeHelper;
import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Class to handle the communication between the user's request and the location service.
 * The request will be processed and the result will be forward to the location request handler.
 *
 * @author Anthony Nahas
 * @version 9.0.0-rc4
 * @since 29.11.2016
 */
@Singleton
public class LocationServiceHandler extends Handler {

    private static final String TAG = "LocationServiceHandler";

    public static boolean isListening = false;

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private Message mAnswer;

    //    Injection -->
    private Context mContext;
    private Res mRes;
    private PreferencesHelper mPreferencesHelper;
    private TimeHelper mTimeHelper;
    private EventBus mEventBus;


    @Inject
    public LocationServiceHandler
        (@LocationServiceLooper Looper looper,
         @ApplicationContext Context mContext,
         @Shared Res mRes,
         PreferencesHelper mPreferencesHelper,
         TimeHelper mTimeHelper,
         EventBus mEventBus
        ) {

        super(looper);
        this.mContext = mContext;
        this.mPreferencesHelper = mPreferencesHelper;
        this.mTimeHelper = mTimeHelper;
        this.mEventBus = mEventBus;
        this.mRes = mRes;
    }

    /**
     * Handle the received message.
     *
     * @param msg - The message sent by the location service handler.
     */
    @Override
    public void handleMessage(Message msg) {
        Bundle result = new Bundle();
        Resources.WHAT msg_what = Resources.WHAT.values()[msg.what];

        switch (msg_what) {
            case START_LOCATION_SERVICE:
                mAnswer = Message.obtain(null, msg.what);
                isListening = mPreferencesHelper.isServiceStarted() ||
                    initializeLocationManager(msg.getData().getLong(Resources.LOCATION_MIN_TIME_KEY),
                        msg.getData().getFloat(Resources.LOCATION_MIN_DISTANCE_KEY));
                result.putBoolean(Resources.IS_LOCATION_MANAGER_LISTENING, isListening);
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
                if (mRes.getLocation() != null) {
                    if (!(mTimeHelper.getTimeAge(mRes.getLocation().getTime()) <= Resources.LOCATION_MAX_AGE)) {
                        mRes.clearLocation();
                        Log.d(TAG, "setLocation --> null");
                    }
                }
                try {
                    msg.replyTo.send(mAnswer);
                } catch (RemoteException e) {
                    Log.e(TAG, "Error: ", e);
                }
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
                if (location.hasAccuracy() && location.getAccuracy() <= Resources.LOCATION_MIN_ACCURACY) {
                    //mLastGoodLocation = location;
                    Log.d(TAG, "accuracy = " + location.getAccuracy());
                    location.setTime(System.currentTimeMillis());
                    mRes.setLocation(location);
                    notifyOnChangedLocation();
                    mEventBus.post(new OnChangedLocation(mRes));
                    Log.d(TAG, "setLocation --> " + location);
                }
            }

            @Override
            public void onStatusChanged(String provider, int i, Bundle bundle) {
                //ignore
                Log.d(TAG, "on status changed: " + provider);
            }

            @Override
            public void onProviderEnabled(String provider) {
                //ignore
                Log.d(TAG, "onProviderEnabled with " + provider);

                if (provider.equals("gps")) {
                    //send intent with true
                    notifyOnChangedProvider(true);
                }
            }

            @Override
            public void onProviderDisabled(String provider) {
                //ignore
                Log.d(TAG, "onProviderDisabled with " + provider);

                if (provider.equals("gps")) {
                    //send intent with false
                    notifyOnChangedProvider(false);
                }
            }
        };

        try {
            String provider = LocationManager.GPS_PROVIDER;
            if (isProviderAvailable(provider)) {
                mLocationManager.requestLocationUpdates(provider, minTime, minDistance, mLocationListener);
                Log.d(TAG, "Location Manager is listening...");
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
     * Notify the new location receiver about the new location ;)
     */
    private void notifyOnChangedLocation() {
        LocalBroadcastManager
            .getInstance(mContext)
            .sendBroadcast(new Intent().setAction(Resources.BROADCAST_ACTION_ON_NEW_LOCATION));
    }

    private void notifyOnChangedProvider(boolean isGpsProviderEnabled) {
        Intent intent = new Intent(Resources.BROADCAST_ACTION_ON_CHANGED_PROVIDER);
        intent.putExtra(Resources.IS_PROVIDER_ENABLED, isGpsProviderEnabled);

        LocalBroadcastManager
            .getInstance(mContext)
            .sendBroadcast(intent);
    }
}
