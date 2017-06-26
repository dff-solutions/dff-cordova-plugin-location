package com.dff.cordova.plugin.location.actions.index;

import com.dff.cordova.plugin.location.actions.ClearStopIDAction;
import com.dff.cordova.plugin.location.actions.EnableMappingAction;
import com.dff.cordova.plugin.location.actions.GetKeySetFromLocationsMultimapAction;
import com.dff.cordova.plugin.location.actions.GetLocationAction;
import com.dff.cordova.plugin.location.actions.GetLocationListAction;
import com.dff.cordova.plugin.location.actions.GetTotalDistanceAction;
import com.dff.cordova.plugin.location.actions.RegisterProviderListenerAction;
import com.dff.cordova.plugin.location.actions.RestoreAction;
import com.dff.cordova.plugin.location.actions.SetStopIdAction;
import com.dff.cordova.plugin.location.actions.StartLocationServiceAction;
import com.dff.cordova.plugin.location.actions.StopLocationServiceAction;
import com.dff.cordova.plugin.location.actions.UnregisterProviderListenerAction;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by anahas on 19.06.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 19.06.17
 */
@Singleton
public class IndexActions {

    // 8 Actions
    public RestoreAction mRestoreAction;
    public StartLocationServiceAction mStartLocationServiceAction;
    public StopLocationServiceAction mStopLocationServiceAction;
    public GetLocationAction mGetLocationAction;
    public GetLocationListAction mGetLocationListAction;
    public GetTotalDistanceAction mGetTotalDistanceAction;
    public SetStopIdAction mSetStopIdAction;
    public ClearStopIDAction mClearStopIDAction;
    public EnableMappingAction mEnableMappingAction;
    public GetKeySetFromLocationsMultimapAction mGetKeySetFromLocationsMultimapAction;
    public RegisterProviderListenerAction mRegisterProviderListenerAction;
    public UnregisterProviderListenerAction mUnregisterProviderListenerAction;

    @Inject
    public IndexActions(
        RestoreAction mRestoreAction,
        StartLocationServiceAction mStartLocationServiceAction,
        StopLocationServiceAction mStopLocationServiceAction,
        GetLocationAction mGetLocationAction,
        GetLocationListAction mGetLocationListAction,
        GetTotalDistanceAction mGetTotalDistanceAction,
        SetStopIdAction mSetStopIdAction,
        ClearStopIDAction mClearStopIDAction,
        EnableMappingAction mEnableMappingAction,
        GetKeySetFromLocationsMultimapAction mGetKeySetFromLocationsMultimapAction,
        RegisterProviderListenerAction mRegisterProviderListenerAction,
        UnregisterProviderListenerAction mUnregisterProviderListenerAction
    ) {
        this.mRestoreAction = mRestoreAction;
        this.mStartLocationServiceAction = mStartLocationServiceAction;
        this.mStopLocationServiceAction = mStopLocationServiceAction;
        this.mGetLocationAction = mGetLocationAction;
        this.mGetLocationListAction = mGetLocationListAction;
        this.mGetTotalDistanceAction = mGetTotalDistanceAction;
        this.mSetStopIdAction = mSetStopIdAction;
        this.mClearStopIDAction = mClearStopIDAction;
        this.mEnableMappingAction = mEnableMappingAction;
        this.mGetKeySetFromLocationsMultimapAction = mGetKeySetFromLocationsMultimapAction;
        this.mRegisterProviderListenerAction = mRegisterProviderListenerAction;
        this.mUnregisterProviderListenerAction = mUnregisterProviderListenerAction;
    }
}
