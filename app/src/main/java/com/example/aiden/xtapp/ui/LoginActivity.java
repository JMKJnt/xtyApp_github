
package com.example.aiden.xtapp.ui;

import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.example.aiden.xtapp.R;
import com.example.aiden.xtapp.base.BaseActivity;
import com.example.aiden.xtapp.base.BaseApp;
import com.example.aiden.xtapp.constant.SPConstant;
import com.example.aiden.xtapp.entity.json.LoginTelRequest;
import com.example.aiden.xtapp.util.GsonUtil;
import com.example.aiden.xtapp.util.SpUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A login screen that offers login via tel/password.
 */
public class LoginActivity extends BaseActivity {


    // UI references.
    @BindView(R.id.editText_login_tel)
    protected EditText mTelEt;
    @BindView(R.id.editText_login_password)
    protected EditText mPasswordEt;
    @BindView(R.id.login_progress)
    protected View mProgressView;
    @BindView(R.id.login_form)
    protected View mLoginFormView;
    @BindView(R.id.button_login_sign)
    protected Button mSignBtn;
    @BindView(R.id.button_login_register)
    protected Button mBtnRegister;

    //当前Activity对应的layout
    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    //开启事件监听
    @Override
    protected void enableEventBus() {
        setEnableEventBus(true);
    }

    //页面加载完后执行
    @Override
    protected void onInitViewAfterOnCreate() {
        mPasswordEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 后台触发事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onAsyncEvent(LoginEvent event) {
        switch (event.code) {
            case LoginEvent.CODE_DO_LOGIN:
                httpLogin(event.tel, event.password);
                break;

        }
    }


    /**
     * 前台触发事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(LoginEvent event) {
        switch (event.code) {
//            case LoginEvent.CODE_LOGIN_FINISHED:
//                processingReturn(event.result);
//                break;
            case LoginEvent.CODE_START_ACTIVITY://跳转首页
                jumpWeatherIndex();
                break;

        }
    }


    //按钮点击事件
    @OnClick({R.id.button_login_register,
            R.id.button_login_sign,
    R.id.imageView_login_back})
    protected void buttonClick(View v) {
        switch (v.getId()){
            case R.id.button_login_register:
                startActivity(new Intent(this, RegistrationActivity.class));
                break;
            case R.id.button_login_sign:
                attemptLogin();
                break;
            case R.id.imageView_login_back:
                jumpSelectActivity();
        }

    }


    /**
     * 登录事件,判断参数
     */
    private void attemptLogin() {
        String tel = mTelEt.getText().toString();
        String password = mPasswordEt.getText().toString();
        mTelEt.setError(null);
        mPasswordEt.setError(null);
        if ("".equals(tel)) {
            mTelEt.setError("请输入手机号。");
        } else if ("".equals(password)) {
            mPasswordEt.setError("请输入密码");
        } else {
            EventBus.getDefault().post(new LoginEvent(LoginEvent.CODE_DO_LOGIN, tel, password));//参数判断完,掉HTTP方法


        }
    }

    //访问后台登录
    private void httpLogin(String tel, String password) {
        //登录最后一步0
        BaseApp.getHttpApiClient_zumelAPPInterface().loginTel(tel, password, new ApiCallback() {
            @Override
            public void onFailure(ApiRequest request, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(ApiRequest request, ApiResponse response) {
                try {
                    doLoginTelData(response);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }


//    private void processingReturn(String result) {
//        Log.i("登录返回结果:", result);
//        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
//    }

    /**
     * 处理登录返回数据
     * @param response
     * @return
     * @throws IOException
     */
    private void doLoginTelData(ApiResponse response)  {

        String bodyString = new String(response.getBody(), SdkConstant.CLOUDAPI_ENCODING);
        LoginTelRequest loginTelRequest= GsonUtil.jsonToBean(bodyString,LoginTelRequest.class);
        if(0==loginTelRequest.getRequestCode()){
            Log.i("登录成功:","用户"+loginTelRequest.getUserBase().getUserTel()+"登录成功");
            //存储到缓存
            SpUtil.saveSP(SPConstant.SP_USER_BASE,GsonUtil.beanToJson(loginTelRequest.getUserBase()));
            SpUtil.saveSP(SPConstant.SP_USER_AND_EQUIPMENT,GsonUtil.beanToJson(loginTelRequest.getUserAndEquipment()));
            EventBus.getDefault().post(new LoginEvent(LoginEvent.CODE_START_ACTIVITY));//跳转页面
        }else{
            Toast.makeText(this,loginTelRequest.getRequestMessage(),Toast.LENGTH_SHORT).show();
        }
    }


    public void jumpWeatherIndex() {
        startActivity(new Intent(this,Home_V10_Activity.class));
//        startActivity(new Intent(this, XtListDemoActivity.class));
        finish();
    }

    private void jumpSelectActivity(){
        startActivity(new Intent(this,SelectActivity.class));
        finish();
    }

    /**
     * 事件 实体
     */
    private static class LoginEvent {
        /**
         * 参数判断完成,开始访问后台
         */
        private static final int CODE_DO_LOGIN = 0X01;
        /**
         * 登录请求处理结束Finished
         */
        private static final int CODE_LOGIN_FINISHED = 0X02;
        /**
         * 跳转页面
         */
        private static final int CODE_START_ACTIVITY = 0X03;
        final int code;
        String tel;
        String password;
        String result;

        LoginEvent(int code) {
            this.code = code;
        }

        public LoginEvent(int code, String result) {
            this.code = code;
            this.result = result;
        }


        public LoginEvent(int code, String tel, String password) {
            this.code = code;
            this.tel = tel;
            this.password = password;
        }
    }
}

