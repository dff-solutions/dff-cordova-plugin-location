package com.dff.cordova.plugin.location.utilities;

import java.util.concurrent.TimeUnit;

/**
 * Simple class to handle tasks related to the time.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 1.12.2016
 */
public class TimeHelper {

    /**
     * Calculate the difference between the current time and the given time in ms.
     *
     * @param time - The time to compare to.
     * @return - The difference time.
     */
    public static long getTimeAge(long time) {
        long diffInMillies = System.currentTimeMillis() - time;
        return TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

}
