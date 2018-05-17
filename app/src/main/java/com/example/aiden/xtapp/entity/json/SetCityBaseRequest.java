package com.example.aiden.xtapp.entity.json;

import com.example.aiden.xtapp.entity.base.CityBase;

/**
 * Created by ldn on 2017/11/12.
 */

public class SetCityBaseRequest extends BaseRequest {
    private CityBase CityBase;

    public com.example.aiden.xtapp.entity.base.CityBase getCityBase() {
        return CityBase;
    }

    public void setCityBase(com.example.aiden.xtapp.entity.base.CityBase cityBase) {
        CityBase = cityBase;
    }
}
