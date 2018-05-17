package com.example.aiden.xtapp.entity.json;

/**
 * Created by chenly on 2017/11/4.
 */

public class BaseRequest {
    private int RequestCode;
    private String RequestMessage;

    public int getRequestCode() {
        return RequestCode;
    }

    public void setRequestCode(int requestCode) {
        RequestCode = requestCode;
    }

    public String getRequestMessage() {
        return RequestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        RequestMessage = requestMessage;
    }
}
