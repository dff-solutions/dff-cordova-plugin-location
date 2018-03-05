package com.dff.cordova.plugin.location.events;

/**
 * class that forward a result for permissions from the plugin via the event bus
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 05.12.17
 */
public class OnRequestPermissionResult {
    private int mRequestCode;
    private String[] mPermissions;
    private int[] mGrantResults;

    public OnRequestPermissionResult(int mRequestCode, String[] mPermissions, int[] mGrantResults) {
        this.mRequestCode = mRequestCode;
        this.mPermissions = mPermissions;
        this.mGrantResults = mGrantResults;
    }

    public int getRequestCode() {
        return mRequestCode;
    }

    public String[] getPermissions() {
        return mPermissions;
    }

    public int[] getGrantResults() {
        return mGrantResults;
    }
}
