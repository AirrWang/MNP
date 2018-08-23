package com.mm.medicalman.Home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mm.medicalman.Api.NpApi;
import com.mm.medicalman.Api.WfApi;
import com.mm.medicalman.Bean.GetyearsBean;
import com.mm.medicalman.Bean.MockResultsBean;
import com.mm.medicalman.Bean.OveYearsQuestionBean;
import com.mm.medicalman.Bean.QuestBean;
import com.mm.medicalman.DBBean.MockResults;
import com.mm.medicalman.DBBean.Subject;
import com.mm.medicalman.MainActivity;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.DBUtil.DbCore;
import com.mm.medicalman.Tool.Kyloading.KyLoadingBuilder;
import com.mm.medicalman.Tool.Kyloading.KyLoadingView;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.Tool.RetrofitUtils;
import com.mm.medicalman.Tool.SPUtils;
import com.mm.medicalman.Tool.SavaDataLocalUtils;
import com.mm.medicalman.Tool.ToastUtils;
import com.mm.medicalman.greendao.gen.MockResultsDao;
import com.mm.medicalman.greendao.gen.SubjectDao;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang on 2017/11/2.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class YearsActivity extends AppCompatActivity {

    private ListView listView;
    private KyLoadingBuilder kyLoadingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_years);
        initToolbar();
        listView = (ListView) findViewById(R.id.lv_years);
        kyLoadingView=new KyLoadingBuilder(this);
        MoudleUtils.kyloadingShow(kyLoadingView);
        initData();
    }

    private void initData() {
        String user_id = (String) SPUtils.get("user_id", "");
        String token = (String) SPUtils.get("token", "");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        Call<GetyearsBean> callBack = restApi.getOverYears(user_id, token);
        callBack.enqueue(new Callback<GetyearsBean>() {
            @Override
            public void onResponse(Call<GetyearsBean> call, Response<GetyearsBean> response) {
                MoudleUtils.kyloadingDismiss(kyLoadingView);
                if (response.body() == null) {
                    return;
                }
                switch (response.body().getStatus()) {
                    case "1":
                        initAdapter(response.body().getData().getList());
                        break;
                    case "0":
                        ToastUtils.initLoginAgain(YearsActivity.this, response.body().getErr_var());
                        ToastUtils.showShort(YearsActivity.this, response.body().getMessage());
                        break;
                }
            }

            @Override
            public void onFailure(Call<GetyearsBean> call, Throwable t) {
                MoudleUtils.toChekWifi(YearsActivity.this);
                MoudleUtils.kyloadingDismiss(kyLoadingView);
            }
        });
    }

    private void initSetItem(final List<GetyearsBean.GetyearsData.Detail> list) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DbCore.getDaoSession().getTitleRecordExaminationDao().deleteAll();
                startActivity(new Intent().putExtra("eName",list.get(position).getName()).putExtra("id", list.get(position).getPaper_id())
                        .putExtra("type", "4").putExtra("mmNp",list.get(position).getMinute()).
                                setClass(YearsActivity.this, ExaminationActivity.class));
            }
        });
    }


    private ListAdapterYearsPaper adapter;

    private void initAdapter(List<GetyearsBean.GetyearsData.Detail> list) {
        if (list == null || list.size() == 0) {
            finish();
            ToastUtils.showShort(this, "暂无");
            return;
        }
        if (adapter == null) {
            adapter = new ListAdapterYearsPaper(this);
            adapter.setList(list);
            listView.setAdapter(adapter);
        } else {
            adapter.setList(list);
            adapter.notifyDataSetChanged();
        }
        initSetItem(list);

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
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("历年试题");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }
}
