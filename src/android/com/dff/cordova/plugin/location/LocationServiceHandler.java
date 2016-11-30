package com.dff.cordova.plugin.location;

import android.content.ContentValues;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.*;
import android.util.Log;

import java.util.List;

/**
 * Created by anahas on 29.11.2016.
 *
 * @author Anthony Nahas
 * @version 0.1
 * @since 29.11.2016
 */
public class LocationServiceHandler extends Handler {

    private static final String TAG = "LocationServiceHandler";
    private LocationManager mLocationManager;
    private Location mLastGoodLocation;
    private Context mContext;
    public static final int WHAT_A = 1;

    LocationServiceHandler(Looper looper, Context context) {
        super(looper);
        mContext = context;
        initializeLocationManager();
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case WHAT_A:
                Message answer = Message.obtain(null, WHAT_A);
                ContentValues contents = new ContentValues();
                contents.put("location", mLastGoodLocation.toString());
                try {
                    msg.replyTo.send(answer);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            default:
                Log.w(TAG, "WHAT???");
                break;
        }
        super.handleMessage(msg);
    }

    private void initializeLocationManager() {
        //get a reference of the system location manager
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "onLocationChanged: " + location);
                if(location != null && location.hasAccuracy()){
                    mLastGoodLocation = location;
                }
                //Toast.makeText(LocationService.this, location.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        listAllProviders();

        try {
            //for production
            //mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0 ,0 , locationListener);
            //for testing
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

    private void listAllProviders() {
        List<String> allProviders = mLocationManager.getAllProviders();
        int count = 0;
        for (String provider : allProviders) {
            Log.d(TAG, "Provider " + count++ + ": " + provider);
        }
    }

    private boolean isProviderAvailable(String provider) {
        return mLocationManager.getAllProviders().contains(provider);
    }

}