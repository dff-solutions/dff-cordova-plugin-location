package com.dff.cordova.plugin.location.utilities.helpers;

import android.content.Context;
import android.util.Log;
import com.dff.cordova.plugin.common.log.CordovaPluginLog;
import com.dff.cordova.plugin.location.resources.LocationResources;
import org.apache.cordova.LOG;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class to read/write data in a file.
 *
 * @author Anthony Nahas
 * @version 2.0
 * @since 05.12.2016
 */
public class FileHelper {

    private static final String TAG = "FileHelper";

    /**
     * Store all locations that exists in the location list in a file.
     *
     * @param context context of the application/service
     */
    public static void storePendingLocation(Context context) {
        Log.d(TAG, "onStorePendingLocation()");
        PreferencesHelper preferencesHelper = new PreferencesHelper(context);
        FileOutputStream fos = null;
        ObjectOutputStream os;
        try {
            fos = context.openFileOutput(LocationResources.LOCATION_FILE_NAME, Context.MODE_PRIVATE);
            switch (preferencesHelper.getReturnType()) {
                case LocationResources.DFF_STRING:
                    ArrayList<String> pendingLocationDffString = LocationResources.getLocationListDffString();
                    if (pendingLocationDffString.size() > 0) {
                        Log.d(TAG, "PendingLocationsList count = " + pendingLocationDffString.size());
                        //Mode_Append / private
                        os = new ObjectOutputStream(fos);
                        for (String location : pendingLocationDffString) {
                            os.writeObject(location);
                        }
                        os.close();
                    }
                    break;
                case LocationResources.JSON:
                    ArrayList<JSONObject> pendingLocationJSON = LocationResources.getLocationListJson();
                    if (pendingLocationJSON.size() > 0) {
                        Log.d(TAG, "PendingLocationsList count = " + pendingLocationJSON.size());
                        os = new ObjectOutputStream(fos);
                        for (JSONObject location : pendingLocationJSON) {
                            os.writeObject(location.toString());
                        }
                        os.close();
                    }
                    break;
            }
        } catch (IOException e) {
            CordovaPluginLog.e(TAG, "Error: ", e);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                CordovaPluginLog.e(TAG, "Error: ", e);
            }
        }
    }

    /**
     * Restore all locations from file to the locations list.
     *
     * @param context context of the application/service
     */
    public static void restorePendingLocation(Context context) {
        LOG.d(TAG, "onRestorePendingLocations()");
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        PreferencesHelper preferencesHelper = new PreferencesHelper(context);

        try {
            fis = context.openFileInput(LocationResources.LOCATION_FILE_NAME);
            int i = 0;
            if (fis.available() != 0) {
                Log.d(TAG, "fis is available!");
                ois = new ObjectInputStream(fis);

                Scanner scanner = new Scanner(fis);
                while (scanner.hasNext()){
                    LocationResources.getLocationListJson().add(new JSONObject(scanner.next()));
                }
                scanner.close();

                while (i != 0) {
                    /*
                    switch (preferencesHelper.getReturnType()) {
                        case LocationResources.DFF_STRING:
                            if (LocationResources.getLocationListDffString() != null) {
                                String location = (String) ois.readObject();
                                LocationResources.addLocationToListAsDffString(location);
                                Log.d(TAG, "location " + i + " = " + location);
                                i++;
                            } else {
                                Log.d(TAG, "array (dff string) location list is null");
                            }
                            break;
                        case LocationResources.JSON:
                            if (LocationResources.getLocationListJson() != null) {
                                String location = (String) ois.readObject();
                                LocationResources.addLocationToListAsJson(new JSONObject(location));
                                Log.d(TAG, "location " + i + " = " + location);
                                i++;
                            } else {
                                Log.d(TAG, "array location list is null");
                            }
                            break;
                    }
                    */
                }
            }

        } catch (IOException e) { // | ClassNotFoundException | JSONException
            CordovaPluginLog.e(TAG, "Error: ", e);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
                new PreferencesHelper(context).setLocationCanBeCleared(true);
            } catch (IOException e) {
                CordovaPluginLog.e(TAG, "Error: ", e);
            }
        }
    }
}
