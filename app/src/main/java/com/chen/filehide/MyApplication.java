package com.chen.filehide;

import android.app.Application;
import android.content.Context;

/**
 * Created by hui on 2017/4/23.
 */

public class MyApplication extends Application {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
