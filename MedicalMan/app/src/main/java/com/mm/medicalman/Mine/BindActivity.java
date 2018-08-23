package com.mm.medicalman.Mine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
public class BindActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.editText)
    EditText et_tel;
    @BindView(R.id.editText2)
    EditText et_code;
    @BindView(R.id.button3)
    Button btn_register;
    @BindView(R.id.btn_code)
    Button btn_code;
    @BindView(R.id.et_login_psw)
    EditText et_login_psw;
    @BindView(R.id.et_login_name)
    EditText et_login_name;

    private ProgressDialog dialog;
    private BindActivity.TimeCount nRegist;
    private String tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind);
        ButterKnife.bind(this);
        initToolbar();
        Intent intent=getIntent();
        tel = intent.getStringExtra("phone");
        et_tel.setText(tel+"");
        dialog = new ProgressDialog(this);
    }

    @OnClick(R.id.btn_code)
    void initSetCode() {
        getCode();
    }

    @OnClick(R.id.button3)
    void thirdRegister() {
        toRegistData();
    }
    private void toRegistData() {

        if (et_code.getText().toString().trim().equals("") || et_login_psw.getText().toString().trim().equals("")||et_login_name.getText().toString().trim().equals("")) {
            ToastUtils.showShort(this, "必须填写全部信息");
            return;
        }

        MoudleUtils.dialogShow(dialog);
        toGetScores();
    }

    private void toGetScores() {
        String user_id = (String) SPUtils.get("user_id", "");
        String token = (String) SPUtils.get("token", "");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        Call<BeanTool> callBack = restApi.bind(user_id,token,tel,et_code.getText().toString().trim(),et_login_psw.getText().toString().trim(),et_login_name.getText().toString().trim());
        callBack.enqueue(new Callback<BeanTool>() {
            @Override
            public void onResponse(Call<BeanTool> call, Response<BeanTool> response) {

                if (response.body() == null) {
                    MoudleUtils.dialogDismiss(dialog);
                    return;
                }
                switch (response.body().getStatus()) {
                    case "1":
                        startActivity(new Intent().setClass(BindActivity.this, BindSchoolInfoActivity.class));
                        MoudleUtils.dialogDismiss(dialog);
                        finish();
                        break;
                    case "0":
                        ToastUtils.initLoginAgain(BindActivity.this, response.body().getErr_var());
                        ToastUtils.showShort(BindActivity.this, response.body().getMessage());
                        MoudleUtils.dialogDismiss(dialog);
                        break;
                }
            }

            @Override
            public void onFailure(Call<BeanTool> call, Throwable t) {
                MoudleUtils.toChekWifi(BindActivity.this);
                MoudleUtils.dialogDismiss(dialog);
            }
        });

    }

    private void getCode() {
        if (nRegist != null) return;
        String s = tel+"";
        if (s.equals("")) {
            ToastUtils.showShort(this, "必须填写有效手机号");
            return;
        }
        if (!MoudleUtils.isPhone(s)) {
            ToastUtils.showShort(this, "手机号码格式不正确");
            return;
        }
        btn_code.setClickable(false); //防止重复点击
        btn_code.setBackgroundResource(R.drawable.btn_regist_5dp_code);
        nRegist = new TimeCount(60000, 1000);//开始计时60s
        nRegist.start();
        toGetCodeTask(s);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        initCancleTime();
    }
    private void initCancleTime() {
        if (nRegist != null) {
            nRegist.cancel();
        }
    }
    private void toGetCodeTask(String s) {
        Call<BeanTool> codeBeanCall = RetrofitUtils.retrofit.create(NpApi.class).getCode(s, "4");
        codeBeanCall.enqueue(new Callback<BeanTool>() {
            @Override
            public void onResponse(Call<BeanTool> call, Response<BeanTool> response) {
                BeanTool codeBean = response.body();
                if (codeBean != null) {
                    if (codeBean.getStatus().equals("0")){
                        initCancleTime();
                        initEndTime();
                    }
                    ToastUtils.show(BindActivity.this, codeBean.getMessage(), Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<BeanTool> call, Throwable t) {
                ToastUtils.show(BindActivity.this, getResources().getString(R.string.not_wlan_show), Toast.LENGTH_SHORT);
                initCancleTime();
                initEndTime();
            }
        });

    }

    private void initEndTime() {
        nRegist = null;
        if (btn_code!=null) {
            btn_code.setClickable(true);
            btn_code.setBackgroundResource(R.drawable.btn_regist_5dp_coding);
            btn_code.setText("获取验证码");
        }
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            initEndTime();
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            btn_code.setText((millisUntilFinished / 1000) + "秒");
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
        toolbar_title.setText("校园通验证绑定");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }
}
