package com.example.aiden.xtapp.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.aiden.xtapp.util.AppManager;
import com.example.aiden.xtapp.util.AppUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;


/**
 * @auther by yushilei.
 * @time 2016/10/25-09:56
 * @desc
 */

public abstract class BaseActivity extends AppCompatActivity {

    private boolean _isVisible;

    private final String packageName4Umeng = this.getClass().getName();

    private boolean enableEventBus = false;

    private boolean disableHomeAndRecentKey = true;
    /**
     * Activity是否已经 或者即将调用 onSaveInstanceState()
     */
    private boolean isInstanceStateSaved = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        enableHomeRecentKey();

        if (disableHomeAndRecentKey)
            AppUtil.disableHomeAndRecentAppKeyByUserType(this);

        AppManager.getAppManager().addActivity(this);

        onSetDataBeforeSetLayout();

        setContentView(getLayoutId());

        ButterKnife.bind(this);

        onInit(savedInstanceState);

        enableEventBus();

        if (enableEventBus && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        onInitViewAfterOnCreate();

        onInitDataAfterOnCreate();

        _isVisible = true;
    }

    protected void enableHomeRecentKey() {

    }

    public void setDisableHomeAndRecentKey(boolean disableHomeAndRecentKey) {
        this.disableHomeAndRecentKey = disableHomeAndRecentKey;
    }

    protected void enableEventBus() {
    }

    /**
     * 是否激活当前Activity注册EventBus-取消EventBus 操作
     *
     * @param enableEventBus true-激活 false -不激活
     */
    protected void setEnableEventBus(boolean enableEventBus) {
        this.enableEventBus = enableEventBus;
    }

    /**
     * Activity onCreate(savedInstanceState) 方法调用
     */
    protected void onInit(Bundle savedInstanceState) {

    }

    /**
     * Activity onCreate(savedInstanceState) 中在 setContentView之前调用该方法
     */
    protected void onSetDataBeforeSetLayout() {

    }

    /**
     * Activity onCreate(savedInstanceState) 中在 setContentView之后调用该方法
     */
    protected void onInitViewAfterOnCreate() {

    }

    /**
     * Activity onCreate(savedInstanceState) 中在 setContentView之后调用该方法
     */
    protected void onInitDataAfterOnCreate() {

    }

    /**
     * 获取当前Activity 相关联的LayoutId
     */
    protected abstract int getLayoutId();


    protected String getTAG() {
        return this.getClass().getSimpleName();
    }

    @Override
    protected void onDestroy() {

        if (enableEventBus && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        AppManager.getAppManager().removeActivity(this);

        super.onDestroy();
    }

    public boolean is_isVisible() {
        return _isVisible;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isInstanceStateSaved = false;
    }

    @Override
    protected void onPause() {
        isInstanceStateSaved = true;
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        isInstanceStateSaved = true;
        super.onSaveInstanceState(outState);
    }

    /**
     * 是否已经或者即将调用 onSaveInstanceState
     *
     * @return true 已经或者马上会调用onSaveInstanceState，false 处于onResume状态
     */
    public boolean isInstanceStateSaved() {
        return isInstanceStateSaved;
    }
}
