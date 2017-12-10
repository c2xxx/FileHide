package com.chen.filehide.util;

import android.content.Context;

import com.chen.filehide.MyApplication;

/**
 * Created by hui on 2017/4/23.
 */

public class SharedPreferencesHelper {
    public static void save(String key, String value) {
        android.content.SharedPreferences sp = MyApplication.getContext().getSharedPreferences("z", Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String read(String key) {
        android.content.SharedPreferences sp = MyApplication.getContext().getSharedPreferences("z", Context.MODE_PRIVATE);
        return sp.getString(key, null);
    }


}
