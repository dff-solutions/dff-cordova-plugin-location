package com.dff.cordova.plugin.location.handlers;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.*;
import android.util.Log;
import com.dff.cordova.plugin.location.resources.LocationResources;
import com.dff.cordova.plugin.location.utilities.holders.LocationsHolder;
import com.dff.cordova.plugin.location.utilities.helpers.TimeHelper;

import java.util.List;

/**
 * Class to handle the communication between the user's request and the location service.
 * The request will be processed and the result will be forward to the location request handler.
 *
 * @author Anthony Nahas
 * @version 2.0.0
 * @since 29.11.2016
 */
public class LocationServiceHandler extends Handler {

    private static final String TAG = "LocationServiceHandler";
    private LocationManager mLocationManager;
    //private Location mLastGoodLocation;
    private Context mContext;
    private Handler mLocationsListHandler;
    private Handler mDistanceCalculatorFullListHandler;
    private Handler mDistanceCalculatorCustomListHandler;

    /**
     * Custom constructor.
     *
     * @param looper  - The used looper.
     * @param context - The application/service context.
     */
    public LocationServiceHandler(Looper looper, Context context) {
        super(looper);
        mContext = context;
        initializeLocationManager();
        runLocationsHolder();
    }

    /**
     * Handle the received message.
     *
     * @param msg - The message sent by the location service handler.
     */
    @Override
    public void handleMessage(Message msg) {
        Bundle result = new Bundle();
        switch (msg.what) {
            case LocationResources.WHAT_GET_LOCATION:
                Message answer = Message.obtain(null, msg.what);
                Bundle params = msg.getData();
                int returnType = params.getInt(LocationResources.LOCATION_RETURN_TYPE_KEY);
                Log.d(TAG, "return type = " + returnType);
                if (LocationResources.getLastGoodLocation() != null) {
                    if (TimeHelper.getTimeAge(LocationResources.getLastGoodLocation().getTime()) <= LocationResources.LOCATION_MAX_AGE) {
                        switch (returnType) {
                            case 0:
                                Log.d(TAG, "lastGoodLocation as string = " + LocationResources.getLastGoodLocationAsString());
                                //result.putString(LocationResources.DATA_LOCATION_KEY, LocationResources.getLastGoodLocationAsString());
                                result.putInt(LocationResources.LOCATION_RETURN_TYPE_KEY, 0);
                                break;
                            case 1:
                                Log.d(TAG, "lastGoodLocation as JSON = " + LocationResources.getLastGoodLocationAsJson());
                                //result.p(LocationResources.DATA_LOCATION_KEY, LocationResources.getLastGoodLocationAsJson());
                                result.putInt(LocationResources.LOCATION_RETURN_TYPE_KEY, 1);
                                break;
                        }

                        answer.setData(result);
                    } else {
                        LocationResources.setLastGoodLocation(null);
                        Log.d(TAG, "setLastGoodLocation --> null");
                    }
                }
                try {
                    msg.replyTo.send(answer);
                } catch (RemoteException e) {
                    e.printStackTrace();
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
    private void initializeLocationManager() {
        //get a reference of the system location manager
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
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
                    Log.d(TAG, "setLastGoodLocation --> " + location);
                }
                //Toast.makeText(LocationService.this, location.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                //ignore
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

        listAllProviders();

        try {
            String provider = LocationManager.GPS_PROVIDER;
            if (isProviderAvailable(provider)) {
                mLocationManager.requestLocationUpdates(provider, 0, 0, locationListener);
                Log.d(TAG, "Location Manager is listening...");
            } else {
                Log.e(TAG, "Location Manager: provider unavailable");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error with providers", e);
            listAllProviders();
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
        mLocationsListHandler = new Handler();
        mLocationsListHandler.postDelayed(new LocationsHolder(mLocationsListHandler), LocationResources.LOCATION_DELAY);
    }

    private void runDistanceCalculatorHolder(){
        //todo
    }

}