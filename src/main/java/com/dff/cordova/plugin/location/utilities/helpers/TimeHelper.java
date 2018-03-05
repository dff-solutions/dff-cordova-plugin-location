package com.dff.cordova.plugin.location.utilities.helpers;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Simple classes to handle tasks related to the time.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 1.12.2016
 */
@Singleton
public class TimeHelper {

    @Inject
    public TimeHelper() {
    }

    /**
     * Calculate the difference between the current time and the given time in ms.
     *
     * @param time - The time to compare to.
     * @return - The difference time.
     */
    public long getTimeAge(long time) {
        long diffInMillies = System.currentTimeMillis() - time;
        return TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    /**
     * Print out the difference time between the current time of the operating system
     * and the one the last good location.
     * <p>
     * NB: Used for test/debug purposes.
     */
    private static void printDifference(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy/MM/dd HH:mm:ss");
        try {
            Date d = new Date(date.getTime());
            Log.d("Location", "ep =" + dateFormat.format(d));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        Date now = new Date();
        Log.d("Location", "now = " + dateFormat.format(now));
    }

}
