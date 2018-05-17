package com.example.aiden.xtapp.ui;

import android.content.Intent;
import android.view.View;

import com.example.aiden.xtapp.R;
import com.example.aiden.xtapp.base.BaseActivity;

import butterknife.OnClick;

/**
 * 我的页
 */
public class MyActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my;
    }


    /**
     * 底部按钮事件
     * @param v
     */
    @OnClick({R.id.button_my_home,
            R.id.button_my_dashboard
    })
    public void bottomMenuClick(View v){
        switch (v.getId()){
            case R.id.button_my_home://跳转home
                startActivity(new Intent(v.getContext(),Home2Activity.class));
                break;
            case R.id.button_my_dashboard://跳转发现
                startActivity(new Intent(v.getContext(),FinderActivity.class));
                break;

        }
    }
}
