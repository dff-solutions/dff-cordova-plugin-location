package com.dff.cordova.plugin.location.events;

import com.dff.cordova.plugin.location.resources.Res;

/**
 * Created by anahas on 11.08.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 11.08.17
 */

public class onResRequestEvent {

    public Res res;

    public onResRequestEvent(Res res) {
        this.res = res;
    }
}
