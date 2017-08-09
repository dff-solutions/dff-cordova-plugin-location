package com.dff.cordova.plugin.location.actions;

import android.location.Location;
import android.util.Log;

import com.dff.cordova.plugin.common.action.Action;
import com.dff.cordova.plugin.location.dagger.annotations.Shared;
import com.dff.cordova.plugin.location.resources.Res;
import com.dff.cordova.plugin.location.resources.Resources;
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

    private Res mRes;
    private DistanceSimulator mDistanceSimulator;

    @Inject
    public GetTotalDistanceAction(@Shared Res mRes, DistanceSimulator mDistanceSimulator) {
        this.mRes = mRes;
        this.mDistanceSimulator = mDistanceSimulator;
    }

    @Override
    public void execute() {
        boolean isClean = false;
        try {
            JSONObject params = args.getJSONObject(0);
            Resources.IS_TO_CALCULATE_DISTANCE = !params.optBoolean(Resources.RESET, false);
            if (!Resources.IS_TO_CALCULATE_DISTANCE) {
                Resources.STOP_ID = Resources.UNKNOWN;
            }
            isClean = params.optBoolean(Resources.CLEAN);
        } catch (JSONException e) {
            Log.e(TAG, "Error: ", e);
        }
        Multimap<String, Location> clonedMultimap = mRes.getLocationListMultimap();
        if (clonedMultimap == null) {
            callbackContext.error("Error: --> clonedMultimap may be null");
            return;
        }
        if (isClean) {
            // TODO: 07.08.2017 check remove all
            clonedMultimap.removeAll(Resources.UNKNOWN);
        }
        ArrayList<Location> locationsList = new ArrayList<>(clonedMultimap.values());
        if (locationsList.size() > 1) {
            mDistanceSimulator.performDistanceCalculation(callbackContext, locationsList);
            if (!Resources.IS_TO_CALCULATE_DISTANCE) {
                mRes.clearLocationListMultimap();
            }
        } else {
            callbackContext.error("Error: --> locations list size = 0");
        }
    }
}
