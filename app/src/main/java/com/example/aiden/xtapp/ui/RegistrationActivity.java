package com.example.aiden.xtapp.ui;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.example.aiden.xtapp.R;
import com.example.aiden.xtapp.base.BaseActivity;
import com.example.aiden.xtapp.base.BaseApp;
import com.example.aiden.xtapp.entity.json.RegistrationRequest;
import com.example.aiden.xtapp.util.GsonUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

public class RegistrationActivity extends BaseActivity {


    @BindView(R.id.editText_registration_userTel)
    protected EditText editText_registration_userTel;

    @BindView(R.id.editText_registration_userPassWord)
    protected EditText editText_registration_userPassWord;

    @BindView(R.id.editText_registration_verification)
    protected EditText editText_registration_verification;

    @BindView(R.id.button_registration_verification)
    protected Button button_registration_verification;

    @BindView(R.id.button_registration_submit)
    protected Button button_registration_submit;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_registration;
    }

    //开启事件监听
    @Override
    protected void enableEventBus() {
        setEnableEventBus(true);
    }

    //页面加载完后执行

    @Override
    protected void onInitViewAfterOnCreate() {

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
    public void onAsyncEvent(RegistrationActivity.RegistrationEvent event) {
        switch (event.code) {
            case RegistrationActivity.RegistrationEvent.CODE_DO_REGISTRATION:
                httpRegistration(event.tel, event.password,event.securityCode);
                break;

        }
    }


    /**
     * 前台触发事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(RegistrationActivity.RegistrationEvent event) {
        switch (event.code) {
            case RegistrationActivity.RegistrationEvent.CODE_REGISTRATION_FINISHED:
                processingReturn(event.result);
                break;
            case RegistrationActivity.RegistrationEvent.CODE_START_ACTIVITY://跳转首页
                JumpLoginActivity();
                break;

        }
    }


    //按钮点击事件
    @OnClick({R.id.button_registration_verification,
            R.id.button_registration_submit,
    R.id.imageView_registration_back})
    protected void buttonClick(View v) {
        switch (v.getId()){
            case R.id.button_registration_verification:
                //发送验证码模块
                break;
            case R.id.button_registration_submit:
                attemptLogin();
                break;
            case R.id.imageView_registration_back:
                jumpSelectActivity();
                break;
        }

    }

    private void processingReturn(String result) {
        Log.i("注册返回结果:", result);
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }

    public void JumpLoginActivity() {
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

    private void jumpSelectActivity(){
        startActivity(new Intent(this,SelectActivity.class));
        finish();
    }

    /**
     * 登录事件,判断参数
     */
    private void attemptLogin() {
        String tel = editText_registration_userTel.getText().toString();
        String password = editText_registration_userPassWord.getText().toString();
        String securityCode=editText_registration_verification.getText().toString();
        editText_registration_userTel.setError(null);
        editText_registration_userPassWord.setError(null);
        if ("".equals(tel)) {
            editText_registration_userTel.setError("请输入手机号");
        } else if ("".equals(password)) {
            editText_registration_userPassWord.setError("请输入密码");
        } else {
            EventBus.getDefault().post(new RegistrationActivity.RegistrationEvent(RegistrationActivity.RegistrationEvent.CODE_DO_REGISTRATION, tel, password,"123456"));//参数判断完,掉HTTP方法


        }
    }

    //访问后台登录
    private void httpRegistration(String tel, String password,String securityCode) {
        //登录最后一步0
        BaseApp.getHttpApiClient_zumelAPPInterface().UserSignin(tel, password,securityCode, new ApiCallback() {
            @Override
            public void onFailure(ApiRequest request, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(ApiRequest request, ApiResponse response) {
                try {
                    doRegistrationEventData(response);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }

    /**
     * 处理登录返回数据
     * @param response
     * @return
     * @throws IOException
     */
    private void doRegistrationEventData(ApiResponse response)  {

        String bodyString = new String(response.getBody(), SdkConstant.CLOUDAPI_ENCODING);
        RegistrationRequest registratioRequest= GsonUtil.jsonToBean(bodyString,RegistrationRequest.class);
        Log.i("message:",bodyString);
        if(registratioRequest!=null) {
            if (0 == registratioRequest.getRequestCode()) {
                Log.i("message:", "注册成功");
                //存储到缓存
//            SpUtil.saveSP(SPConstant.SP_USER_BASE,GsonUtil.beanToJson(loginTelRequest.getUserBase()));
//            SpUtil.saveSP(SPConstant.SP_USER_AND_EQUIPMENT,GsonUtil.beanToJson(loginTelRequest.getUserAndEquipment()));
                EventBus.getDefault().post(new RegistrationActivity.RegistrationEvent(RegistrationActivity.RegistrationEvent.CODE_START_ACTIVITY));//跳转页面
            } else {
                Toast.makeText(this, registratioRequest.getRequestMessage(), Toast.LENGTH_SHORT).show();//xxxxxxxx
            }
        }else{
            Toast.makeText(this, "返回接参的对象为空", Toast.LENGTH_SHORT).show();//xxxxxxxx
        }
    }

    /**
     * 事件 实体
     */
    private static class RegistrationEvent {
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
        String tel;
        String password;
        String result;
        String securityCode;

        RegistrationEvent(int code) {
            this.code = code;
        }

        public RegistrationEvent(int code, String result) {
            this.code = code;
            this.result = result;
        }


        public RegistrationEvent(int code, String tel, String password,String securityCode) {
            this.code = code;
            this.tel = tel;
            this.password = password;
            this.securityCode=securityCode;
        }
    }

}
