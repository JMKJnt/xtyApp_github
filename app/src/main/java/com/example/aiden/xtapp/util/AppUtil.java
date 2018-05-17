package com.example.aiden.xtapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


import com.example.aiden.xtapp.base.BaseApp;

import java.io.File;

/**
 * @auther by yushilei.
 * @time 2016/11/1-14:58
 * @desc
 */

public class AppUtil {
    private AppUtil() {
    }

    public static String getAppVersionV() {

        String version = null;
        PackageManager packageManager = BaseApp.context().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(BaseApp.context().getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "v " + version;
    }

    public static String getAppVersion() {
        String version = null;
        PackageManager packageManager = BaseApp.context().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(BaseApp.context().getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public static int getAppVersionCode() {
        int version = 0;
        PackageManager packageManager = BaseApp.context().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(BaseApp.context().getPackageName(), 0);
            version = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

//    public static void disableHomeAndRecentAppKeyByUserType(Activity activity) {
//        if (AccountManager.getCurrentLoginUserType() == AccountManager.USER_SALER) {
//            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
//            lp.dimAmount = 0.0f;
//            activity.getWindow().setAttributes(lp);
//            activity.getWindow().addFlags(5);
//            activity.getWindow().addFlags(3);
//            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }
//    }



    public static void disableHomeAndRecentAppKeyByUserType(Activity activity) {

        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.dimAmount = 0.0f;
        activity.getWindow().setAttributes(lp);
        activity.getWindow().addFlags(5);
        activity.getWindow().addFlags(3);
        // activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    /**
     * 获取手机型号
     */
    public static String getMODEL() {
        return android.os.Build.MODEL;
    }

    /**
     * 覆盖安装
     *
     * @param context
     * @param apkFile
     */
    public static void install(Context context, File apkFile) {
        Intent newIntent = new Intent(Intent.ACTION_VIEW);
        newIntent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newIntent);
    }

    public static void hideSoftInput(Activity activity) {
        InputMethodManager imm = (InputMethodManager) BaseApp.context().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive())
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }

    public static void hideSoftInput(Window window) {
        InputMethodManager imm = (InputMethodManager) BaseApp.context().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive())
            imm.hideSoftInputFromWindow(window.getDecorView().getWindowToken(), 0);
    }
}
