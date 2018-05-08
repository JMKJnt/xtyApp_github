package com.example.aiden.xtapp.util;

import android.util.Log;

import com.example.aiden.xtapp.BuildConfig;


/**
 * @auther by yushilei.
 * @time 2017/4/13-17:49
 * @desc
 */

public class TimeCompute {
    private String TAG;
    private String name;

    public TimeCompute(String TAG, String name) {
        this.TAG = TAG;
        this.name = name;
    }

    private long startTime;
    private long endTime;
    private boolean isStart = false;

    public void start() {
        if (BuildConfig.DEBUG)
            Log.i(TAG, name + " 开始");
        isStart = true;
        startTime = System.currentTimeMillis();
    }

    public void end() {
        if (isStart) {
            endTime = System.currentTimeMillis();
            float second = ((endTime - startTime) / 1000f);
            if (BuildConfig.DEBUG)
                Log.i(TAG, name + " 结束：" + second + "秒");
        }
    }

}
