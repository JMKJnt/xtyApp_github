package com.example.aiden.xtapp.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;


import com.alibaba.cloudapi.client.HttpApiClient_zumelAPPInterface;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;
import com.example.aiden.xtapp.BuildConfig;

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


    }

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
