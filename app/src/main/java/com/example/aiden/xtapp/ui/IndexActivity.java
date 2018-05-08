package com.example.aiden.xtapp.ui;

import android.content.Intent;

import com.example.aiden.xtapp.R;
import com.example.aiden.xtapp.base.BaseActivity;
import com.example.aiden.xtapp.constant.SPConstant;
import com.example.aiden.xtapp.service.DbIntentService;
import com.example.aiden.xtapp.util.SpUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

public class IndexActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_index;
    }

    //开启事件监听
    @Override
    protected void enableEventBus() {
        setEnableEventBus(true);
    }

    @Override
    protected void onInitViewAfterOnCreate() {
        if (!SpUtil.getSP(SPConstant.SP_INIT, Boolean.class)) {

            EventBus.getDefault().post(new IndexEvent(IndexEvent.CODE_COPY_DATA_DB));
        }else{
            EventBus.getDefault().post(new IndexEvent(IndexEvent.CODE_DO_WEATHER));
        }

    }


    /**
     * 后台触发事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onAsyncEvent(IndexEvent event) {
        switch (event.code) {
            case IndexEvent.CODE_DO_WEATHER:
                delay();
                break;
            case IndexEvent.CODE_COPY_DATA_DB: {
                SpUtil.initSP();
                DbIntentService.setActionDataAreaAdd();
                EventBus.getDefault().post(new IndexEvent(IndexEvent.CODE_DO_WEATHER));//延迟跳转页面
            }

            break;
        }
    }


    private void delay() {
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                doSelect();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 5000);
    }

    private void doSelect() {
//        startActivity(new Intent(this, Home_V10_Activity.class));
        startActivity(new Intent(this, XtListDemoActivity.class));
        finish();
    }


    /**
     * 事件
     */
    private static class IndexEvent {
        /**
         * 延迟跳转页面
         */
        private static final int CODE_DO_WEATHER = 0X01;
        private static final int CODE_COPY_DATA_DB = 0X02;
        final int code;

        IndexEvent(int code) {
            this.code = code;
        }


    }
}
