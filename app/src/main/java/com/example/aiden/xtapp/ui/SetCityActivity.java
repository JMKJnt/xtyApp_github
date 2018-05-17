package com.example.aiden.xtapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.aiden.xtapp.BuildConfig;
import com.example.aiden.xtapp.R;
import com.example.aiden.xtapp.base.BaseActivity;
import com.example.aiden.xtapp.db.XDBManager;
import com.example.aiden.xtapp.entity.base.Area;
import com.example.aiden.xtapp.entity.base.CityBase;
import com.example.aiden.xtapp.ui.Adapter.LocaleAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.ex.DbException;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;

public class SetCityActivity extends BaseActivity {


    @BindView(R.id.editText_setCity_cityName)
    protected EditText editText_setCity_cityName;


    @BindView(R.id.layout_city_list)
    protected LinearLayout layout_city_list;

    @BindView(R.id.listView1)
    protected ListView listView1;

    private LocaleAdapter adapter;//自定义的adapter



    @BindView(R.id.layout_init_city_list)
    protected LinearLayout layout_init_city_list;


    @BindView(R.id.button_init_beijing)
    protected Button button_init_beijing;

    @BindView(R.id.button_init_tianjin)
    protected Button button_init_tianjin;

    @BindView(R.id.button_init_shanghai)
    protected Button button_init_shanghai;

    @BindView(R.id.button_init_chongqing)
    protected Button button_init_chongqing;

    @BindView(R.id.button_init_shengyang)
    protected Button button_init_shengyang;

    @BindView(R.id.button_init_dalian)
    protected Button button_init_dalian;

    @BindView(R.id.button_init_changchun)
    protected Button button_init_changchun;

    @BindView(R.id.button_init_haerbin)
    protected Button button_init_haerbin;

    @BindView(R.id.button_init_zhengzhou)
    protected Button button_init_zhengzhou;

    @BindView(R.id.button_init_wuhan)
    protected Button button_init_wuhan;

    @BindView(R.id.button_init_changsha)
    protected Button button_init_changsha;

    @BindView(R.id.button_init_guangzhou)
    protected Button button_init_guangzhou;

    @BindView(R.id.button_init_shenzhen)
    protected Button button_init_shenzhen;

    @BindView(R.id.button_init_nanjing)
    protected Button button_init_nanjing;

