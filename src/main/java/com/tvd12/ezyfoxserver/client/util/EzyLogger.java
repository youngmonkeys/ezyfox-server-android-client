package com.tvd12.ezyfoxserver.client.util;

import android.util.Log;

/**
 * Created by Dung Ta Van on 7/29/19.
 * Copyright © 2019 Young Monkeys. All rights reserved.
 **/
public final class EzyLogger {

    private static volatile boolean debug = true;
    private static final String TAG = "ezyfox-client";

    private EzyLogger() {
    }

    public static void debug(String msg) {
        if(debug)
            Log.d(TAG, msg);
    }

    public static void info(String msg) {
        if(debug)
            Log.i(TAG, msg);
    }
}
