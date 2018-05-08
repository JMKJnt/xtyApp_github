package com.example.aiden.xtapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.aiden.xtapp.R;
import com.example.aiden.xtapp.base.BaseActivity;
import com.example.aiden.xtapp.entity.base.UserMemorandumSet;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 留言备忘录编辑
 * Created by admin on 2018/1/8.
 */

public class MemorandumActivity extends BaseActivity {

    @BindView(R.id.button_save)
    protected Button button_save;

    @BindView(R.id.editText_setMemorandum)
    protected EditText editText_setMemorandum;

//备忘录显示开关功能，暂时没加，先注释控件
//    @BindView(R.id.switch_setMemorandum)
//    protected Switch switch_setMemorandum;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_note;
    }

    //开启事件监听
    @Override
    protected void enableEventBus() {
        setEnableEventBus(true);
    }

    @Override
    protected void onInitViewAfterOnCreate() {
        Intent intent = getIntent();//获取传来的intent对象
        String UserMemorandumSetStr = intent.getStringExtra("UserMemorandumSetStr");//获取键值对的键名
        editText_setMemorandum.setText(UserMemorandumSetStr);
//        button_registration_submit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                if (id == R.id.login || id == EditorInfo.IME_NULL) {
//                    attemptLogin();
//                    return true;
//                }
//                return false;
//            }
//        });
    }

    /**
     * 后台触发事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onAsyncEvent(MemorandumEvent event) {
//        switch (event.code) {
//            case SetCityEvent.DO_SE_AREA:
//                doSeCity(event.result);
//                break;
//
//        }
    }


    /**
     * 前台触发事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(MemorandumEvent event) {
        switch (event.code) {
            case MemorandumEvent.CODE_REGISTRATION_FINISHED:
//                processingReturn(event.result);
                break;
//            case MemorandumEvent.CODE_START_ACTIVITY://跳转首页
//                JumpLoginActivity();
//                break;

        }
    }


    //按钮点击事件
    @OnClick({R.id.button_save})
    protected void buttonClick(View v) {
        switch (v.getId()) {
            case R.id.button_save:
                //用户设置的留言备忘录信息 提交返回上级页面（由上级页面访问server处理）
                setMemorandum();
                break;

        }

    }

    private void setMemorandum(){
        UserMemorandumSet userMemorandumSet =new UserMemorandumSet();
        userMemorandumSet.setContentStr(editText_setMemorandum.getText().toString());
        userMemorandumSet.setContent_control_YN("1");
        backWeatherIndexActivity(userMemorandumSet);
    }

    private  void backWeatherIndexActivity(Object object)
    {
        UserMemorandumSet userMemorandumSet =(UserMemorandumSet)object;
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("contentStr", userMemorandumSet.getContentStr());
        bundle.putString("content_control_YN", userMemorandumSet.getContent_control_YN());
        resultIntent.putExtras(bundle);
        this.setResult(RESULT_OK, resultIntent);
        finish();
    }


    /**
     * 事件 实体
     */
    private static class MemorandumEvent {
        /**
         * 参数判断完成,开始访问后台
         */
        private static final int CODE_DO_REGISTRATION = 0X01;
        /**
         * 登录请求处理结束Finished
         */
        private static final int CODE_REGISTRATION_FINISHED = 0X02;
        /**
         * 跳转页面
         */
        private static final int CODE_START_ACTIVITY = 0X03;

        final int code;
        String result;
//        String tel;
//        String password;
//        String securityCode;

        MemorandumEvent(int code) {
            this.code = code;
        }

        public MemorandumEvent(int code, String result) {
            this.code = code;
            this.result = result;
        }


//        public MemorandumEvent(int code, String tel, String password,String securityCode) {
//            this.code = code;
//            this.tel = tel;
//            this.password = password;
//            this.securityCode=securityCode;
//        }
    }
}
