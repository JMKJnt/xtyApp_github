package com.example.aiden.xtapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;

import com.example.aiden.xtapp.BuildConfig;
import com.example.aiden.xtapp.R;


/**
 * @auther by yushilei.
 * @time 2016/10/27-16:36
 * @desc
 */
@SuppressWarnings("NullableProblems")
public class MultiStateView extends FrameLayout {
    private static final String TAG = "MultiStateView";

    private MultiStateViewData mViewState = new MultiStateViewData(ContentState.CONTENT);

    private View mContentView;
    private View mLoadingView;
    private View mNetworkErrorView;
    private View mGeneralErrorView;
    private View mPermissionErrorView;


    private OnClickListener mTapToRetryClickListener;

    public MultiStateView(Context context) {
        this(context, null);
    }

    public MultiStateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttrs(context, attrs);
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultiStateView);

        mViewState.loadingLayoutResId = a.getResourceId(R.styleable.MultiStateView_layout_loading, R.layout.msv_loading);
        mViewState.networkErrorLayoutResId = a.getResourceId(R.styleable.MultiStateView_layout_error_net, R.layout.msv_error_net);
        mViewState.generalErrorLayoutResId = a.getResourceId(R.styleable.MultiStateView_layout_error_unknown, R.layout.msv_error_unknown);
        mViewState.permissionErrorLayoutResId = a.getResourceId(R.styleable.MultiStateView_layout_error_permission, R.layout.msv_error_permission);

        a.recycle();
    }

    public void setState(int nativeInt) {
        setState(ContentState.getState(nativeInt));
    }

    public void setState(final ContentState state) {
        if (state == mViewState.state) {
            return;
        }
        final View contentView = getContentView();

        if (contentView == null) {
            if (BuildConfig.DEBUG) Log.v(TAG, "Content not yet set, waiting...");
            return;
        }
        if (state == ContentState.LOADING && mViewState.state == ContentState.CONTENT) {
            View newStateView = getStateView(state);
            if (newStateView != null) {
                if (BuildConfig.DEBUG) Log.v(TAG, "Showing new state " + state);
                newStateView.setVisibility(View.VISIBLE);
            }
            mViewState.state = state;
            return;
        }


        final ContentState previousState = mViewState.state;

        View previousView = getStateView(previousState);

        if (previousView != null) {
            if (BuildConfig.DEBUG) Log.v(TAG, "Hiding previous state " + previousState);
            previousView.setVisibility(View.GONE);
        }
        // Show the new state view
        View newStateView = getStateView(state);

        if (newStateView != null) {
            if (BuildConfig.DEBUG) Log.v(TAG, "Showing new state " + state);
            newStateView.setVisibility(View.VISIBLE);
        }

        mViewState.state = state;

    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        if (!isViewInternal(child)) {
            addContentView(child);
        }
        Log.i(TAG, "addView" + child);
        super.addView(child, params);
    }

    private void addContentView(View contentView) {
        if (mContentView != null && mContentView != contentView) {
            throw new IllegalStateException("Can't add more than one view to MultiStateView");
        }
        setContentView(contentView);
    }

    public void setContentView(View contentView) {
        mContentView = contentView;
        setState(mViewState.state);
    }

    private boolean isViewInternal(View view) {
        return view == mNetworkErrorView || view == mGeneralErrorView || view == mLoadingView || view == mPermissionErrorView;
    }

    @Nullable
    public View getStateView(ContentState state) {
        if (state == null) return null;

        switch (state) {
            case ERROR_NETWORK:
                return getNetworkErrorView();

            case ERROR_PERMISSION:
                return getPermissionErrorView();

            case ERROR_GENERAL:
                return getGeneralErrorView();

            case LOADING:
                return getLoadingView();

            case CONTENT:
                return getContentView();
        }

        return null;
    }

    public View getContentView() {
        return mContentView;
    }

    @Nullable
    public View getNetworkErrorView() {
        if (mNetworkErrorView == null) {
            mNetworkErrorView = View.inflate(getContext(), mViewState.networkErrorLayoutResId, null);
            mNetworkErrorView.setOnClickListener(mTapToRetryClickListener);
            addView(mNetworkErrorView);

            mNetworkErrorView.setOnClickListener(mTapToRetryClickListener);

        }
        return mNetworkErrorView;
    }

    /**
     * Returns the view to be displayed for the case of a network error
     *
     * @return
     */
    @NonNull
    public View getPermissionErrorView() {
        if (mPermissionErrorView == null) {
            mPermissionErrorView = View.inflate(getContext(), mViewState.permissionErrorLayoutResId, null);
            mPermissionErrorView.setOnClickListener(mTapToRetryClickListener);
            addView(mPermissionErrorView);

            mPermissionErrorView.setOnClickListener(mTapToRetryClickListener);
        }
        return mPermissionErrorView;
    }

    /**
     * Returns the view to be displayed for the case of an unknown error
     *
     * @return
     */
    @NonNull
    public View getGeneralErrorView() {
        if (mGeneralErrorView == null) {
            mGeneralErrorView = View.inflate(getContext(), mViewState.generalErrorLayoutResId, null);
            mGeneralErrorView.setOnClickListener(mTapToRetryClickListener);
            addView(mGeneralErrorView);

            mGeneralErrorView.setOnClickListener(mTapToRetryClickListener);
        }

        return mGeneralErrorView;
    }

    /**
     * Builds the loading view if not currently built, and returns the view
     */
    @NonNull
    public View getLoadingView() {
        if (mLoadingView == null) {
            mLoadingView = View.inflate(getContext(), mViewState.loadingLayoutResId, null);
            addView(mLoadingView);
        }

        return mLoadingView;
    }

    public boolean isLoading() {
        return mViewState.state == ContentState.LOADING;
    }

    public void setOnTapToRetryClickListener(View.OnClickListener listener) {
        mTapToRetryClickListener = listener;

        if (mNetworkErrorView != null) {
            mNetworkErrorView.setOnClickListener(listener);
        }

        if (mGeneralErrorView != null) {
            mGeneralErrorView.setOnClickListener(listener);
        }

        if (mPermissionErrorView != null) {
            mPermissionErrorView.setOnClickListener(listener);
        }
    }

    public static class MultiStateViewData {
        public int loadingLayoutResId;
        public int generalErrorLayoutResId;
        public int networkErrorLayoutResId;
        public int permissionErrorLayoutResId;
        ContentState state;

        public MultiStateViewData(ContentState state) {
            this.state = state;
        }
    }

    public static enum ContentState {
        CONTENT(0x01),
        /**
         * Used to indicate that the Loading indication should be displayed to the user
         */
        LOADING(0x02),
        /**
         * Used to indicate that the Network Error indication should be displayed to the user
         */
        ERROR_NETWORK(0x03),
        /**
         * Used to indicate that the Permission Error indication should be displayed to the user
         */
        ERROR_PERMISSION(0x04),
        /**
         * Used to indicate that the Unknown Error indication should be displayed to the user
         */
        ERROR_GENERAL(0x05);

        public final int nativeInt;
        private static final SparseArray<ContentState> sStates = new SparseArray<ContentState>();

        static {
            for (ContentState scaleType : values()) {
                sStates.put(scaleType.nativeInt, scaleType);
            }
        }

        public static ContentState getState(int nativeInt) {
            if (nativeInt >= 0) {
                return sStates.get(nativeInt);
            }

            return null;
        }

        private ContentState(int nativeValue) {
            this.nativeInt = nativeValue;
        }
    }
}
