package com.example.aiden.xtapp.util;

import android.content.Context;
import android.content.SharedPreferences;


import com.example.aiden.xtapp.base.BaseApp;
import com.example.aiden.xtapp.constant.SPConstant;

/**
 * @auther by yushilei.
 * @time 2016/10/25-16:00
 * @desc
 */

public class SpUtil {
    private SpUtil() {
    }

    private static final String SP_NAME = "local_sp";

    /**
     * 获取指定 名称为 local_sp 的sp
     */
    private static SharedPreferences getLocalSP() {
        return BaseApp.context().getApplicationContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 存储 sp key  -value
     */
    public static void saveSP(String key, Object value) {
        SharedPreferences.Editor edit = getLocalSP().edit();
        if (value instanceof Boolean) {
            edit.putBoolean(key, (Boolean) value);
        } else if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            edit.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            edit.putLong(key, (Long) value);
        }
        edit.apply();
    }

    /**
     * 获取 sp中 指定key的 value
     *
     * @param key    key
     * @param tClass 获取的value 类型
     */
    @SuppressWarnings("unchecked")
    public static <T> T getSP(String key, Class<T> tClass) {
        T value = null;
        SharedPreferences sp = getLocalSP();
        if (tClass.isAssignableFrom(Boolean.class)) {
            value = (T) Boolean.valueOf(sp.getBoolean(key, false));
        } else if (tClass.isAssignableFrom(Integer.class)) {
            value = (T) Integer.valueOf(sp.getInt(key, -1));
        } else if (tClass.isAssignableFrom(String.class)) {
            value = (T) String.valueOf(sp.getString(key, null));
        } else if (tClass.isAssignableFrom(Float.class)) {
            value = (T) Float.valueOf(sp.getFloat(key, -1.0f));
        } else if (tClass.isAssignableFrom(Long.class)) {
            value = (T) Long.valueOf(sp.getLong(key, -1));
        }
        return value;
    }

    public static void initSP() {
        SharedPreferences sp = getLocalSP();
        SharedPreferences.Editor edit = sp.edit();
        //设置已经初始化
        edit.putBoolean(SPConstant.SP_INIT, true);
        //设置未回传数据
//        edit.putBoolean(SPConstant.SP_UPLOAD, false);
        //设置未展示过引导页
//        edit.putBoolean(SPConstant.SP_GUIDE_GOOD_SEARCH_SHOWED, false);
//        edit.putBoolean(SPConstant.SP_GUIDE_SEAL_SETTING_SHOWED, false);
        edit.apply();
    }


}
