package com.cpxiao.androidutils.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * SystemUtils
 *
 * @author cpxiao on 2016/7/22
 */
public class SystemUtils {
    private static final String TAG =  SystemUtils.class.getSimpleName();

    /**
     * cannot be instantiated
     **/
    private SystemUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    public static boolean isAppActive(Context context, String MY_PKG_NAME) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        boolean isAppRunning = false;
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(MY_PKG_NAME) || info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
                isAppRunning = true;
                Log.i(TAG, info.topActivity.getPackageName() + " info.baseActivity.getPackageName()=" + info.baseActivity.getPackageName());
                break;
            }
        }
        return isAppRunning;
    }
}
