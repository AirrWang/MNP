package com.mm.medicalman.Home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.mm.medicalman.Api.WfApi;
import com.mm.medicalman.Bean.SigninHelpBean;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.Tool.RetrofitUtils;
import com.mm.medicalman.Tool.SPUtils;
import com.mm.medicalman.Tool.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang on 2017/11/6.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class SiginHelpActivity extends AppCompatActivity{

    @BindView(R.id.toolbar_title)
    TextView tv;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_loction)
    TextView tv_loction;
    @BindView(R.id.tv_distance)
    TextView tv_distance;

    private ProgressDialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
        initToolbar();
        initData();
    }

    private void initData() {
        dialog = new ProgressDialog(this);
        MoudleUtils.dialogShow(dialog);
        String user_id = (String) SPUtils.get("user_id", "");
        String token = (String) SPUtils.get("token", "");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        Call<SigninHelpBean> callBack = restApi.signinHelp( user_id,token);
        callBack.enqueue(new Callback<SigninHelpBean>() {
            @Override
            public void onResponse(Call<SigninHelpBean> call, Response<SigninHelpBean> response) {
                SigninHelpBean bean = response.body();
                if (bean == null) {
                    finish();
                }
                switch (bean.getStatus()) {
                    case "1":
                        initView(bean);
                        break;
                    case "0":
                        ToastUtils.initLoginAgain(SiginHelpActivity.this, bean.getErr_var());
                        ToastUtils.showShort(SiginHelpActivity.this, bean.getMessage());
                        finish();
                        break;
                }
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<SigninHelpBean> call, Throwable t) {
                MoudleUtils.dialogDismiss(dialog);
                MoudleUtils.toChekWifi(SiginHelpActivity.this);
                finish();
            }
        });
    }

    private void initView(SigninHelpBean bean) {
        tv_name.setText(bean.getData().getName());
        tv_time.setText(bean.getData().getTime());
        tv_loction.setText(bean.getData().getLocation());
        tv_distance.setText("有效范围"+bean.getData().getDistance()+"米");

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
        tv.setText("使用帮助");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }
}
