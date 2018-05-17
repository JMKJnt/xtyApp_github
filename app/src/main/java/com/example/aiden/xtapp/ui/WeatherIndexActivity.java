package com.example.aiden.xtapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.espressif.iot.esptouch.demo_activity.EsptouchDemoActivity;
import com.example.aiden.xtapp.BuildConfig;
import com.example.aiden.xtapp.R;
import com.example.aiden.xtapp.base.BaseActivity;
import com.example.aiden.xtapp.base.BaseApp;
import com.example.aiden.xtapp.constant.SPConstant;
import com.example.aiden.xtapp.entity.base.UserAndEquipment;
import com.example.aiden.xtapp.entity.base.UserBase;
import com.example.aiden.xtapp.entity.json.LoginTelRequest;
import com.example.aiden.xtapp.entity.json.SetCityBaseRequest;
import com.example.aiden.xtapp.entity.json.UserAndEquipmentRequest;
import com.example.aiden.xtapp.util.GsonUtil;
import com.example.aiden.xtapp.util.SpUtil;
import com.example.aiden.xtapp.widget.MultiStateView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import zxing.activity.CaptureActivity;

public class WeatherIndexActivity extends BaseActivity {

    private static final int BARCODE_REQUEST_CODE = 0x123;//二维码扫描请求码
    private static final int BARCODE_REQUEST_SETCITY_CODE = 0x124;//选择城市后回调
    private static final int BARCODE_REQUEST_EASYLINK_CODE = 0x125;//一键上网页面的回调
    //整体布局
    @BindView(R.id.weather_multi)
    MultiStateView weatherMulti;
    //***********************设备布局区*****************************


    //设备型号
    @BindView(R.id.textView_weatherIndex_equipmentType)
    TextView mEquipmentTypeTv;

    //设备区域布局
    @BindView(R.id.button_weatherIndex_binding)
    LinearLayout mEquipmentContainerCl;

    //是否绑定设备 textView文字
    @BindView(R.id.textView_weatherIndex_equipmentEQ_text)
    TextView mEquipmentSetText;

    //设备出厂SN
    @BindView(R.id.textView_weatherIndex_equipmentSN)
    TextView mEquipmentSNTv;

    //*************************end*******************************

    //***********************城市信息布局区*****************************
    //城市信息布局
    @BindView(R.id.button_weatherIndex_selectCity)
    LinearLayout mcityContainerLl;

    //城市是否设置描述
    @BindView(R.id.textView_weatherIndex_cityName_set)
    TextView mCitySetTv;

    //城市名称
    @BindView(R.id.textView_weatherIndex_cityName)
    TextView mCityNameTv;



    //一键上网---按钮---
    @BindView(R.id.button_weatherIndex_easylink)
    TextView mEasylink;
    //*************************end*******************************


