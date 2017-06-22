package com.dff.cordova.plugin.location.abstracts;

import com.dff.cordova.plugin.location.interfaces.Executable;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;

/**
 * Created by anahas on 19.06.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 19.06.17
 */

public abstract class Action implements Executable {
    private CallbackContext mCallbackContext;
    private JSONArray mArguments;

    public CallbackContext getCallbackContext() {
        return mCallbackContext;
    }

    public JSONArray getArguments() {
        return mArguments;
    }

    public Action with(CallbackContext callbackContext) {
        this.mCallbackContext = callbackContext;
        return this;
    }

    public Action andHasArguments(JSONArray args) {
        this.mArguments = args;
        return this;
    }
}