    @BindView(R.id.button_init_hangzhou)
    protected Button button_init_hangzhou;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_city;
    }

    @Override
    protected void enableEventBus() {
        setEnableEventBus(true);
    }


    //页面加载完后执行
    @Override
    protected void onInitViewAfterOnCreate() {
        listView1.setEnabled(false);
        listView1.setVisibility(View.GONE);
        layout_init_city_list.setEnabled(true);
        layout_init_city_list.setVisibility(View.VISIBLE);

        CityBase cb1=new CityBase();
        cb1.setCityAreaId("101010100");
        cb1.setCityName("北京");
        button_init_beijing.setTag(cb1);

        CityBase cb2=new CityBase();
        cb2.setCityAreaId("101030100");
        cb2.setCityName("天津");
        button_init_tianjin.setTag(cb2);

        CityBase cb3=new CityBase();
        cb3.setCityAreaId("101020100");
        cb3.setCityName("上海");
        button_init_shanghai.setTag(cb3);

        CityBase cb4=new CityBase();
        cb4.setCityAreaId("101040100");
        cb4.setCityName("重庆");
        button_init_chongqing.setTag(cb4);

        CityBase cb5=new CityBase();
        cb5.setCityAreaId("101070101");
        cb5.setCityName("沈阳");
        button_init_shengyang.setTag(cb5);

        CityBase cb6=new CityBase();
        cb6.setCityAreaId("101070201");
        cb6.setCityName("大连");
        button_init_dalian.setTag(cb6);

        CityBase cb7=new CityBase();
        cb7.setCityAreaId("101060101");
        cb7.setCityName("长春");
        button_init_changchun.setTag(cb7);

        CityBase cb8=new CityBase();
        cb8.setCityAreaId("101050101");
        cb8.setCityName("哈尔滨");
        button_init_haerbin.setTag(cb8);

        CityBase cb9=new CityBase();
        cb9.setCityAreaId("101180101");
        cb9.setCityName("郑州");
        button_init_zhengzhou.setTag(cb9);

        CityBase cb10=new CityBase();
        cb10.setCityAreaId("101200101");
        cb10.setCityName("武汉");
        button_init_wuhan.setTag(cb10);

        CityBase cb11=new CityBase();
        cb11.setCityAreaId("101250101");
        cb11.setCityName("长沙");
        button_init_changsha.setTag(cb11);

        CityBase cb12=new CityBase();
        cb12.setCityAreaId("101280101");
        cb12.setCityName("广州");
        button_init_guangzhou.setTag(cb12);

        CityBase cb13=new CityBase();
        cb13.setCityAreaId("101280601");
        cb13.setCityName("深圳");
        button_init_shenzhen.setTag(cb13);

        CityBase cb14=new CityBase();
        cb14.setCityAreaId("101190101");
        cb14.setCityName("南京");
        button_init_nanjing.setTag(cb14);

        CityBase cb15=new CityBase();
        cb15.setCityAreaId("101210101");
        cb15.setCityName("杭州");
        button_init_hangzhou.setTag(cb15);



    }


    //listview每项的点击事件
    @OnItemClick(R.id.listView1)
    void onItemClick(int position) {//though there are 4 parameters, you can just write the one you want to use
//        Toast.makeText(this, "You clicked: " + adapter.getItem(position).getName(), Toast.LENGTH_SHORT).show();
        Log.i("SetCityActivity", adapter.getItem(position).getNAMECN());
        CityBase cityBase=new CityBase();
        cityBase.setCityAreaId(adapter.getItem(position).getAREAID());
        cityBase.setCityName(adapter.getItem(position).getNAMECN());
        backWeatherIndexActivity(cityBase);
    }

    //按钮点击事件
    @OnClick({
            R.id.button_init_beijing,
            R.id.button_init_tianjin,
            R.id.button_init_shanghai,
            R.id.button_init_chongqing,
            R.id.button_init_shengyang,
            R.id.button_init_dalian,
            R.id.button_init_changchun,
            R.id.button_init_haerbin,
            R.id.button_init_zhengzhou,
            R.id.button_init_wuhan,
            R.id.button_init_changsha,
            R.id.button_init_guangzhou,
            R.id.button_init_shenzhen,
            R.id.button_init_nanjing,
            R.id.button_init_hangzhou
    })
    protected void buttonClick(View v) {
        switch (v.getId()){
            case R.id.button_init_beijing:
                backWeatherIndexActivity(button_init_beijing.getTag());
                break;
            case R.id.button_init_tianjin:
                backWeatherIndexActivity(button_init_tianjin.getTag());
                break;
            case R.id.button_init_shanghai:
                backWeatherIndexActivity(button_init_shanghai.getTag());
                break;
            case R.id.button_init_chongqing:
                backWeatherIndexActivity(button_init_chongqing.getTag());
                break;
            case R.id.button_init_shengyang:
                backWeatherIndexActivity(button_init_shengyang.getTag());
                break;
            case R.id.button_init_dalian:
                backWeatherIndexActivity(button_init_dalian.getTag());
                break;
            case R.id.button_init_changchun:
                backWeatherIndexActivity(button_init_changchun.getTag());
                break;
            case R.id.button_init_haerbin:
                backWeatherIndexActivity(button_init_haerbin.getTag());
                break;
            case R.id.button_init_zhengzhou:
                backWeatherIndexActivity(button_init_zhengzhou.getTag());
                break;
            case R.id.button_init_wuhan:
                backWeatherIndexActivity(button_init_wuhan.getTag());
                break;
            case R.id.button_init_changsha:
                backWeatherIndexActivity(button_init_changsha.getTag());
                break;
            case R.id.button_init_guangzhou:
                backWeatherIndexActivity(button_init_guangzhou.getTag());
                break;
            case R.id.button_init_shenzhen:
                backWeatherIndexActivity(button_init_shenzhen.getTag());
                break;
            case R.id.button_init_nanjing:
                backWeatherIndexActivity(button_init_nanjing.getTag());
                break;
            case R.id.button_init_hangzhou:
                backWeatherIndexActivity(button_init_hangzhou.getTag());
                break;
        }

    }