    private UserBase userBase;
    private  UserAndEquipment userAndEquipment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_weather_index;
    }

    @Override
    protected void enableEventBus() {
        setEnableEventBus(true);
    }

    //页面加载完后执行
    @Override
    protected void onInitViewAfterOnCreate() {
        weatherMulti.setState(MultiStateView.ContentState.LOADING);
        doGetUserBase();
        EventBus.getDefault().post(new WeatherIndexEvent(WeatherIndexEvent.DO_UPDATE_CONFIRM));


    }

    /**
     * 后台触发事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onAsyncEvent(WeatherIndexEvent event) {
        switch (event.code) {
            case WeatherIndexEvent.DO_UPDATE_CONFIRM://更新
                doUpdateConfirm();
                break;
            case WeatherIndexEvent.DO_LOGIN_YZ:
                httpLogin(event.userTel,event.userPassWord);
                break;
//            case WeatherIndexEvent.DO_GET_USER_AND_EQUIPMENT:
//                doGetUserAndEquipment();
//                break;
            case WeatherIndexEvent.DO_FOB_BAR_CODE:
                userAndEquipmentObj(event.massage);
                break;
        }
    }

    /**
     * 前台处理事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(WeatherIndexEvent event) {
        switch (event.code) {
            case WeatherIndexEvent.DO_UPDATE_UI:
                doUpdateUI();
                break;
            case WeatherIndexEvent.DO_SHOW_ERROR:
                doShowError(event.massage);
                break;
            case WeatherIndexEvent.DO_BOF_USER_AND_EQUIPMENT_Y:
                doUpdateUI();
                doShowMessage(event.massage);
                break;
            case WeatherIndexEvent.DO_BOF_USER_AND_EQUIPMENT_N:
                doShowMessage(event.massage);
                break;
            case WeatherIndexEvent.DO_BOF_SET_CITY_CODE:
                doShowMessage(event.massage);
                break;



        }
    }

    @OnClick({R.id.button_weatherIndex_binding,
    R.id.button_weatherIndex_selectCity,
            R.id.button_weatherIndex_easylink
    })
    public void buttonClick(View v){
        switch (v.getId()){
            case R.id.button_weatherIndex_binding://绑定设备
                bindingEquipment();
                break;
            case R.id.button_weatherIndex_selectCity://设置城市
                toSetCity();
                break;
            case R.id.button_weatherIndex_easylink://一键上网
                toEsptouchDemoActivity();
                break;
        }
    }


//    /**
//     * 底部按钮事件
//     * @param v
//     */
//    @OnClick({R.id.button_home_dashboard,
//            R.id.button_home_notifications
//    })
//    public void bottomMenuClick(View v){
//        switch (v.getId()){
//            case R.id.button_home_dashboard://跳转发现
//                startActivity(new Intent(v.getContext(),FinderActivity.class));
//                break;
//            case R.id.button_weatherIndex_selectCity://跳转我的
//                startActivity(new Intent(v.getContext(),MyActivity.class));
//                break;
//
//        }
//    }


    /**
     * 查询本地 用户数据
     */
    private void doGetUserBase() {
        String jsonUserBase = SpUtil.getSP(SPConstant.SP_USER_BASE, String.class);
        if (!"null".equals(jsonUserBase)) {
            userBase = GsonUtil.jsonToBean(jsonUserBase, UserBase.class);
            //调用《《==接口1==》》（用户登陆是否过期失效）返回结果cccc
            EventBus.getDefault().post(new WeatherIndexEvent(WeatherIndexEvent.DO_LOGIN_YZ, userBase.getUserTel(),userBase.getUserPassWord()));

        } else {
            //跳转到 登陆界面
            startActivity(new Intent(this, SelectActivity.class));
            //跳转后消亡 本calss WeatherIndexActivity
            finish();
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
        if(200==response.getCode()){
            String bodyString = new String(response.getBody(), SdkConstant.CLOUDAPI_ENCODING);
            LoginTelRequest loginTelRequest= GsonUtil.jsonToBean(bodyString,LoginTelRequest.class);
            if(0==loginTelRequest.getRequestCode()){
                Log.i("登录验证成功:","用户"+loginTelRequest.getUserBase().getUserTel());
                //存储到缓存
                SpUtil.saveSP(SPConstant.SP_USER_BASE,GsonUtil.beanToJson(loginTelRequest.getUserBase()));
                SpUtil.saveSP(SPConstant.SP_USER_AND_EQUIPMENT,GsonUtil.beanToJson(loginTelRequest.getUserAndEquipment()));
                userBase=loginTelRequest.getUserBase();
                if(0!=loginTelRequest.getUserAndEquipment().size()){
                    userAndEquipment=loginTelRequest.getUserAndEquipment().get(0);
                }
                EventBus.getDefault().post(new WeatherIndexEvent(WeatherIndexEvent.DO_UPDATE_UI));
            }else{
//            清空map信息（用户、设备、城市）
                SpUtil.saveSP(SPConstant.SP_USER_BASE, "null");
                SpUtil.saveSP(SPConstant.SP_USER_AND_EQUIPMENT, "null");
                //跳转到 登陆界面
                startActivity(new Intent(this, SelectActivity.class));
                //跳转后消亡 本calss WeatherIndexActivity
                finish();
            }
        } else {
            String httpError = response.getHeaders().get("X-Ca-Error-Message") + SdkConstant.CLOUDAPI_LF + SdkConstant.CLOUDAPI_LF;
            Log.i("用户验证是否失效,错误原因：", httpError);
            EventBus.getDefault().post(new WeatherIndexEvent(WeatherIndexEvent.DO_SHOW_ERROR, httpError));
        }

    }


    /**
     * 更新界面
     */
    private void doUpdateUI() {
        if(null!=userAndEquipment){
            if (null != userAndEquipment.getEquipment()) { //判断设备信息

                mEquipmentSNTv.setText(userAndEquipment.getEquipment().getEquipmentSN());
                mEquipmentTypeTv.setText(userAndEquipment.getEquipment().getEquipmentType());
                mEquipmentSetText.setText("已绑定");
                mEquipmentContainerCl.setVisibility(View.VISIBLE);//显示
                mEquipmentContainerCl.setEnabled(true);

            } else {
                mEquipmentSNTv.setText("");
                mEquipmentTypeTv.setText("");
                mEquipmentSetText.setText("点击绑定设备");
                mEquipmentContainerCl.setVisibility(View.VISIBLE);//显示
                mEquipmentContainerCl.setEnabled(true);
            }
            if (null != userAndEquipment.getCityBase()) { //判断城市
                mCitySetTv.setText("您所在的城市");
                mCityNameTv.setText(userAndEquipment.getCityBase().getCityName());
                mcityContainerLl.setVisibility(View.VISIBLE);
                mcityContainerLl.setEnabled(true);
            } else {
                if (null == userAndEquipment.getEquipment()) {
                    mCitySetTv.setText("您还未绑定设备");
                    mCityNameTv.setText("");
                    mcityContainerLl.setVisibility(View.GONE);
                    mcityContainerLl.setEnabled(false);
                } else {
                    mCitySetTv.setText("您还未设置城市 点击设置");
                    mCityNameTv.setText("");
                    mcityContainerLl.setVisibility(View.VISIBLE);
                    mcityContainerLl.setEnabled(true);
                }
            }
        }else{

            mEquipmentSNTv.setText("");
            mEquipmentTypeTv.setText("");
            mEquipmentSetText.setText("点击绑定设备");
            mEquipmentContainerCl.setVisibility(View.VISIBLE);//显示
            mEquipmentContainerCl.setEnabled(true);

            mCitySetTv.setText("您还未绑定设备");
            mCityNameTv.setText("");
            mcityContainerLl.setVisibility(View.GONE);
            mcityContainerLl.setEnabled(false);
        }

        weatherMulti.setState(MultiStateView.ContentState.CONTENT);

    }

    /**
     * 查询APP更新
     */
    private void doUpdateConfirm() {
        //???判断当前手机网络 返回结果aaaa
        boolean isConnected = true;
        //后台处理

    }

    /**
     * 弹出错误提示
     *
     * @param errorMassage
     */
    private void doShowError(String errorMassage) {
        weatherMulti.setState(MultiStateView.ContentState.CONTENT);
        Toast.makeText(this, errorMassage, Toast.LENGTH_SHORT).show();
    }
    /**
     * 弹出提示
     *
     * @param
     */
    private void doShowMessage(String massage) {
        weatherMulti.setState(MultiStateView.ContentState.CONTENT);
        Toast.makeText(this, massage, Toast.LENGTH_SHORT).show();
    }



    /**
     * 弹出扫码,绑定设备
     */
    private void bindingEquipment(){
        startActivityForResult(new Intent(this, CaptureActivity.class), BARCODE_REQUEST_CODE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case BARCODE_REQUEST_CODE://调用扫码结果
                doBarCodeResult(resultCode, data);
                break;
            case BARCODE_REQUEST_SETCITY_CODE://调用设置城市结果
                doSetCityResult(resultCode, data);
                break;
            case BARCODE_REQUEST_EASYLINK_CODE://一键上网页面返回后的处理
               //处理从一键上网返回时候需要处理的事，暂时没想好怎么处理
                Log.i("一键上网：","退出一键上网");
                break;
        }



        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 二维码扫描回调结果处理
     */
    private void doBarCodeResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            if (BuildConfig.DEBUG) {
                Log.i("扫描结果：",scanResult);
                Toast.makeText(this,"扫描结果："+scanResult,Toast.LENGTH_SHORT).show();
            }
            EventBus.getDefault().post(new WeatherIndexEvent(WeatherIndexEvent.DO_FOB_BAR_CODE,scanResult));


        } else {
            Toast.makeText(this,"扫描已取消",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 用户-设备绑定过程-访问网络
     * @param equipmentId
     */
    private void userAndEquipmentObj(String equipmentId)
    {
        BaseApp.getHttpApiClient_zumelAPPInterface().UserAndEquipmentObj(userBase.getUserId(),equipmentId,new ApiCallback() {
            @Override
            public void onFailure(ApiRequest request, Exception e) {
                EventBus.getDefault().post(new WeatherIndexEvent(WeatherIndexEvent.DO_SHOW_ERROR, e.getMessage()));
                e.printStackTrace();
            }

            @Override
            public void onResponse(ApiRequest request, ApiResponse response) {
                doUserAndEquipmentObjResponse(response);
            }
        });
    }

    /**
     * 用户绑定设备结果处理
     * @param response
     */
    private  void doUserAndEquipmentObjResponse(ApiResponse response)
    {
        if (200 == response.getCode()) {
            String jsonBody = new String(response.getBody(), SdkConstant.CLOUDAPI_ENCODING);
            Log.i("用户绑定设备成功后的返回---：", jsonBody);
            UserAndEquipmentRequest userAndEquipmentRequest = GsonUtil.jsonToBean(jsonBody, UserAndEquipmentRequest.class);
            if (0 == userAndEquipmentRequest.getRequestCode()) {
//                Log.i("用户登录是否失效:", "有效的登录");
//
                //存储到缓存
                SpUtil.saveSP(SPConstant.SP_USER_AND_EQUIPMENT,GsonUtil.beanToJson(userAndEquipmentRequest.getUserAndEquipment()));
                userAndEquipment=userAndEquipmentRequest.getUserAndEquipment().get(0);
                EventBus.getDefault().post(new WeatherIndexEvent(WeatherIndexEvent.DO_BOF_USER_AND_EQUIPMENT_Y,userAndEquipmentRequest.getRequestMessage()));
            } else {

                EventBus.getDefault().post(new WeatherIndexEvent(WeatherIndexEvent.DO_BOF_USER_AND_EQUIPMENT_N,userAndEquipmentRequest.getRequestMessage()));
            }
        } else {

            String httpError = response.getHeaders().get("X-Ca-Error-Message") + SdkConstant.CLOUDAPI_LF + SdkConstant.CLOUDAPI_LF;
            Log.i("用户登录是否失效,错误原因：", httpError);
            EventBus.getDefault().post(new WeatherIndexEvent(WeatherIndexEvent.DO_SHOW_ERROR, httpError));
        }
    }

    /**
     * 跳转到城市设置页面
     */
    private void toSetCity(){
        startActivityForResult(new Intent(this, SetCityActivity.class), BARCODE_REQUEST_SETCITY_CODE);
    }

    private void doSetCityResult(int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String cityAreaId = bundle.getString("cityAreaId");
            String cityName = bundle.getString("cityName");
            if (BuildConfig.DEBUG) {
                Log.i("选择城市编码返回结果：",cityAreaId);
                Log.i("选择城市名称返回结果：",cityName);
            }

            setCityHttp(userBase.getUserId(),cityAreaId,userAndEquipment.getUserAndEquipmentId());
        } else {
            Toast.makeText(this,"取消选择城市",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 用户设置城市+定制天气过程-访问网络
     * @param
     */
    private void setCityHttp(String userId,String cityAreaId,String userAndEquipmentId)
    {
        BaseApp.getHttpApiClient_zumelAPPInterface().setCity(userId,cityAreaId,userAndEquipmentId,new ApiCallback() {
            @Override
            public void onFailure(ApiRequest request, Exception e) {
                EventBus.getDefault().post(new WeatherIndexEvent(WeatherIndexEvent.DO_SHOW_ERROR, e.getMessage()));
                e.printStackTrace();
            }

            @Override
            public void onResponse(ApiRequest request, ApiResponse response) {
                doSetCityResponse(response);
            }
        });
    }
    private  void doSetCityResponse(ApiResponse response)
    {
        if (200 == response.getCode()) {
            String jsonBody = new String(response.getBody(), SdkConstant.CLOUDAPI_ENCODING);
            Log.i("用户设置城市和定制天气信息成功后的返回---：", jsonBody);

            SetCityBaseRequest setCityBaseRequest = GsonUtil.jsonToBean(jsonBody, SetCityBaseRequest.class);
            if (0 == setCityBaseRequest.getRequestCode()) {


                String jsonUserAndEquipment = SpUtil.getSP(SPConstant.SP_USER_AND_EQUIPMENT, String.class);
                ArrayList<UserAndEquipment> userAndEquipmentList = GsonUtil.jsonArrayToList(jsonUserAndEquipment, UserAndEquipment.class);
                userAndEquipmentList.get(0).setCityBase(setCityBaseRequest.getCityBase());
                //存储到缓存
                SpUtil.saveSP(SPConstant.SP_USER_AND_EQUIPMENT,GsonUtil.beanToJson(userAndEquipmentList));

                userAndEquipment=userAndEquipmentList.get(0);
                EventBus.getDefault().post(new WeatherIndexEvent(WeatherIndexEvent.DO_UPDATE_UI));
            } else {

                EventBus.getDefault().post(new WeatherIndexEvent(WeatherIndexEvent.DO_BOF_USER_AND_EQUIPMENT_N,setCityBaseRequest.getRequestMessage()));
            }
        } else {
            String httpError = response.getHeaders().get("X-Ca-Error-Message") + SdkConstant.CLOUDAPI_LF + SdkConstant.CLOUDAPI_LF;
            Log.i("设置城市+定制天气失败原因：", httpError);
            EventBus.getDefault().post(new WeatherIndexEvent(WeatherIndexEvent.DO_SHOW_ERROR, httpError));
        }
    }

    /**
     * 跳转到一键上网设置页面
     */
    private void toEsptouchDemoActivity(){
        startActivityForResult(new Intent(this, EsptouchDemoActivity.class), BARCODE_REQUEST_EASYLINK_CODE);
    }




    /**
     * 事件
     */
    private static class WeatherIndexEvent {
        /**
         * 参数判断完成,开始访问后台
         */
        private static final int DO_UPDATE_CONFIRM = 0X01;
        private static final int DO_UPDATE_UI = 0X02;
        private static final int DO_LOGIN_YZ = 0X03;
        private static final int DO_SHOW_ERROR = 0X04;
        private static final int DO_GET_USER_AND_EQUIPMENT = 0X05;
        private static final int DO_FOB_BAR_CODE= 0X06;//前台-》后台 扫码结果传到后台处理
        private static final int DO_BOF_USER_AND_EQUIPMENT_Y= 0X07;//
        private static final int DO_BOF_USER_AND_EQUIPMENT_N= 0X08;//
        private static final int DO_BOF_SET_CITY_CODE= 0X09;//后台-》前台 设置城市的返回
        final int code;
        String massage;
        String userTel;
        String userPassWord;

        WeatherIndexEvent(int code) {
            this.code = code;
        }

        WeatherIndexEvent(int code, String massage) {
            this.code = code;
            this.massage = massage;
        }

        WeatherIndexEvent(int code, String userTel,String userPassWord) {
            this.code = code;
            this.userTel = userTel;
            this.userPassWord = userPassWord;
        }

    }
}
