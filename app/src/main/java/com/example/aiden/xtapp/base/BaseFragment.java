package com.example.aiden.xtapp.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * @auther by yushilei.
 * @time 2016/10/27-10:18
 * @desc
 */

public abstract class BaseFragment extends Fragment {
    boolean enableEventBus = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onEnableEventBus();

        if (enableEventBus && !EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    protected void onEnableEventBus() {
    }

    protected void setEnableEventBus(boolean enableEventBus) {
        this.enableEventBus = enableEventBus;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = LayoutInflater.from(getContext()).inflate(getLayoutId(), container, false);

        ButterKnife.bind(this, contentView);

        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onInitViewAfterOnViewCreated();
        onInitDataAfterOnViewCreated();
    }

    protected void onInitViewAfterOnViewCreated() {

    }

    protected void onInitDataAfterOnViewCreated() {

    }

    protected abstract int getLayoutId();

    @Override
    public void onDestroy() {
        if (enableEventBus && EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public String getTAG() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
