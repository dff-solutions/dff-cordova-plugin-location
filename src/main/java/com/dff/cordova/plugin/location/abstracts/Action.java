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

    public Action with(CallbackContext callbackContext) {
        return this;
    }

    public Action andHasArguments(JSONArray args) {
        return this;
    }
}
