package com.mm.medicalman.Home;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import com.mm.medicalman.Api.NpApi;
import com.mm.medicalman.Bean.OveYearsQuestionBean;
import com.mm.medicalman.Bean.SchoollistBean;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.Tool.RetrofitUtils;
import com.mm.medicalman.Tool.SPUtils;
import com.mm.medicalman.Tool.ToastUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang on 2017/10/23.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class OpenSclloActivity extends Activity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private OpenSclloAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_open_scllo);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.43);   //高度设置为屏幕的1.0
        p.width = (int) (d.getWidth() * 0.86);    //宽度设置为屏幕的0.8
        getWindow().setAttributes(p);
        initId();
        initData();
    }

    private void initData() {
        String userid = (String) SPUtils.get(this, "user_id", "");
        String token = (String) SPUtils.get(this, "token", "");
        Call<SchoollistBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class)
                .schoollist(token, userid);
        getSignBeanCall.enqueue(new Callback<SchoollistBean>() {
            @Override
            public void onResponse(Call<SchoollistBean> call, Response<SchoollistBean> response) {
                SchoollistBean bean = response.body();
                if (bean == null) return;
                if (bean.getStatus().equals("1")) {
                    if (bean.getData() == null || bean.getData().getList() == null || bean.getData().getList().size() == 0)
                        return;
                    initAdapter(bean.getData().getList());
                } else {
                    ToastUtils.initLoginAgain(OpenSclloActivity.this, bean.getErr_var());
                    ToastUtils.showShort(OpenSclloActivity.this, bean.getMessage());
                }
            }

            @Override
            public void onFailure(Call<SchoollistBean> call, Throwable t) {
                MoudleUtils.toChekWifi(OpenSclloActivity.this);
            }
        });
    }

    private void initId() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_information);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);//线性横向布局
        // 设置布局管理器
        recyclerView.setLayoutManager(mLayoutManager);
    }

    private void initAdapter(List<String> data) {
        if (mAdapter == null) {
            mAdapter = new OpenSclloAdapter(this);
            mAdapter.updateData(data);
            recyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.updateData(data);
            mAdapter.notifyDataSetChanged();
        }
    }
}
