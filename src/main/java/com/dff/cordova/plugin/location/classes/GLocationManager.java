package com.dff.cordova.plugin.location.classes;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.dagger.annotations.Shared;
import com.dff.cordova.plugin.location.events.OnChangedLocation;
import com.dff.cordova.plugin.location.resources.Res;
import com.dff.cordova.plugin.location.resources.Resources;
import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class GLocationManager implements LocationListener {

    private static final String TAG = GLocationManager.class.getSimpleName();

    private Context mContext;
    private Res mRes;
    private EventBus mEventBus;
    private LocationManager mLocationManager;
    private boolean isListening;

    @Inject
    public GLocationManager(
        @ApplicationContext Context mContext,
        @Shared Res mRes,
        EventBus mEventBus
    ) {
        this.mContext = mContext;
        this.mRes = mRes;
        this.mEventBus = mEventBus;
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    }

    public boolean init() {
        try {
            String provider = LocationManager.GPS_PROVIDER;
            if (isProviderAvailable(provider)) {
                mLocationManager.requestLocationUpdates(provider, Resources.LOCATION_MIN_TIME,
                    Resources.LOCATION_MIN_DISTANCE, this);
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
        Log.d(TAG, "on status changed: " + provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "onProviderEnabled with " + provider);

        if (provider.equals("gps")) {
            //send intent with true
            notifyOnChangedProvider(true);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "onProviderDisabled with " + provider);

        if (provider.equals("gps")) {
            //send intent with false
            notifyOnChangedProvider(false);
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

    public boolean isListening() {
        return isListening;
    }

    public void setListening(boolean listening) {
        isListening = listening;
    }
}
