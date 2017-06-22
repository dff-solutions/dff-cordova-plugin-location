package com.dff.cordova.plugin.location.actions;

import android.location.Location;
import android.util.Log;

import com.dff.cordova.plugin.location.abstracts.Action;
import com.dff.cordova.plugin.location.resources.LocationResources;
import com.dff.cordova.plugin.location.simulators.DistanceSimulator;
import com.google.common.collect.Multimap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by anahas on 22.06.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 22.06.17
 */
@Singleton
public class GetTotalDistanceAction extends Action {

    private static final String TAG = GetTotalDistanceAction.class.getSimpleName();

    private DistanceSimulator mDistanceSimulator;

    @Inject
    public GetTotalDistanceAction(DistanceSimulator mDistanceSimulator) {
        this.mDistanceSimulator = mDistanceSimulator;
    }

    @Override
    public Action execute() {
        boolean isClean = false;
        try {
            JSONObject params = super.getArguments().getJSONObject(0);
            LocationResources.IS_TO_CALCULATE_DISTANCE = !params.optBoolean(LocationResources.RESET, false);
            if (!LocationResources.IS_TO_CALCULATE_DISTANCE) {
                LocationResources.STOP_ID = LocationResources.UNKNOWN;
            }
            isClean = params.optBoolean(LocationResources.CLEAN);
        } catch (JSONException e) {
            Log.e(TAG, "Error: ", e);
        }
        Multimap<String, Location> clonedMultimap = LocationResources.getLocationsMultimap();
        if (clonedMultimap == null) {
            super.getCallbackContext().error("Error: --> clonedMultimap may be null");
            return this;
        }
        if (isClean) {
            clonedMultimap.removeAll(LocationResources.UNKNOWN);
        }
        ArrayList<Location> locationsList = new ArrayList<>(clonedMultimap.values());
        if (locationsList.size() > 1) {
            mDistanceSimulator.performDistanceCalculation(super.getCallbackContext(), locationsList);
            if (!LocationResources.IS_TO_CALCULATE_DISTANCE) {
                LocationResources.clearLocationsMultimap();
            }
        } else {
            super.getCallbackContext().error("Error: --> locations list size = 0");
        }
        return this;
    }
}
