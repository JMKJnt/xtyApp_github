package com.example.aiden.xtapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.aiden.xtapp.BuildConfig;
import com.example.aiden.xtapp.R;
import com.example.aiden.xtapp.base.BaseApp;
import com.example.aiden.xtapp.db.XDBManager;
import com.example.aiden.xtapp.entity.base.Area;
import com.example.aiden.xtapp.util.GsonUtil;
import com.example.aiden.xtapp.util.TimeCompute;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by chenly on 2017/11/23.
 */

public class DbIntentService extends IntentService {
    private static final String TAG = "DbIntentService";
    public static final String ACTION_DATA_AREA_ADD = "com.example.aiden.zumelapp.service.action.areaAdd";

    public DbIntentService() {
        super("DbIntentService");
    }


    public static void setActionDataAreaAdd() {
        Intent intent = new Intent(BaseApp.context(), DbIntentService.class);
        intent.setAction(ACTION_DATA_AREA_ADD);
        BaseApp.context().startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DATA_AREA_ADD.equals(action)) {
                handleActionDataDb();
            }
        }
    }

    /**
     * 读取JSON文本,写入数据库
     */
    private void handleActionDataDb() {
        TimeCompute timeCompute = new TimeCompute(TAG, "初始化区域表");
        timeCompute.start();
        doAddArea();
        timeCompute.end();
    }


    private void doAddArea() {


        try {
            DbManager db = XDBManager.getInstance().getInitDb();
            InputStream open = BaseApp.resource().openRawResource(R.raw.city_json);
            StringBuilder sb = new StringBuilder();
            InputStreamReader inputStreamReader = new InputStreamReader(open, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = "";
            while ((str = bufferedReader.readLine()) != null) {
                sb.append(str);
            }

            ArrayList<Area> areaList = GsonUtil.jsonArrayToList(sb.toString(), Area.class);
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "初始化区域表" + areaList.size() + "条");
            }
            db.save(areaList);
//            for (int i = 0; i < areaList.size(); i++) {
//                Area area=areaList.get(i);
//                Log.i(TAG,area.getAREAID());
//                db.save(area);
//            }
        } catch (DbException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }


    }
}
