package com.dff.cordova.plugin.location.dagger.annotations.broadcasts;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by anahas on 26.06.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 26.06.17
 */
@Qualifier
@Retention(RUNTIME)
public @interface BroadcastChangeProviderReceiver {
}
