package com.example.aiden.xtapp.base;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;


import com.alibaba.cloudapi.client.HttpApiClient_zumelAPPInterface;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.register.GcmRegister;
import com.alibaba.sdk.android.push.register.HuaWeiRegister;
import com.alibaba.sdk.android.push.register.MiPushRegister;
import com.example.aiden.xtapp.BuildConfig;
import com.example.aiden.xtapp.ui.XtListDemoActivity;

import org.xutils.x;


/**
 * @auther by yushilei.
 * @time 2016/10/25-09:56
 * @desc
 */

public class BaseApp extends Application {
    private static final String TAG = "BaseApp";
    static Context mAppContext;
    static Resources mResources;
    public static HttpApiClient_zumelAPPInterface httpApiClient_zumelAPPInterface;

//    private static final String TAG = "Init";
    private static XtListDemoActivity _XtListDemoActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppContext = getApplicationContext();
        mResources = getResources();
        creatTestGroup();
        //数据库初始化
        x.Ext.init(this);
        if (BuildConfig.DEBUG) {
            x.Ext.setDebug(false); // 开启debug会影响性能
        } else {
            x.Ext.setDebug(false);
        }
        initCloudChannel(this);


    }

    /**
     * 初始化云推送通道
     * @param applicationContext
     */
    private void initCloudChannel(final Context applicationContext) {
        // 创建notificaiton channel
        this.createNotificationChannel();
        PushServiceFactory.init(applicationContext);
        final CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.i(TAG, "init cloudchannel success");
                setConsoleText("init cloudchannel success","");
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.e(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
                setConsoleText("init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage,"");
            }
        });

        MiPushRegister.register(applicationContext, "XIAOMI_ID", "XIAOMI_KEY"); // 初始化小米辅助推送
        HuaWeiRegister.register(applicationContext); // 接入华为辅助推送
        GcmRegister.register(applicationContext, "send_id", "application_id"); // 接入FCM/GCM初始化推送
    }

//    public static void setMainActivity(BaseApp baseApp) {
//        mainActivity = activity;
//    }

    public static void set_XtListDemoActivity(XtListDemoActivity _XtListDemoActivity) {
        BaseApp._XtListDemoActivity = _XtListDemoActivity;
    }

    public static void setConsoleText(String text,String text2) {
        if (_XtListDemoActivity != null && text != null) {
            _XtListDemoActivity.appendConsoleText(text,text2);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // 通知渠道的id
            String id = "1";
            // 用户可以看到的通知渠道的名字.
            CharSequence name = "notification channel";
            // 用户可以看到的通知渠道的描述
            String description = "notification description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // 配置通知渠道的属性
            mChannel.setDescription(description);
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            // 设置通知出现时的震动（如果 android 设备支持的话）
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            //最后在notificationmanager中创建该通知渠道
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }


    //------------------------------------------------------------------------
    private void creatTestGroup() {
        HttpClientBuilderParams httpParam = new HttpClientBuilderParams();
        httpParam.setAppKey("24682826");
        httpParam.setAppSecret("317bc3385b94893853f45f2a100157ce");

//        /**
//         * 以HTTPS方式提交请求
//         * 本DEMO采取忽略证书的模式,目的是方便大家的调试
//         * 为了安全起见,建议采取证书校验方式
//         */
//        X509TrustManager xtm = new X509TrustManager() {
//            @Override
//            public void checkClientTrusted(X509Certificate[] chain, String authType) {
//            }
//
//            @Override
//            public void checkServerTrusted(X509Certificate[] chain, String authType) {
//            }
//
//            @Override
//            public X509Certificate[] getAcceptedIssuers() {
//                X509Certificate[] x509Certificates = new X509Certificate[0];
//                return x509Certificates;
//            }
//        };
//
//        SSLContext sslContext = null;
//        try {
//            sslContext = SSLContext.getInstance("SSL");
//            sslContext.init(null, new TrustManager[]{xtm}, new SecureRandom());
//
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        } catch (KeyManagementException e) {
//            throw new RuntimeException(e);
//        }
//        HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
//            @Override
//            public boolean verify(String hostname, SSLSession session) {
//                return true;
//            }
//        };
//
//        httpParam.setSslSocketFactory(sslContext.getSocketFactory());
//        httpParam.setX509TrustManager(xtm);
//        httpParam.setHostnameVerifier(DO_NOT_VERIFY);

        httpApiClient_zumelAPPInterface=HttpApiClient_zumelAPPInterface.getInstance();
        httpApiClient_zumelAPPInterface.init(httpParam);

    }

    public static synchronized BaseApp context() {
        return (BaseApp) mAppContext;
    }

    public static Resources resource() {
        return mResources;
    }

    public static HttpApiClient_zumelAPPInterface getHttpApiClient_zumelAPPInterface() {
        return httpApiClient_zumelAPPInterface;
    }
}
