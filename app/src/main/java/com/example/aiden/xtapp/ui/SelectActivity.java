package com.example.aiden.xtapp.ui;

import android.content.Intent;
import android.view.View;

import com.example.aiden.xtapp.R;
import com.example.aiden.xtapp.base.BaseActivity;

import butterknife.OnClick;

/**
 * 登录和注册选择页
 * Created by chenly on 2017/11/19.
 */

public class SelectActivity extends BaseActivity{
    @Override
    protected int getLayoutId() {
        return R.layout.activity_select;
    }

    @OnClick({R.id.button_select_login,
    R.id.button_select_registration})
    public void buttonOnClick(View v){
        switch (v.getId()){
            case R.id.button_select_login:
                jumpLoginActivity();
                break;
            case R.id.button_select_registration:
                jumpRegistrationActivity();
                break;
            default:
                break;
        }
    }

    private void jumpLoginActivity(){
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

    private void jumpRegistrationActivity(){
        startActivity(new Intent(this,RegistrationActivity.class));
        finish();
    }
}
