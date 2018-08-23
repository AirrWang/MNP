package com.mm.medicalman.Home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mm.medicalman.Api.WfApi;
import com.mm.medicalman.Bean.BeanTool;
import com.mm.medicalman.Bean.SigninBean;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.AppConfig;
import com.mm.medicalman.Tool.FrescoUtils;
import com.mm.medicalman.Tool.LocationTools;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.Tool.RetrofitUtils;
import com.mm.medicalman.Tool.SPUtils;
import com.mm.medicalman.Tool.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang on 2017/11/6.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class SiginActivity extends AppCompatActivity implements AMapLocationListener {

    // 定位相关
    private AMapLocationClient mlocationClient;

    @BindView(R.id.toolbar_sd)
    SimpleDraweeView toolbar_sd;
    @BindView(R.id.rv_sign)
    RecyclerView rv_sign;
    @BindView(R.id.tv_today)
    TextView tv_today;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.btn_sign)
    Button btn_sign;
    @BindView(R.id.tv_log)
    TextView tv_log;

    private ProgressDialog dialog;
    private LatLng center;
    private String startNodeStr;
    private int id=0;
    private boolean complate;
    private int type=1;
    private boolean sign;
    private float distance;
    private SigninBean bean;
    private int a;
    private LatLng target;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        ButterKnife.bind(this);
        initToolbar();
        FrescoUtils.setImage(toolbar_sd, AppConfig.res+R.drawable.sign_in_use_help);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rv_sign.setLayoutManager(linearLayoutManager);
        initDw();

    }

    @OnClick(R.id.btn_sign)
    void Sign(){
        if (sign){
            if (complate){
                ToastUtils.showShort(SiginActivity.this,"今日打卡已完成");
            }else {
                toSign();
            }
        }else {
           ToastUtils.showShort(SiginActivity.this,"无法打卡");
        }}
    @OnClick(R.id.toolbar_sd)
    void Help(){
        startActivity(new Intent().setClass(SiginActivity.this,SiginHelpActivity.class));
        }

    private void toSign() {
        dialog = new ProgressDialog(this);
        MoudleUtils.dialogShow(dialog);
        String user_id = (String) SPUtils.get("user_id", "");
        String token = (String) SPUtils.get("token", "");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        Call<BeanTool> callBack = restApi.toSignin(user_id,token,id,type,startNodeStr);
        callBack.enqueue(new Callback<BeanTool>() {
            @Override
            public void onResponse(Call<BeanTool> call, Response<BeanTool> response) {
                BeanTool bean = response.body();
                if (bean == null) {
                    return;
                }
                switch (bean.getStatus()) {
                    case "1":
                        ToastUtils.showShort(SiginActivity.this, bean.getMessage());
                        MoudleUtils.dialogDismiss(dialog);
                        initGetData();
                        break;
                    case "0":
                        ToastUtils.initLoginAgain(SiginActivity.this, bean.getErr_var());
                        ToastUtils.showShort(SiginActivity.this, bean.getMessage());
                        MoudleUtils.dialogDismiss(dialog);
                        initGetData();
                        break;
                }
            }

            @Override
            public void onFailure(Call<BeanTool> call, Throwable t) {
                MoudleUtils.dialogDismiss(dialog);
                MoudleUtils.toChekWifi(SiginActivity.this);
                initGetData();
            }
        });

    }

    private void initGetData() {
        dialog = new ProgressDialog(this);
        MoudleUtils.dialogShow(dialog);
        String user_id = (String) SPUtils.get("user_id", "");
        String token = (String) SPUtils.get("token", "");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        Call<SigninBean> callBack = restApi.signin( user_id,token);
        callBack.enqueue(new Callback<SigninBean>() {
            @Override
            public void onResponse(Call<SigninBean> call, Response<SigninBean> response) {
                bean = response.body();
                if (bean == null) {
                    return;
                }
                switch (bean.getStatus()) {
                    case "1":
                        initView(bean);
                        MoudleUtils.dialogDismiss(dialog);
                        break;
                    case "0":
                        ToastUtils.initLoginAgain(SiginActivity.this, bean.getErr_var());
                        ToastUtils.showShort(SiginActivity.this, bean.getMessage());
                        MoudleUtils.dialogDismiss(dialog);
                        finish();
                        break;
                }

            }

            @Override
            public void onFailure(Call<SigninBean> call, Throwable t) {
                MoudleUtils.dialogDismiss(dialog);
                MoudleUtils.toChekWifi(SiginActivity.this);
                finish();
            }
        });

    }

    /**
     * 定位初始化
     */
    private void initDw() {
        mlocationClient = new AMapLocationClient(this);
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位参数
        mlocationClient.setLocationOption(LocationTools.initLocationMore(2000));
    }

    private void initView(SigninBean bean) {
        initAdapter(bean.getData().getList());
        if (bean.getData().getComplete()==bean.getData().getTotal()){
            complate=true;
        }
        if (bean.getData()==null){
            return;
        }
        tv_today.setText(bean.getData().getDate());
        //已完成
        a = bean.getData().getComplete();
        if (a <bean.getData().getTotal()) {
            if (bean.getData().getList().get(a).getIn_status().equals("0")){
                btn_sign.setText("签到");
                type=1;
            }else {
                btn_sign.setText("签退");
                type=2;
            }
            id=bean.getData().getList().get(a).getAtt_id();
        }else {
            double c= Double.parseDouble(bean.getData().getList().get(a-1).getLat());
            double d= Double.parseDouble(bean.getData().getList().get(a-1).getLon());
            target = new LatLng(c, d);
            return;
        }
        double c= Double.parseDouble(bean.getData().getList().get(a).getLat());
        double d= Double.parseDouble(bean.getData().getList().get(a).getLon());
        target = new LatLng(c, d);


    }


    private RecycleSignAdapter adapter;
    private void initAdapter(List<SigninBean.SiginInfo.DetailInfo> list) {
        if (adapter == null) {
            adapter = new RecycleSignAdapter(this);
            adapter.updateData(list);
            adapter.setType(bean.getData().getType());
            rv_sign.setAdapter(adapter);
        } else {
            adapter.updateData(list);
            adapter.setType(bean.getData().getType());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 页面标题
     */
    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar_title.setText("签到");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
        }
    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mlocationClient.startLocation();

    }
    int x=1;
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) if (aMapLocation != null
                && aMapLocation.getErrorCode() == 0) {
            startNodeStr = aMapLocation.getAddress();

            center = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            if (x==1) {
                initGetData();
                x++;
            }
            if (target!=null) {
                distance = AMapUtils.calculateLineDistance(center, target);
                if (a<bean.getData().getTotal()) {
                    if (distance > bean.getData().getList().get(a).getDistance()) {
                        tv_log.setText("无法打卡");
                        sign = false;
                    } else {
                        tv_log.setText("已进入地点考勤范围");
                        sign = true;
                    }
                }else {
                    tv_log.setText("今日打卡已完成");
                }
            }
        } else {
//            String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
            ToastUtils.showShort(SiginActivity.this, "定位失败");
            sign = false;
        }
    }
}
