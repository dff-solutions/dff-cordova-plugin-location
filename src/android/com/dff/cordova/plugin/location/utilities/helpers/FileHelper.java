package com.dff.cordova.plugin.location.utilities.helpers;

import android.content.Context;
import android.util.Log;
import com.dff.cordova.plugin.common.log.CordovaPluginLog;
import com.dff.cordova.plugin.location.resources.LocationResources;
import org.apache.cordova.LOG;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

/**
 * Class to read/write data in a file.
 *
 * @author Anthony Nahas
 * @version 1.3
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
        File file = null;
        FileOutputStream fos = null;
        ArrayList<String> pendingLocation = LocationResources.getLocationListDffString();


        try {
            /*File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
            boolean isPresent = true;
            if (!docsFolder.exists()) {
                Log.d(TAG, "Dir is not present");
                isPresent = docsFolder.mkdir();
            }
            if (isPresent) {
                Log.d(TAG, "Dir is present");
                file = new File(docsFolder.getAbsolutePath(), LocationResources.LOCATION_FILE_NAME);
            } else {
                // Failure
            }
            String path = file != null ? file.getName() : null;
            Log.d(TAG, "Path = " + path);*/

            File f = new File(LocationResources.LOCATION_FILE_NAME);
            fos = context.openFileOutput(LocationResources.LOCATION_FILE_NAME, Context.MODE_PRIVATE); //Mode_Append / private
            ObjectOutputStream os = new ObjectOutputStream(fos);

            Log.d(TAG, "PendingLocationsList count = " + pendingLocation.size());
            if (pendingLocation.size() > 0) {
                for (String location : pendingLocation) {
                    os.writeObject(location);
                }
                os.close();
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

                while (true) {
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
                                JSONObject location = (JSONObject) ois.readObject();
                                LocationResources.addLocationToListAsJson(location);
                                Log.d(TAG, "location " + i + " = " + location);
                                i++;
                            } else {
                                Log.d(TAG, "array location list is null");
                            }
                            break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            CordovaPluginLog.e(TAG, "Error: ", e);
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
