package com.dff.cordova.plugin.location.tests;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


/**
 * Created by anahas on 18.04.2017.
 */

public class DistanceTester {

    public static final String TAG = DistanceTester.class.getSimpleName();

    private Context mContext;

    public DistanceTester(Context context) {
        mContext = context;
        init();
    }

    private void init() {
        Log.d(TAG, "onInit()");
        readJSON();
    }

    private String loadJsonFromAsset() {
        String json;
        try {
            InputStream is = mContext.getAssets().open("positions.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void readJSON() {
        JSONObject jsonDATA = null;
        try {
            jsonDATA = new JSONObject(loadJsonFromAsset());
        } catch (JSONException e) {
            Log.e(TAG, "Error: ", e);
            e.printStackTrace();
        }
        if (jsonDATA != null) {
            Log.d(TAG, jsonDATA.toString());
        } else {
            Log.e(TAG, "JSON Array NOT FOUND 404");
        }
    }

}
