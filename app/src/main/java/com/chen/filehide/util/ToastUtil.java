package com.chen.filehide.util;

import android.widget.Toast;

import com.chen.filehide.MyApplication;

/**
 * Created by hui on 2017/4/23.
 */

public class ToastUtil {
    private static Toast toast;

    public static void show(String msg) {
        if (toast == null) {
            toast = Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_LONG);
        }
        toast.setText(msg);
        toast.show();
    }
}
