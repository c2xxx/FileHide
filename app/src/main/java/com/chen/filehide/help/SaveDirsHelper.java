package com.chen.filehide.help;

import android.text.TextUtils;

import com.chen.filehide.util.SharedPreferencesHelper;
import com.chen.filehide.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hui on 2017/4/23.
 */

public class SaveDirsHelper {
    private static final String SPLIT_TAG = ">>>";
    private static final String KEY_NAME = "SaveDirsHelper";

    private static void save(List<String> dirs) {
        if (dirs == null) {
            SharedPreferencesHelper.save(KEY_NAME, "");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String dir : dirs) {
            sb.append(SPLIT_TAG + dir);
        }
        if (sb.length() > 0) {
            SharedPreferencesHelper.save(KEY_NAME, sb.toString().replaceFirst(SPLIT_TAG, ""));
        } else {
            SharedPreferencesHelper.save(KEY_NAME, "");
        }
    }

    public static void remove(String dir) {
        List<String> dirs = readDirs();
        dirs.remove(dir);
        save(dirs);
    }

    public static List<String> readDirs() {
        List<String> list = new ArrayList<>();
        String values = SharedPreferencesHelper.read(KEY_NAME);
        if (TextUtils.isEmpty(values)) {
            return list;
        }
        String[] dirs = values.split(SPLIT_TAG);
        for (String dir : dirs) {
            if (TextUtils.isEmpty(dir)) {
                continue;
            }
            list.add(dir);
        }
        return list;
    }

    public static boolean appendDir(String curr) {
        if (TextUtils.isEmpty(curr)) {
            return false;
        }
        List<String> dirs = readDirs();
        if (dirs.contains(curr)) {
            ToastUtil.show("已经存在！");
            return false;
        }
        String values = SharedPreferencesHelper.read(KEY_NAME);
        if (TextUtils.isEmpty(values)) {
            values = curr;
        } else {
            values += SPLIT_TAG + curr;
        }
        SharedPreferencesHelper.save(KEY_NAME, values);
        return true;
    }
}
