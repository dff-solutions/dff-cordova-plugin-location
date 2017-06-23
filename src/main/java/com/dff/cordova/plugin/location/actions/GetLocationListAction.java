package com.dff.cordova.plugin.location.actions;

import android.util.Log;

import com.dff.cordova.plugin.location.resources.Res;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Get the all pending location that are stored in the location list.
 * These location has been stored to be sent back later.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 21.06.2017
 */
@Singleton
public class GetLocationListAction extends Action {

    private static final String TAG = GetLocationListAction.class.getSimpleName();

    @Inject
    public GetLocationListAction() {
    }


    @Override
    public void execute() {
        Boolean canReset = true;
        try {
            JSONObject params = args.getJSONObject(0);
            if (params != null) {
                canReset = params.optBoolean(Res.RESET, true);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error: ", e);
        }
        switch (Res.LOCATION_RETURN_TYPE) {
            case Res.DFF_STRING:
                ArrayList<String> dffStringLocationList = Res.getLocationListDffString();

                if (dffStringLocationList.size() > 0) {
                    callbackContext.success(new JSONArray(dffStringLocationList));
                    Log.d(TAG, "list > 0 ");
                    if (canReset) {
                        Res.clearDffStringLocationsList();
                    }
                } else {
                    callbackContext.success(new JSONArray());
                    Log.d(TAG, "list < 0 ");
                }
                break;
            case Res.JSON: {
                ArrayList<JSONObject> jsonLocationList = Res.getLocationListJson();

                if (jsonLocationList.size() > 0) {
                    callbackContext.success(new JSONArray(jsonLocationList));
                    Log.d(TAG, "list > 0 ");
                    if (canReset) {
                        Res.clearJsonLocationsList();
                    }
                } else {
                    callbackContext.success(new JSONArray());
                    Log.d(TAG, "list < 0 ");
                }
            }
        }
    }
}
