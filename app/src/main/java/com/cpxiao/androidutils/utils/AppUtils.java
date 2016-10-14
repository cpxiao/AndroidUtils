package com.cpxiao.androidutils.utils;


import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * 跟App相关的辅助类
 *
 * @author cpxiao on 2016/5/30
 */
public class AppUtils {

    /**
     * cannot be instantiated
     **/
    private AppUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取当前应用程序包名
     *
     * @param context context
     * @return packageName
     */
    public static String getPackageName(Context context) {
        if (context == null) {
            return null;
        }
        return context.getPackageName();
    }

    /**
     * 获取当前应用程序名称
     *
     * @param context context
     * @return App Name
     */
    public static String getAppName(Context context) {
        if (context == null) {
            return null;
        }
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    /**
     * 获取当前应用程序版本名称
     *
     * @param context context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        if (context == null) {
            return null;
        }
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    /**
     * 获取当前应用程序版本号
     *
     * @param context context
     * @return 当前应用的版本号
     */
    public static int getVersionCode(Context context) {
        if (context == null) {
            return 0;
        }
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.getMessage();
        }
        return 0;
    }

    /**
     * 打开应用
     *
     * @param context     context
     * @param packageName 包名
     */
    public static void openApp(Context context, String packageName) {
        if (context == null || packageName == null) {
            return;
        }
        PackageManager pm = context.getPackageManager();
        if (pm != null) {
            Intent intent = pm.getLaunchIntentForPackage(packageName);
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }

    /**
     * 是否安装了某个app
     *
     * @param context     context
     * @param packageName 包名
     * @param versionCode 版本号
     * @return 已安装返回true, 否则返回false
     */
    public static boolean isInstalled(Context context, String packageName, int versionCode) {
        try {
            PackageManager pm = context.getPackageManager();
            if (pm != null) {
                PackageInfo packagesInfo = pm.getPackageInfo(packageName, 0);
                if (packagesInfo != null) {
                    return packagesInfo.versionCode == versionCode;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 设置铃声
     *
     * @param context context
     * @param path    path
     */
    public static void setRingtone(Context context, String path) {
        File file = new File(path);
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, file.getName());
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
        values.put(MediaStore.Audio.Media.IS_ALARM, false);
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);

        Uri uri = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());
        Uri newUri = context.getContentResolver().insert(uri, values);
        RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri);
    }

    /**
     * 设置壁纸
     *
     * @param context   context
     * @param target    target
     * @param totalByte totalByte
     * @return boolean
     */
    public static boolean setWallPaper(Context context, String target, float totalByte) {
        float maxSize = 300f * 1024f;
        try {
            float scale;//缩放比例
            if (totalByte > maxSize) {
                scale = totalByte / maxSize;
            } else {
                scale = 1;
            }
            if (scale <= 0) {
                scale = 1;
            }
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = false;
            opts.inSampleSize = Math.round(scale) + 1;
            Bitmap image = BitmapFactory.decodeFile(target, opts);
            setWallPaper(context, image);
            if (image != null && !image.isRecycled()) {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
                    image.recycle();
                }
                image = null;
            }
            return true;
        } catch (Exception e) {
            System.gc();
            return false;
        }
    }

    /**
     * 设置壁纸
     *
     * @param context context
     * @param bitmap  bitmap
     */
    public static void setWallPaper(Context context, Bitmap bitmap) {
        WallpaperManager wm = WallpaperManager.getInstance(context);
        try {
            wm.setBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            System.gc();
        }
    }

    /**
     * get user apps in static apps
     *
     * @param context context
     * @return ["a","b","c","e","d","f",....]
     */
    public static String getUserInstalledApps(Context context) {
        if (context == null) {
            return null;
        }
        JSONArray result = new JSONArray();
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> packages = packageManager.getInstalledApplications(0);
        for (ApplicationInfo app : packages) {
            try {
                result.put(app.packageName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }

    /**
     * Get User Apps and version
     */
    public static HashMap<String, String> getUserAppsVersion(Context c) {
        JSONArray array = new JSONArray();
        PackageManager packageManager = c.getPackageManager();
        PackageInfo info;
        List<ApplicationInfo> packages = packageManager.getInstalledApplications(0);
        for (ApplicationInfo app : packages) {
            JSONObject obj = new JSONObject();
            try {
                info = packageManager.getPackageInfo(app.packageName, 0);
                obj.put("appid", app.packageName);//"com.adobe.flashplayer"
                obj.put("appid", app.taskAffinity);//"com.adobe.flashplayer"
                obj.put("appid", app.sourceDir);//"/system/app/install_flash_player.apk"
                obj.put("appid", app.publicSourceDir);//"/system/app/install_flash_player.apk"
                obj.put("appid", app.dataDir);//"/data/data/com.adobe.flashplayer"
                obj.put("appid", app.processName);//"com.adobe.flashplayer"
                obj.put("appid", app.nativeLibraryDir);//"/data/data/com.adobe.flashplayer/lib"
                obj.put("appid", app.nonLocalizedLabel);//"Adobe Flash Player 11.1"
                obj.put("appid", app.targetSdkVersion);//"5"

                obj.put("version_name", info.versionName);// "11.1.115.12"
                obj.put("version_code", info.firstInstallTime);//1366824036000
                obj.put("version_code", info.lastUpdateTime);//1366824036000

                ApplicationInfo applicationInfo = info.applicationInfo;
                if (info.versionName != null && !info.versionName.equals("")) {
                    array.put(obj);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        HashMap<String, String> maps = new HashMap<>();
        maps.put("apps_info", array.toString());
        return maps;
    }
}
