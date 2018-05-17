package com.example.aiden.xtapp.ui;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.annotation.VisibleForTesting;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.example.aiden.xtapp.R;
import com.example.aiden.xtapp.adapder.StoryListAdapter;
import com.example.aiden.xtapp.base.BaseActivity;
import com.example.aiden.xtapp.base.BaseApp;
import com.example.aiden.xtapp.entity.json.XtData;
import com.example.aiden.xtapp.util.GsonUtil;
import com.example.aiden.xtapp.viewholder.LoadMoreFooter;
import com.example.aiden.xtapp.viewholder.PaddingFooter;
import com.example.aiden.xtapp.viewholder.PaddingHeader;
import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;
import com.takwolf.android.hfrecyclerview.HeaderAndFooterRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class XtListDemoActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, LoadMoreFooter.OnLoadMoreListener{

    public static final String TAG = "XtListDemoActivity_LOG";

    @BindView(R.id.swipeRefresh_XtListDemo)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView_XtListDemo)
    HeaderAndFooterRecyclerView recyclerView;
    @BindView(R.id.tvConsoleText)
    HTextView tvConsoleText;
    @BindView(R.id.tvConsoleText_time)
    TextView tvConsoleText_time;




    private LoadMoreFooter loadMoreFooter;
    private StoryListAdapter adapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_xt_list_demo;
    }
    //开启事件监听
    @Override
    protected void enableEventBus() {
        setEnableEventBus(true);
    }

    /**
     * 前台触发事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(XtListDemoEvent event) {
        switch (event.code) {
//            case LoginEvent.CODE_LOGIN_FINISHED:
//                processingReturn(event.result);
//                break;
            case XtListDemoEvent.CODE_DO_LOAD_MORE://加载更多
                doLoadMore(event.apiResponse);
                break;
            case XtListDemoEvent.CODE_DO_REFRESH://刷新
                doRefresh(event.apiResponse);
                break;

        }
    }


    @Override
    protected void onInitDataAfterOnCreate(){
        BaseApp.set_XtListDemoActivity(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadMoreFooter = new LoadMoreFooter(this, recyclerView, this);
        new PaddingHeader(this, recyclerView);
        new PaddingFooter(this, recyclerView);

        adapter = new StoryListAdapter(this);
        recyclerView.setAdapter(adapter);

        refreshLayout.setColorSchemeResources(R.color.color_accent);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setRefreshing(true);
        onRefresh();
    }


    /**
     * 加载更多
     */
    @Override
    public void onLoadMore() {
        BaseApp.getHttpApiClient_zumelAPPInterface().list_xtData(new ApiCallback() {
            @Override
            public void onFailure(ApiRequest request, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(ApiRequest request, ApiResponse response) {
//                doLoadMore(response);
                EventBus.getDefault().post(new XtListDemoEvent(XtListDemoEvent.CODE_DO_LOAD_MORE, response));
            }
        });
    }

    /**
     * 刷新
     */
    @Override
    public void onRefresh() {
        BaseApp.getHttpApiClient_zumelAPPInterface().list_xtData(new ApiCallback() {
            @Override
            public void onFailure(ApiRequest request, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(ApiRequest request, ApiResponse response) {
//                doRefresh(response);
                EventBus.getDefault().post(new XtListDemoEvent(XtListDemoEvent.CODE_DO_REFRESH, response));
            }
        });
    }



    private void doLoadMore(ApiResponse response){
        String bodyString = new String(response.getBody(), SdkConstant.CLOUDAPI_ENCODING);
        XtData xtData= GsonUtil.jsonToBean(bodyString,XtData.class);

//        if(0==xtData.getRequestCode()){
            adapter.getXtUploadList().addAll(xtData.getList_xtData());
            adapter.notifyDataSetChanged();
//        }else{
//            Toast.makeText(this,xtData.getRequestMessage(),Toast.LENGTH_SHORT).show();
//        }


        refreshLayout.setRefreshing(false);
//        loadMoreFooter.setState(LoadMoreFooter.STATE_ENDLESS);
    }


    private void doRefresh(ApiResponse response){
        String bodyString = new String(response.getBody(), SdkConstant.CLOUDAPI_ENCODING);
        XtData xtData= GsonUtil.jsonToBean(bodyString,XtData.class);

//        if(0==xtData.getRequestCode()){
            adapter.getXtUploadList().clear();
            adapter.getXtUploadList().addAll(xtData.getList_xtData());
            adapter.notifyDataSetChanged();
//        }else{
//            Toast.makeText(this,xtData.getRequestMessage(),Toast.LENGTH_SHORT).show();
//        }


        refreshLayout.setRefreshing(false);
//        loadMoreFooter.setState(LoadMoreFooter.STATE_ENDLESS);
    }


    public void appendConsoleText(String text, String text2) {
//        this.tvConsoleText.append(text + "\n");
        this.tvConsoleText_time.setText("上传时间："+text2);

        tvConsoleText.setAnimateType(HTextViewType.EVAPORATE);
        tvConsoleText.animateText(text); // animate

        this.tvConsoleText.setText(text);
        Log.i(TAG,"Message is:------"+text);
    }

    /**
     * 事件 实体
     */
    private static class XtListDemoEvent {
        /**
         * 参数判断完成,开始访问后台
         */
        private static final int CODE_DO_REFRESH = 0X01;
        private static final int CODE_DO_LOAD_MORE = 0X02;
        final int code;
        ApiResponse apiResponse;
//        XtListDemoEvent(int code) {
//            this.code = code;
//        }

        public XtListDemoEvent(int code, ApiResponse apiResponse) {
            this.code = code;
            this.apiResponse = apiResponse;
        }


    }
}
