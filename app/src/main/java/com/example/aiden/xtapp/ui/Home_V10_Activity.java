package com.example.aiden.xtapp.ui;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.example.aiden.xtapp.entity.json.SetMemorandumRequest;
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


/**
 * Created by ldn on 2018/3/17.
 */
public class Home_V10_Activity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final int BARCODE_REQUEST_CODE = 0x123;//二维码扫描请求码
    private static final int BARCODE_REQUEST_SETCITY_CODE = 0x124;//选择城市后回调
    private static final int BARCODE_REQUEST_EASYLINK_CODE = 0x125;//一键上网页面的回调
    private static final int BARCODE_REQUEST_MEMORANDUM_CODE = 0x126;//设置备忘提醒页面的回调
    //整体布局
    @BindView(R.id.weather_multi)
    MultiStateView weatherMulti;

    //***********************账号-设备绑定区*****************************
    //  设备绑定 布局
    @BindView(R.id.bind_layout_id)
    LinearLayout bind_layout_id;
    //  布局区 文字提示
    @BindView(R.id.bind_text)
    TextView bind_text;
    //  布局区 绑定按钮
    @BindView(R.id.bind_button)
    Button bind_button;

    //***********************账号布局区*****************************
    //用户账号
//    @BindView(R.id.text_user_id)
//    TextView text_user_id;

    //***********************设备布局区*****************************
    //设备区域布局
    @BindView(R.id.layout_equipment)
    LinearLayout layout_equipment;
    //设备型号
    @BindView(R.id.textView_weatherIndex_equipmentType)
    TextView mEquipmentTypeTv;

//    //设备区域布局
//    @BindView(R.id.button_weatherIndex_binding)
//    LinearLayout mEquipmentContainerCl;
//
//    //是否绑定设备 textView文字
//    @BindView(R.id.textView_weatherIndex_equipmentEQ_text)
//    TextView mEquipmentSetText;
//
//    //设备出厂SN
//    @BindView(R.id.textView_weatherIndex_equipmentSN)
//    TextView mEquipmentSNTv;

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
    //*************************end*******************************

    //***********************备忘录布局区*****************************
    //备忘录---布局---
    @BindView(R.id.linearLayout_note)
    LinearLayout linearLayout_note;
    //备忘录---按钮---
    @BindView(R.id.button_memorandum_set)
    TextView button_memorandum_set;
    //*************************end*******************************

