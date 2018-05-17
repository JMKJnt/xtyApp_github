//
//  Created by  fred on 2017/1/12.
//  Copyright © 2016年 Alibaba. All rights reserved.
//

package com.alibaba.cloudapi.client;

import com.alibaba.cloudapi.sdk.client.HttpApiClient;
import com.alibaba.cloudapi.sdk.enums.HttpMethod;
import com.alibaba.cloudapi.sdk.enums.ParamPosition;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;


public class HttpsApiClient_zumelAPPInterface extends HttpApiClient{
    public final static String HOST = "303f2228e57c485f91735f5683d12fc9-cn-beijing.alicloudapi.com";
    static HttpsApiClient_zumelAPPInterface instance = new HttpsApiClient_zumelAPPInterface();
    public static HttpsApiClient_zumelAPPInterface getInstance(){return instance;}

    public void init(HttpClientBuilderParams httpClientBuilderParams){
        httpClientBuilderParams.setScheme(Scheme.HTTPS);
        httpClientBuilderParams.setHost(HOST);
        super.init(httpClientBuilderParams);
    }



    public void list_xtData(ApiCallback callback) {
        String path = "/app/list_xtData";
        ApiRequest request = new ApiRequest(HttpMethod.GET , path);
        

        sendAsyncRequest(request , callback);
    }
    public void setMemorandum_control_YN(String userId , String userAndEquipmentId , String content_control_YN , ApiCallback callback) {
        String path = "/app/setMemorandum_control_YN";
        ApiRequest request = new ApiRequest(HttpMethod.GET , path);
        request.addParam("userId" , userId , ParamPosition.QUERY , true);
        request.addParam("userAndEquipmentId" , userAndEquipmentId , ParamPosition.QUERY , true);
        request.addParam("content_control_YN" , content_control_YN , ParamPosition.QUERY , true);


        sendAsyncRequest(request , callback);
    }
    public void setMemorandum(String userId , String userAndEquipmentId , String contentStr , String content_control_YN , ApiCallback callback) {
        String path = "/app/setMemorandum";
        ApiRequest request = new ApiRequest(HttpMethod.GET , path);
        request.addParam("userId" , userId , ParamPosition.QUERY , true);
        request.addParam("userAndEquipmentId" , userAndEquipmentId , ParamPosition.QUERY , true);
        request.addParam("contentStr" , contentStr , ParamPosition.QUERY , true);
        request.addParam("content_control_YN" , content_control_YN , ParamPosition.QUERY , true);


        sendAsyncRequest(request , callback);
    }
    public void test001(String msg , ApiCallback callback) {
        String path = "/app/test001";
        ApiRequest request = new ApiRequest(HttpMethod.GET , path);
        request.addParam("msg" , msg , ParamPosition.QUERY , true);


        sendAsyncRequest(request , callback);
    }
    public void userWeatherSetObj(String userId , String userAndEquipmentId , ApiCallback callback) {
        String path = "/app/userWeatherSetObj";
        ApiRequest request = new ApiRequest(HttpMethod.GET , path);
        request.addParam("userId" , userId , ParamPosition.QUERY , true);
        request.addParam("userAndEquipmentId" , userAndEquipmentId , ParamPosition.QUERY , true);


        sendAsyncRequest(request , callback);
    }
    public void UserSignin(String userTel , String userPassWord , String securityCode , ApiCallback callback) {
        String path = "/app/UserSignin";
        ApiRequest request = new ApiRequest(HttpMethod.GET , path);
        request.addParam("userTel" , userTel , ParamPosition.QUERY , true);
        request.addParam("userPassWord" , userPassWord , ParamPosition.QUERY , true);
        request.addParam("securityCode" , securityCode , ParamPosition.QUERY , true);


        sendAsyncRequest(request , callback);
    }
    public void userLoginYZ(String userId , ApiCallback callback) {
        String path = "/app/userLoginYZ";
        ApiRequest request = new ApiRequest(HttpMethod.GET , path);
        request.addParam("userId" , userId , ParamPosition.QUERY , true);


        sendAsyncRequest(request , callback);
    }
    public void eqUserExist(String userTel , ApiCallback callback) {
        String path = "/app/eqUserExist";
        ApiRequest request = new ApiRequest(HttpMethod.GET , path);
        request.addParam("userTel" , userTel , ParamPosition.QUERY , false);


        sendAsyncRequest(request , callback);
    }
    public void UserGetTelSecurityCode(String userTel , ApiCallback callback) {
        String path = "/app/UserGetTelSecurityCode";
        ApiRequest request = new ApiRequest(HttpMethod.GET , path);
        request.addParam("userTel" , userTel , ParamPosition.QUERY , false);


        sendAsyncRequest(request , callback);
    }
    public void UserAndEquipmentObj(String userId , String equipmentId , ApiCallback callback) {
        String path = "/app/UserAndEquipmentObj";
        ApiRequest request = new ApiRequest(HttpMethod.GET , path);
        request.addParam("userId" , userId , ParamPosition.QUERY , true);
        request.addParam("equipmentId" , equipmentId , ParamPosition.QUERY , true);


        sendAsyncRequest(request , callback);
    }
    public void setCity(String userId , String cityBaseId , String userAndEquipmentId , ApiCallback callback) {
        String path = "/app/setCity";
        ApiRequest request = new ApiRequest(HttpMethod.GET , path);
        request.addParam("userId" , userId , ParamPosition.QUERY , true);
        request.addParam("cityBaseId" , cityBaseId , ParamPosition.QUERY , true);
        request.addParam("userAndEquipmentId" , userAndEquipmentId , ParamPosition.QUERY , true);


        sendAsyncRequest(request , callback);
    }
    public void loginTel(String userTel , String userPassWord , ApiCallback callback) {
        String path = "/app/loginTel";
        ApiRequest request = new ApiRequest(HttpMethod.GET , path);
        request.addParam("userTel" , userTel , ParamPosition.QUERY , true);
        request.addParam("userPassWord" , userPassWord , ParamPosition.QUERY , true);


        sendAsyncRequest(request , callback);
    }
}