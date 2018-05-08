package com.example.aiden.xtapp.entity.json;

import com.example.aiden.xtapp.entity.base.UserMemorandumSet;

/**
 * Created by ldn on 2018/1/8.
 */

public class SetMemorandumRequest  extends BaseRequest{
    private UserMemorandumSet UserMemorandumSet;

    public UserMemorandumSet getUserMemorandumSet() {
        return UserMemorandumSet;
    }

    public void setUserMemorandumSet(UserMemorandumSet userMemorandumSet) {
        UserMemorandumSet = userMemorandumSet;
    }
}
