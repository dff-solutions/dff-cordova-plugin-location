package com.dff.cordova.plugin.location.actions;

import android.location.Location;
import android.util.Log;

import com.dff.cordova.plugin.location.resources.LocationResources;
import com.dff.cordova.plugin.location.simulators.DistanceSimulator;

import org.apache.cordova.CallbackContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by anahas on 23.06.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 23.06.17
 */

public class ClearStopIDAction extends Action {

    private static final String TAG = ClearStopIDAction.class.getSimpleName();

    private DistanceSimulator mDistanceSimulator;

    CallbackContext callbackContext;

    @Inject
    public ClearStopIDAction(DistanceSimulator mDistanceSimulator) {
        this.mDistanceSimulator = mDistanceSimulator;
    }

    @Override
    public void execute() {
        try {
            JSONObject params = args.getJSONObject(0);
            String requestedStopID = params.optString(LocationResources.JSON_KEY_STOP_ID, LocationResources.STOP_ID);
            if (params.optBoolean(LocationResources.RESET, false)) {
                LocationResources.STOP_ID = "UNKNOWN";
            }
            ArrayList<Location> locationsList = new ArrayList<>(LocationResources.getLocationsMultimap().get(requestedStopID));
            if (locationsList.isEmpty()) {
                callbackContext.error(TAG + " : " + "Error -->  arraylist of stopid isEmpty - size = 0");
                return;
            }
            mDistanceSimulator.performDistanceCalculation(callbackContext, locationsList);
        } catch (JSONException e) {
            callbackContext.error("Error: " + e);
            Log.e(TAG, "Error: ", e);
        }
    }
}
