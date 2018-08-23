package com.mm.medicalman.Mine;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mm.medicalman.Api.NpApi;
import com.mm.medicalman.Api.WfApi;
import com.mm.medicalman.Bean.BeanTool;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.Tool.RetrofitUtils;
import com.mm.medicalman.Tool.SPUtils;
import com.mm.medicalman.Tool.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang on 2017/10/23.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class FeedbackActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.mine_feedback)
    EditText mFeedBack;
    private String content;
    @BindView(R.id.btn_to)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_feedback);
        ButterKnife.bind(this);
        progressDialog=new ProgressDialog(this);
        initToolbar();
    }

    @OnClick(R.id.btn_to)
    void initDataTo() {
        initData();
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
        toolbar_title.setText("意见反馈");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }

    private void initData() {
        content = mFeedBack.getText().toString().trim();
        if (content == null || content.equals("")) {
            ToastUtils.showShort(this, "反馈不能为空哦");
        } else {
            if (!MoudleUtils.containsEmoji(content)) {
                initTaskFB();
            } else {
                ToastUtils.showShortNotInternet(this, "不支持输入Emoji表情符号");
            }
        }
    }

    private ProgressDialog progressDialog;

    private void initTaskFB() {
        MoudleUtils.dialogShow(progressDialog);
        String userid = (String) SPUtils.get(this, "user_id", "");
        String token = (String) SPUtils.get(this, "token", "");
        Call<BeanTool> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).feedback(token, userid, content);
        getSignBeanCall.enqueue(new Callback<BeanTool>() {
            @Override
            public void onResponse(Call<BeanTool> call, Response<BeanTool> response) {
                MoudleUtils.dialogDismiss(progressDialog);
                BeanTool bean = response.body();
                if (bean != null) {
                    ToastUtils.showShort(FeedbackActivity.this, bean.getMessage());
                    if (bean.getStatus().equals("1")) {
                        finish();
                    }else {
                        ToastUtils.initLoginAgain(FeedbackActivity.this, bean.getErr_var());
                    }
                }
            }

            @Override
            public void onFailure(Call<BeanTool> call, Throwable t) {
                MoudleUtils.dialogDismiss(progressDialog);
                MoudleUtils.toChekWifi(FeedbackActivity.this);
            }
        });
    }
}
