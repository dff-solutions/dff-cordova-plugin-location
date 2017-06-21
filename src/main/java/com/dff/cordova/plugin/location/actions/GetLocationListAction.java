package com.dff.cordova.plugin.location.actions;

import android.util.Log;

import com.dff.cordova.plugin.location.abstracts.Action;
import com.dff.cordova.plugin.location.resources.LocationResources;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    private CallbackContext mCallbackContext;
    private JSONArray mArguments;

    @Override
    public Action with(CallbackContext callbackContext) {
        this.mCallbackContext = callbackContext;
        return this;
    }

    @Override
    public Action andHasArguments(JSONArray args) {
        this.mArguments = args;
        return this;
    }


    @Override
    public Action execute() {
        Boolean canReset = true;
        try {
            JSONObject params = mArguments.getJSONObject(0);
            if (params != null) {
                canReset = params.optBoolean(LocationResources.RESET, true);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error: ", e);
        }
        switch (LocationResources.LOCATION_RETURN_TYPE) {
            case LocationResources.DFF_STRING:
                ArrayList<String> dffStringLocationList = LocationResources.getLocationListDffString();

                if (dffStringLocationList.size() > 0) {
                    mCallbackContext.success(new JSONArray(dffStringLocationList));
                    Log.d(TAG, "list > 0 ");
                    if (canReset) {
                        LocationResources.clearDffStringLocationsList();
                    }
                } else {
                    mCallbackContext.success(new JSONArray());
                    Log.d(TAG, "list < 0 ");
                }
                break;
            case LocationResources.JSON: {
                ArrayList<JSONObject> jsonLocationList = LocationResources.getLocationListJson();

                if (jsonLocationList.size() > 0) {
                    mCallbackContext.success(new JSONArray(jsonLocationList));
                    Log.d(TAG, "list > 0 ");
                    if (canReset) {
                        LocationResources.clearJsonLocationsList();
                    }
                } else {
                    mCallbackContext.success(new JSONArray());
                    Log.d(TAG, "list < 0 ");
                }
            }
        }
        return this;
    }
}