//    //一键上网---按钮---
    @BindView(R.id.button_weatherIndex_easylink)
    TextView mEasylink;


    //左侧菜单
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    //*************************end*******************************


    private UserBase userBase;
    private UserAndEquipment userAndEquipment;


    //当前Activity对应的layout
    @Override
    protected int getLayoutId() {
        //加载一个包含布局：1 主页 2 左侧滑动菜单
        return R.layout.activity_home2;
    }

    //开启事件监听
    @Override
    protected void enableEventBus() {
        setEnableEventBus(true);
    }

    //页面加载完后执行
    @Override
    protected void onInitViewAfterOnCreate() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
                httpLogin(event.userTel, event.userPassWord);
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

    @OnClick({
            R.id.bind_button,
            R.id.button_weatherIndex_selectCity,
            R.id.button_memorandum_set
    })
    public void buttonClick(View v) {
        switch (v.getId()) {
            case R.id.bind_button://绑定设备
                bindingEquipment();
                break;
            case R.id.button_weatherIndex_selectCity://设置城市
                toSetCity();
                break;
            case R.id.button_memorandum_set://设置备忘录
                toMemorandumActivity();
                break;
        }
    }


    /**
     * 查询本地 用户数据
     */
    private void doGetUserBase() {
        String jsonUserBase = SpUtil.getSP(SPConstant.SP_USER_BASE, String.class);
        if (!"null".equals(jsonUserBase)) {
            userBase = GsonUtil.jsonToBean(jsonUserBase, UserBase.class);
            //调用《《==接口1==》》（用户登陆是否过期失效）返回结果cccc
            EventBus.getDefault().post(new WeatherIndexEvent(WeatherIndexEvent.DO_LOGIN_YZ, userBase.getUserTel(), userBase.getUserPassWord()));

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
                } catch (Exception ex) {
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
     *
     * @param response
     * @return
     * @throws IOException
     */
    private void doLoginTelData(ApiResponse response) {
        if (200 == response.getCode()) {
            String bodyString = new String(response.getBody(), SdkConstant.CLOUDAPI_ENCODING);
            LoginTelRequest loginTelRequest = GsonUtil.jsonToBean(bodyString, LoginTelRequest.class);
            if (0 == loginTelRequest.getRequestCode()) {
                Log.i("登录验证成功:", "用户" + loginTelRequest.getUserBase().getUserTel());
                //存储到缓存
                SpUtil.saveSP(SPConstant.SP_USER_BASE, GsonUtil.beanToJson(loginTelRequest.getUserBase()));
                SpUtil.saveSP(SPConstant.SP_USER_AND_EQUIPMENT, GsonUtil.beanToJson(loginTelRequest.getUserAndEquipment()));
                userBase = loginTelRequest.getUserBase();
                if (0 != loginTelRequest.getUserAndEquipment().size()) {
                    userAndEquipment = loginTelRequest.getUserAndEquipment().get(0);
                }
                EventBus.getDefault().post(new WeatherIndexEvent(WeatherIndexEvent.DO_UPDATE_UI));
            } else {
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
        if (null != userAndEquipment) {

            bind_layout_id.setVisibility(View.GONE);
            bind_layout_id.setEnabled(false);

            layout_equipment.setVisibility(View.VISIBLE);
            layout_equipment.setEnabled(true);

            mcityContainerLl.setVisibility(View.VISIBLE);
            mcityContainerLl.setEnabled(true);

            linearLayout_note.setVisibility(View.VISIBLE);
            linearLayout_note.setEnabled(true);

            if (null != userAndEquipment.getEquipment()) { //判断设备信息
                if("DEMO104".equals(userAndEquipment.getEquipment().getEquipmentType()))
                {
                    mEquipmentTypeTv.setText("橘猫科技信息镜");
                }else
                {
                    mEquipmentTypeTv.setText(userAndEquipment.getEquipment().getEquipmentType());
                }

            } else {
                mEquipmentTypeTv.setText("");
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
            if(null != userAndEquipment.getUserMemorandumSet())
            {
                //血糖仪项目测试用
//                button_memorandum_set.setVisibility(View.GONE);
//                button_memorandum_set.setEnabled(false);
//                mEasylink.setVisibility(View.GONE);
//                mEasylink.setEnabled(false);

                button_memorandum_set.setText(userAndEquipment.getUserMemorandumSet().getContentStr());
            }else{

            }
        } else {

            bind_layout_id.setVisibility(View.VISIBLE);
            bind_layout_id.setEnabled(true);

            layout_equipment.setVisibility(View.GONE);
            layout_equipment.setEnabled(false);

            mcityContainerLl.setVisibility(View.GONE);
            mcityContainerLl.setEnabled(false);

            linearLayout_note.setVisibility(View.GONE);
            linearLayout_note.setEnabled(false);


//            bind_text.setVisibility(View.VISIBLE);
//            bind_text.setEnabled(true);
//
//            bind_button.setVisibility(View.VISIBLE);
//            bind_button.setEnabled(true);

            mEquipmentTypeTv.setText("");

            mCitySetTv.setText("您还未绑定设备");
            mCityNameTv.setText("");

        }
//        if(null != userBase)
//        {
//            text_user_id.setText(userBase.getUserName());
//        }

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
    private void bindingEquipment() {
        startActivityForResult(new Intent(this, CaptureActivity.class), BARCODE_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BARCODE_REQUEST_CODE://调用扫码结果
                doBarCodeResult(resultCode, data);
                break;
            case BARCODE_REQUEST_SETCITY_CODE://调用设置城市结果
                doSetCityResult(resultCode, data);
                break;
            case BARCODE_REQUEST_EASYLINK_CODE://一键上网页面返回后的处理
                //处理从一键上网返回时候需要处理的事，暂时没想好怎么处理
                Log.i("一键上网：", "退出一键上网");
                break;
            case BARCODE_REQUEST_MEMORANDUM_CODE://备忘录设置返回结果
                doSetMemorandumResult(resultCode, data);
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
                Log.i("扫描结果：", scanResult);
                Toast.makeText(this, "扫描结果：" + scanResult, Toast.LENGTH_SHORT).show();
            }
            EventBus.getDefault().post(new WeatherIndexEvent(WeatherIndexEvent.DO_FOB_BAR_CODE, scanResult));


        } else {
            Toast.makeText(this, "扫描已取消", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 用户-设备绑定过程-访问网络
     *
     * @param equipmentId
     */
    private void userAndEquipmentObj(String equipmentId) {
        BaseApp.getHttpApiClient_zumelAPPInterface().UserAndEquipmentObj(userBase.getUserId(), equipmentId, new ApiCallback() {
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
     *
     * @param response
     */
    private void doUserAndEquipmentObjResponse(ApiResponse response) {
        if (200 == response.getCode()) {
            String jsonBody = new String(response.getBody(), SdkConstant.CLOUDAPI_ENCODING);
            Log.i("用户绑定设备成功后的返回---：", jsonBody);
            UserAndEquipmentRequest userAndEquipmentRequest = GsonUtil.jsonToBean(jsonBody, UserAndEquipmentRequest.class);
            if (0 == userAndEquipmentRequest.getRequestCode()) {
//                Log.i("用户登录是否失效:", "有效的登录");
//
                //存储到缓存
                SpUtil.saveSP(SPConstant.SP_USER_AND_EQUIPMENT, GsonUtil.beanToJson(userAndEquipmentRequest.getUserAndEquipment()));
                userAndEquipment = userAndEquipmentRequest.getUserAndEquipment().get(0);
                EventBus.getDefault().post(new WeatherIndexEvent(WeatherIndexEvent.DO_BOF_USER_AND_EQUIPMENT_Y, userAndEquipmentRequest.getRequestMessage()));
            } else {

                EventBus.getDefault().post(new WeatherIndexEvent(WeatherIndexEvent.DO_BOF_USER_AND_EQUIPMENT_N, userAndEquipmentRequest.getRequestMessage()));
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
    private void toSetCity() {
        startActivityForResult(new Intent(this, SetCityActivity.class), BARCODE_REQUEST_SETCITY_CODE);
    }

    private void doSetCityResult(int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String cityAreaId = bundle.getString("cityAreaId");
            String cityName = bundle.getString("cityName");
            if (BuildConfig.DEBUG) {
                Log.i("选择城市编码返回结果：", cityAreaId);
                Log.i("选择城市名称返回结果：", cityName);
            }

            setCityHttp(userBase.getUserId(), cityAreaId, userAndEquipment.getUserAndEquipmentId());
        } else {
            Toast.makeText(this, "取消选择城市", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 用户设置城市+定制天气过程-访问网络
     *
     * @param
     */
    private void setCityHttp(String userId, String cityAreaId, String userAndEquipmentId) {
        BaseApp.getHttpApiClient_zumelAPPInterface().setCity(userId, cityAreaId, userAndEquipmentId, new ApiCallback() {
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

    private void doSetCityResponse(ApiResponse response) {
        if (200 == response.getCode()) {
            String jsonBody = new String(response.getBody(), SdkConstant.CLOUDAPI_ENCODING);
            Log.i("用户设置城市和定制天气信息成功后的返回---：", jsonBody);

            SetCityBaseRequest setCityBaseRequest = GsonUtil.jsonToBean(jsonBody, SetCityBaseRequest.class);
            if (0 == setCityBaseRequest.getRequestCode()) {


                String jsonUserAndEquipment = SpUtil.getSP(SPConstant.SP_USER_AND_EQUIPMENT, String.class);
                ArrayList<UserAndEquipment> userAndEquipmentList = GsonUtil.jsonArrayToList(jsonUserAndEquipment, UserAndEquipment.class);
                userAndEquipmentList.get(0).setCityBase(setCityBaseRequest.getCityBase());
                //存储到缓存
                SpUtil.saveSP(SPConstant.SP_USER_AND_EQUIPMENT, GsonUtil.beanToJson(userAndEquipmentList));

                userAndEquipment = userAndEquipmentList.get(0);
                EventBus.getDefault().post(new WeatherIndexEvent(WeatherIndexEvent.DO_UPDATE_UI));
            } else {

                EventBus.getDefault().post(new WeatherIndexEvent(WeatherIndexEvent.DO_BOF_USER_AND_EQUIPMENT_N, setCityBaseRequest.getRequestMessage()));
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
    private void toEsptouchDemoActivity() {
        startActivityForResult(new Intent(this, EsptouchDemoActivity.class), BARCODE_REQUEST_EASYLINK_CODE);
    }

    /**
     * 跳转到留言备忘录设置页面
     */
    private void toMemorandumActivity() {
        Intent intent = new Intent(this,MemorandumActivity.class);
        intent.putExtra("UserMemorandumSetStr",userAndEquipment.getUserMemorandumSet().getContentStr());
        startActivityForResult(intent, BARCODE_REQUEST_MEMORANDUM_CODE);
    }

    /**
     * SetCityActivity 设置备忘录完成后的回调方法
     */
    private void doSetMemorandumResult(int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String contentStr = bundle.getString("contentStr");
            String content_control_YN = bundle.getString("content_control_YN");
            if (BuildConfig.DEBUG) {
                Log.i("用户设置备忘录内容-返回结果：", contentStr);
                Log.i("备忘录的开启状态-返回结果：", content_control_YN);
            }

            setMemorandumHttp(userBase.getUserId(), userAndEquipment.getUserAndEquipmentId(),contentStr,content_control_YN);
        } else {
            Toast.makeText(this, "退出设置备忘录页面", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 用户设置备忘录-访问网络
     *
     * @param
     */
    private void setMemorandumHttp(String userId, String userAndEquipmentId,String contentStr,String content_control_YN) {
        BaseApp.getHttpApiClient_zumelAPPInterface().setMemorandum(userId, userAndEquipmentId,contentStr,content_control_YN ,new ApiCallback() {
            @Override
            public void onFailure(ApiRequest request, Exception e) {
                EventBus.getDefault().post(new WeatherIndexEvent(WeatherIndexEvent.DO_SHOW_ERROR, e.getMessage()));
                e.printStackTrace();
            }

            @Override
            public void onResponse(ApiRequest request, ApiResponse response) {
                doSetMemorandumResponse(response);
            }
        });
    }

    /**
     * 访问服务设置用户备忘录业务 后的 结果处理
     * @param response
     */
    private void doSetMemorandumResponse(ApiResponse response) {
        if (200 == response.getCode()) {
            String jsonBody = new String(response.getBody(), SdkConstant.CLOUDAPI_ENCODING);
            Log.i("用户设置备忘录成功后的返回---：", jsonBody);

            SetMemorandumRequest setMemorandumRequest = GsonUtil.jsonToBean(jsonBody, SetMemorandumRequest.class);
            if (0 == setMemorandumRequest.getRequestCode()||1 == setMemorandumRequest.getRequestCode()) {


                String jsonUserAndEquipment = SpUtil.getSP(SPConstant.SP_USER_AND_EQUIPMENT, String.class);
                ArrayList<UserAndEquipment> userAndEquipmentList = GsonUtil.jsonArrayToList(jsonUserAndEquipment, UserAndEquipment.class);
                userAndEquipmentList.get(0).setUserMemorandumSet(setMemorandumRequest.getUserMemorandumSet());
                //存储到缓存
                SpUtil.saveSP(SPConstant.SP_USER_AND_EQUIPMENT, GsonUtil.beanToJson(userAndEquipmentList));

                userAndEquipment = userAndEquipmentList.get(0);
                EventBus.getDefault().post(new WeatherIndexEvent(WeatherIndexEvent.DO_UPDATE_UI));
            } else {

                EventBus.getDefault().post(new WeatherIndexEvent(WeatherIndexEvent.DO_BOF_USER_AND_EQUIPMENT_N, setMemorandumRequest.getRequestMessage()));
            }
        } else {
            String httpError = response.getHeaders().get("X-Ca-Error-Message") + SdkConstant.CLOUDAPI_LF + SdkConstant.CLOUDAPI_LF;
            Log.i("设置备忘录失败原因：", httpError);
            EventBus.getDefault().post(new WeatherIndexEvent(WeatherIndexEvent.DO_SHOW_ERROR, httpError));
        }
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
        private static final int DO_FOB_BAR_CODE = 0X06;//前台-》后台 扫码结果传到后台处理
        private static final int DO_BOF_USER_AND_EQUIPMENT_Y = 0X07;//
        private static final int DO_BOF_USER_AND_EQUIPMENT_N = 0X08;//
        private static final int DO_BOF_SET_CITY_CODE = 0X09;//后台-》前台 设置城市的返回
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

        WeatherIndexEvent(int code, String userTel, String userPassWord) {
            this.code = code;
            this.userTel = userTel;
            this.userPassWord = userPassWord;
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else
        if (id == R.id.nav_connect) {
            toEsptouchDemoActivity();
        } else if (id == R.id.nav_seting) {
//            startActivity(new Intent(this, XtListDemoActivity.class));
        }  else if (id == R.id.nav_share) {
            Snackbar.make(navigationView, "功能即将上线 敬请期待", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_quit) {
            dialogLogOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void dialogLogOut() {
        AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("退出提示");//设置标题


    /*对话框内容区域的设置提供了多种方法*/
        alertDialogBuilder.setMessage("确定登出?");//设置显示文本
//监听下方button点击事件
        alertDialogBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                logOut();
                dialogInterface.cancel();
            }
        });
        alertDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        //设置对话框是可取消的
        alertDialogBuilder.setCancelable(true);
        AlertDialog dialog=alertDialogBuilder.create();
        dialog.show();

    }

    public void logOut(){
        SpUtil.saveSP(SPConstant.SP_USER_BASE, "null");
        SpUtil.saveSP(SPConstant.SP_USER_AND_EQUIPMENT, "null");
        startActivity(new Intent(this, SelectActivity.class));
        finish();
    }
}

