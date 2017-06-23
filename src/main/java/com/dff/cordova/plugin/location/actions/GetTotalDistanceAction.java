package com.dff.cordova.plugin.location.actions;

import android.location.Location;
import android.util.Log;

import com.dff.cordova.plugin.location.resources.Res;
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
    public void execute() {
        boolean isClean = false;
        try {
            JSONObject params = args.getJSONObject(0);
            Res.IS_TO_CALCULATE_DISTANCE = !params.optBoolean(Res.RESET, false);
            if (!Res.IS_TO_CALCULATE_DISTANCE) {
                Res.STOP_ID = Res.UNKNOWN;
            }
            isClean = params.optBoolean(Res.CLEAN);
        } catch (JSONException e) {
            Log.e(TAG, "Error: ", e);
        }
        Multimap<String, Location> clonedMultimap = Res.getLocationsMultimap();
        if (clonedMultimap == null) {
            callbackContext.error("Error: --> clonedMultimap may be null");
            return;
        }
        if (isClean) {
            clonedMultimap.removeAll(Res.UNKNOWN);
        }
        ArrayList<Location> locationsList = new ArrayList<>(clonedMultimap.values());
        if (locationsList.size() > 1) {
            mDistanceSimulator.performDistanceCalculation(callbackContext, locationsList);
            if (!Res.IS_TO_CALCULATE_DISTANCE) {
                Res.clearLocationsMultimap();
            }
        } else {
            callbackContext.error("Error: --> locations list size = 0");
        }
    }
}