//    @OnFocusChange(R.id.editText_setCity_cityName)
//    public void onFocusChange(View v,boolean hasFocus){
//        Log.i("SetCityActivity", "焦点---------------" );
//        listView1.setAdapter(null);
//    }


    @OnTextChanged(R.id.editText_setCity_cityName)
    public void onTextChanged(CharSequence s, int start, int before, int count){
        Log.i("SetCityActivity", "字符串改变了&&&&&&&&&&&&&&" + editText_setCity_cityName.getText().toString());
        EventBus.getDefault().post(new SetCityEvent(SetCityEvent.DO_SE_AREA,editText_setCity_cityName.getText().toString()));
    }

    private void doSeCity(String cityName)
    {

        layout_init_city_list.setEnabled(false);
        layout_init_city_list.setVisibility(View.GONE);
        listView1.setEnabled(true);
        listView1.setVisibility(View.VISIBLE);
        if (BuildConfig.DEBUG) {
            Log.i("SetCityActivity", "搜索输出框的值text为：" + cityName);
        }
        if(!"".equals(cityName)) {
            List<Area> rates = null;
            try {
                rates = XDBManager.getInstance().getInitDb().selector(Area.class)
                        .where("NAMECN", "like", "%" + cityName + "%")
                    .or("NAMEEN", "=",  cityName )
//                    .orderBy("", false)
                        .findAll();
            } catch (DbException e) {
                e.printStackTrace();
            }
            if(rates.size()!=0)
            {
                Area area = rates.get(0);
                if (BuildConfig.DEBUG) {
                    Log.i("SetCityActivity", "地区编码为" + area.getAREAID());
                    Log.i("SetCityActivity", "地区中文名称为" + area.getNAMECN());
                }
                adapter = new LocaleAdapter(this, rates);
                listView1.setAdapter(adapter);
            }else
            {
                Log.i("SetCityActivity", "搜索：未找到区域" + cityName);
//                adapter = new SimpleAdapter(this, null);
                listView1.setAdapter(null);

            }

        }else{
            Log.i("SetCityActivity", "空串：不进行搜索" + cityName);
            listView1.setAdapter(null);
            listView1.setEnabled(false);
            listView1.setVisibility(View.GONE);
            layout_init_city_list.setEnabled(true);
            layout_init_city_list.setVisibility(View.VISIBLE);
        }


    }

    private  void backWeatherIndexActivity(Object object)
    {
        CityBase cb=(CityBase)object;
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("cityAreaId", cb.getCityAreaId());
        bundle.putString("cityName", cb.getCityName());
        resultIntent.putExtras(bundle);
        this.setResult(RESULT_OK, resultIntent);
        finish();
    }

    /**
     * 后台触发事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onAsyncEvent(SetCityEvent event) {
//        switch (event.code) {
//            case SetCityEvent.DO_SE_AREA:
//                doSeCity(event.result);
//                break;
//
//        }
    }
    /**
     * 前台触发事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(SetCityEvent event) {
        switch (event.code) {
            case SetCityEvent.DO_SE_AREA:
                doSeCity(event.result);
                break;
//            case LoginActivity.LoginEvent.CODE_START_ACTIVITY://跳转首页
//                jumpWeatherIndex();
//                break;

        }
    }

    /**
     * 事件 实体
     */
    private static class SetCityEvent {
        /**
         * 按钮-后台 行为目的：根据用户输入 地区名称 进行搜索
         */
        private static final int DO_SE_AREA = 0X01;
        /**
         * 登录请求处理结束Finished
         */
        private static final int CODE_LOGIN_FINISHED = 0X02;
        /**
         * 跳转页面
         */
        private static final int CODE_START_ACTIVITY = 0X03;
        final int code;
        String result;
        String userId;
        String cityBaseId;
        String userAndEquipmentId;
        Object object;


        SetCityEvent(int code) {
            this.code = code;
        }

        public SetCityEvent(int code, String result) {
            this.code = code;
            this.result = result;
        }

        public SetCityEvent(int code,Object object)
        {
            this.code = code;
            this.object = object;
        }



    }
}
