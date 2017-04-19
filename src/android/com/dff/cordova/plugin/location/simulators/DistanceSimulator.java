package com.dff.cordova.plugin.location.simulators;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.util.Log;


import com.dff.cordova.plugin.location.utilities.holders.DistanceTesterHolder;

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
 */

public class DistanceSimulator {

  public static final String TAG = DistanceSimulator.class.getSimpleName();

  private Context mContext;
  private ArrayList<Location> mLocationList;
  private Handler mChangeLocationHandler;
  private DistanceTesterHolder mDistanceTesterHolder;

  int changeLocationDelay = 20000;

  public DistanceSimulator(Context context) {
    mContext = context;
    init();
  }

  private void init() {
    Log.d(TAG, "onInit()");
    mLocationList = new ArrayList<>();
    readJSON();
    runChangeLocationHolder();
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
    JSONParser jsonParser = new JSONParser();
    try {
      jsonDATA = (JSONObject) jsonParser.parse(loadJsonFromAsset());
    } catch (ParseException | ClassCastException e) {
      System.err.println("Error: " + e);
    }
    if (jsonDATA != null) {
      Log.d(TAG, jsonDATA.toString());
      readDffStringLocation(jsonDATA);

    } else {
      Log.e(TAG, "JSON Array NOT FOUND 404");
    }
  }

  private void readDffStringLocation(JSONObject jsonObject) {
    JSONArray dffStringLocationArray = (JSONArray) jsonObject.get("rows");
    Log.d(TAG, dffStringLocationArray.toString());
    for (Object DffStringLocationList : dffStringLocationArray) {
      JSONArray DffStringLocation = (JSONArray) DffStringLocationList;
      for (Object DffString : DffStringLocation) {
        mLocationList.add(parseDffStringLocation(DffString));
      }
    }
    Log.d(TAG, "location list size = " + mLocationList.size());
  }

  private static Location parseDffStringLocation(Object dffStringLocation) {
    String dffString = (String) dffStringLocation;
    String[] dffString_split = dffString.split(Pattern.quote("|"));
    Log.d(TAG, dffString);
    Log.d(TAG, dffString_split[0]);
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

  private void runChangeLocationHolder() {
    mChangeLocationHandler = new Handler();
    mDistanceTesterHolder = new DistanceTesterHolder(mLocationList, mChangeLocationHandler, changeLocationDelay);
    mChangeLocationHandler.postDelayed(mDistanceTesterHolder, changeLocationDelay);
  }
}
