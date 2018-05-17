package com.example.aiden.xtapp.entity.json;

import java.util.List;

/** 用户绑定设备关系
 * 含 设备列表（有设置的城市）
 * Created by ldn on 2017/11/10.
 */

public class UserAndEquipmentRequest extends BaseRequest{

    private List<com.example.aiden.xtapp.entity.base.UserAndEquipment> UserAndEquipment;



    public List<com.example.aiden.xtapp.entity.base.UserAndEquipment> getUserAndEquipment() {
        return UserAndEquipment;
    }

    public void setUserAndEquipment(List<com.example.aiden.xtapp.entity.base.UserAndEquipment> userAndEquipment) {
        UserAndEquipment = userAndEquipment;
    }
}
