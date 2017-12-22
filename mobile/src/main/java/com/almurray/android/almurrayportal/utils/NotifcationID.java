package com.almurray.android.almurrayportal.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tom on 13/12/2017.
 */

public class NotifcationID {
    private final static AtomicInteger c = new AtomicInteger(0);
    public static int getID() {
        return c.incrementAndGet();
    }
}
