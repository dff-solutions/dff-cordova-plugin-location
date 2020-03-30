package com.dff.cordova.plugin.location.plugin.dagger.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Scope for location plugin.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface LocationPluginScope {
}
