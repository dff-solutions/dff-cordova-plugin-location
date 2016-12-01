package com.dff.cordova.plugin.location.utilities;

import java.util.concurrent.TimeUnit;

/**
 * Created by anahas on 01.12.2016.
 *
 * @author Anthony Nahas
 * @since 1.12.2016
 * @version 1.0
 */
public class TimeHelper {

    public static long getTimeAge(long time) {
        long diffInMillies = System.currentTimeMillis() - time;
        return TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

}
