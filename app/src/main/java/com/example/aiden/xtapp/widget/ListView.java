package com.example.aiden.xtapp.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.List;

public class ListView extends android.widget.ListView {

    private final OnScrollListenerProxy onScrollListenerProxy = new OnScrollListenerProxy();

    public ListView(@NonNull Context context) {
        super(context);
        init();
    }

    public ListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ListView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        super.setOnScrollListener(onScrollListenerProxy);
    }

    @Override
    @Deprecated
    public void setOnScrollListener(OnScrollListener listener) {
        onScrollListenerProxy.setOnScrollListener(listener);
    }

    public void addOnScrollListener(OnScrollListener listener) {
        onScrollListenerProxy.addOnScrollListener(listener);
    }

    public void removeOnScrollListener(OnScrollListener listener) {
        onScrollListenerProxy.removeOnScrollListener(listener);
    }

    public void clearOnScrollListeners() {
        onScrollListenerProxy.clearOnScrollListeners();
    }

    private static class OnScrollListenerProxy implements OnScrollListener {

        private OnScrollListener onScrollListener;
        private List<OnScrollListener> onScrollListenerList;

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (onScrollListener != null) {
                onScrollListener.onScrollStateChanged(view, scrollState);
            }
            if (onScrollListenerList != null && !onScrollListenerList.isEmpty()) {
                for (OnScrollListener onScrollListener : onScrollListenerList) {
                    onScrollListener.onScrollStateChanged(view, scrollState);
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (onScrollListener != null) {
                onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
            if (onScrollListenerList != null && !onScrollListenerList.isEmpty()) {
                for (OnScrollListener onScrollListener : onScrollListenerList) {
                    onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
            }
        }

        void setOnScrollListener(OnScrollListener listener) {
            onScrollListener = listener;
        }

        void addOnScrollListener(OnScrollListener listener) {
            if (onScrollListenerList == null) {
                onScrollListenerList = new ArrayList<>();
            }
            onScrollListenerList.add(listener);
        }

        void removeOnScrollListener(OnScrollListener listener) {
            if (onScrollListenerList != null) {
                onScrollListenerList.remove(listener);
            }
        }

        void clearOnScrollListeners() {
            if (onScrollListenerList != null) {
                onScrollListenerList.clear();
            }
        }

    }

}
