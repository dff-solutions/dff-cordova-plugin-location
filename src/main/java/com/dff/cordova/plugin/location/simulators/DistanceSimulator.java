package com.dff.cordova.plugin.location.simulators;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.util.Log;


import com.dff.cordova.plugin.location.classes.DistanceCalculator;
import com.dff.cordova.plugin.location.utilities.holders.DistanceSimulatorHolder;

import org.apache.cordova.CallbackContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;


/**
 * Created by anahas on 18.04.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 18.04.2017
 */

public class DistanceSimulator {

    public static final String TAG = DistanceSimulator.class.getSimpleName();

    private Context mContext;
    private int mStopsCount;

    public DistanceSimulator() {
    }

    public DistanceSimulator(Context context) {
        mContext = context;
    }

    public void performDistanceCalculation(CallbackContext callbackContext, ArrayList<Location> locationsList) {

        int counter = 0;
        Location location;
        DistanceCalculator distanceCalculator = new DistanceCalculator();

        while (counter != locationsList.size() - 1) {
            location = locationsList.get(counter);
            if (distanceCalculator.getStartLocation() != null && distanceCalculator.getEndLocation() != null) {
                distanceCalculator.update(location);
            } else {
                distanceCalculator.init(location);
            }
            counter++;
        }
        float result = distanceCalculator.getDistance();
        Log.d(TAG, "Calculation result = " + result);
        callbackContext.success(String.valueOf(result));
    }

    private void simulateStaticJSON() {
        Log.d(TAG, "onInit()");
        mStopsCount = 0;
        ArrayList<Location> locationsList = readJSON();
        runChangeLocationHolder(locationsList, getStopsCount(locationsList != null ? locationsList.size() : 0));
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


    private ArrayList<Location> readJSON() {
        JSONObject jsonDATA = null;
        JSONParser jsonParser = new JSONParser();
        try {
            jsonDATA = (JSONObject) jsonParser.parse(loadJsonFromAsset());
        } catch (ParseException | ClassCastException e) {
            System.err.println("Error: " + e);
        }
        if (jsonDATA != null) {
            Log.d(TAG, jsonDATA.toString());
            return readDffStringLocation(jsonDATA);

        } else {
            Log.e(TAG, "JSON Array NOT FOUND 404");
            return null;
        }
    }

    private ArrayList<Location> readDffStringLocation(JSONObject jsonObject) {
        ArrayList<Location> locationsList = new ArrayList<>();
        JSONArray dffStringLocationArray = (JSONArray) jsonObject.get("rows");
        Log.d(TAG, dffStringLocationArray.toString());
        for (Object DffStringLocationList : dffStringLocationArray) {
            JSONArray DffStringLocation = (JSONArray) DffStringLocationList;
            for (Object DffString : DffStringLocation) {
                locationsList.add(parseDffStringLocation(DffString));
            }
        }
        mStopsCount = getStopsCount(locationsList.size());
        Log.d(TAG, "location list size = " + locationsList.size() + " with " + mStopsCount + " stops!");
        return locationsList;
    }

    private static Location parseDffStringLocation(Object dffStringLocation) {
        String dffString = (String) dffStringLocation;
        String[] dffString_split = dffString.split(Pattern.quote("|"));
        Log.d(TAG, dffString);
        double latitude = (Double.valueOf(dffString_split[0])
            + (Double.valueOf(dffString_split[1]) / 60)
            + (Double.valueOf(dffString_split[2]) / 36000));
        double longitude = (Double.valueOf(dffString_split[3])
            + (Double.valueOf(dffString_split[4]) / 60)
            + (Double.valueOf(dffString_split[5]) / 36000));
        Location location = new Location("GPS");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

    private static int getStopsCount(int size) {
        double result = size / 10;
        result = result % 10 == 0 ? result : result + 1;
        return (int) result;
    }

    private void runChangeLocationHolder(ArrayList<Location> locationsList, int stopsCount) {
        Handler mChangeLocationHandler = new Handler();
        int changeLocationDelay = 20;
        DistanceSimulatorHolder mDistanceTesterHolder = new DistanceSimulatorHolder(mContext, locationsList, stopsCount, mChangeLocationHandler, changeLocationDelay);
        mChangeLocationHandler.postDelayed(mDistanceTesterHolder, changeLocationDelay);
    }
}
