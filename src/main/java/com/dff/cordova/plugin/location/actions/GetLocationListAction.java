package com.dff.cordova.plugin.location.actions;

import android.util.Log;

import com.dff.cordova.plugin.common.action.Action;
import com.dff.cordova.plugin.location.classes.GLocation;
import com.dff.cordova.plugin.location.dagger.annotations.Shared;
import com.dff.cordova.plugin.location.resources.Res;
import com.dff.cordova.plugin.location.resources.Resources;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.List;

/**
 * Get the all pending location that are stored in the location list.
 * These location has been stored to be sent back later.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 21.06.2017
 */
public class GetLocationListAction extends Action {

    private static final String TAG = GetLocationListAction.class.getSimpleName();

    private Res mRes;

    @Inject
    public GetLocationListAction(@Shared Res mRes) {
        this.mRes = mRes;
    }


    @Override
    public void execute() {
        Boolean canReset = true;
        try {
            JSONObject params = args.getJSONObject(0);
            if (params != null) {
                canReset = params.optBoolean(Resources.RESET, true);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error: ", e);
        }
        if (mRes.getLocationList().size() > 0) {
            List<GLocation> clonedList = mRes.getLocationList();
            Gson gson = new Gson();
            try {
                if (clonedList != null) {
                    callbackContext.success(new JSONArray(gson.toJson(clonedList)));
                }
            } catch (JSONException e) {
                Log.e(TAG, "Error: ", e);
                callbackContext.error("JSONException + " + e);
            }

            Log.d(TAG, "list > 0 ");
            if (canReset) {
                mRes.clearList();
            }
        } else {
            callbackContext.success(new JSONArray());
            Log.d(TAG, "list < 0 ");
        }
    }
}
