package com.example.aiden.xtapp.entity.json;

import com.example.aiden.xtapp.entity.base.UserAndEquipment;
import com.example.aiden.xtapp.entity.base.UserBase;

import java.util.List;

/**
 * 用户手机号登陆
 * Created by chenly on 2017/11/4.
 */

public class LoginTelRequest extends BaseRequest{
    private UserBase UserBase;
    private List<UserAndEquipment> UserAndEquipment;

    public com.example.aiden.xtapp.entity.base.UserBase getUserBase() {
        return UserBase;
    }

    public void setUserBase(com.example.aiden.xtapp.entity.base.UserBase userBase) {
        UserBase = userBase;
    }

    public List<com.example.aiden.xtapp.entity.base.UserAndEquipment> getUserAndEquipment() {
        return UserAndEquipment;
    }

    public void setUserAndEquipment(List<com.example.aiden.xtapp.entity.base.UserAndEquipment> userAndEquipment) {
        UserAndEquipment = userAndEquipment;
    }
}
