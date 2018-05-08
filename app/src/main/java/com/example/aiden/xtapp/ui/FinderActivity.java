package com.example.aiden.xtapp.ui;

import android.content.Intent;
import android.view.View;

import com.example.aiden.xtapp.R;
import com.example.aiden.xtapp.base.BaseActivity;

import butterknife.OnClick;

/**
 * 发现页
 */
public class FinderActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_finder;
    }


    /**
     * 底部按钮事件
     * @param v
     */
    @OnClick({R.id.button_finder_home,
            R.id.button_finder_notifications
    })
    public void bottomMenuClick(View v){
        switch (v.getId()){
            case R.id.button_finder_home://跳转home
                startActivity(new Intent(v.getContext(),Home2Activity.class));
                break;
            case R.id.button_finder_notifications://跳转发现
                startActivity(new Intent(v.getContext(),MyActivity.class));
                break;

        }
    }
}
