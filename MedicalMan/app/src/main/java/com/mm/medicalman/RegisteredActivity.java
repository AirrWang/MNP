package com.mm.medicalman;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mm.medicalman.Api.NpApi;
import com.mm.medicalman.Bean.BeanTool;
import com.mm.medicalman.Mine.MineSetActivity;
import com.mm.medicalman.Tool.AppConfig;
import com.mm.medicalman.Tool.KeyBoardUtils;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.Tool.NetworkUtils;
import com.mm.medicalman.Tool.RetrofitUtils;
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
public class RegisteredActivity extends AppCompatActivity {
    @BindView(R.id.editText)
    EditText et_tel;
    @BindView(R.id.editText2)
    EditText et_code;
    @BindView(R.id.button3)
    Button btn_register;
    @BindView(R.id.btn_code)
    Button btn_code;
    @BindView(R.id.textView_return)
    TextView textView_return;
    @BindView(R.id.r_registration_protocol)
    RelativeLayout r_registration_protocol;
    private TimeCount nRegist;
    private String userName = "", prove_code = "";
    private String type = "1";//验证码类型,注册:1,找回密码:2
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        initBtnTitle();
        initToCode();
    }

    @OnClick(R.id.r_registration_protocol)
    void registrationProtocol() {
        Intent intent = new Intent();
        intent.putExtra("acurl", AppConfig.h5Protocol);
        intent.setClass(RegisteredActivity.this, AcH5Activity.class);
        startActivity(intent);
    }

    @OnClick(R.id.textView_return)
    void ToReturn() {
        finish();
    }

    private void initBtnTitle() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        switch (type) {
            case "1":
                MoudleUtils.viewShow(r_registration_protocol);
                btn_register.setText("立即注册");
                break;
            case "2":
                MoudleUtils.viewGone(r_registration_protocol);
                btn_register.setText("下一步");
                break;
        }
    }

    @OnClick(R.id.btn_code)
    void initSetCode() {
        getCode();
    }

    @OnClick(R.id.button3)
    void initSetOk() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            MoudleUtils.toChekWifi(this);
            return;
        }
        prove_code = et_code.getText().toString().trim();
        toRegistData(prove_code);
    }

    /**
     * 监听EditText框中的变化
     */
    private void initToCode() {
        et_code.addTextChangedListener(new TextWatcher() {
            /**
             * 文本变化之前
             * @param s
             * @param start
             * @param count
             * @param after
             */
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            /**
             * 文本变化中
             * @param s
             * @param start
             * @param before
             * @param count
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {
                    toRegistData(s.toString());
                }
            }

            /**
             * 文本变化之后
             * @param s
             */
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getCode() {
        if (et_tel == null || nRegist != null) return;
        String s = et_tel.getText().toString().trim();
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

    private void initEndTime() {
        nRegist = null;
        if (btn_code!=null) {
            btn_code.setClickable(true);
            btn_code.setBackgroundResource(R.drawable.btn_regist_5dp_coding);
            btn_code.setText("获取验证码");
        }
    }

    private void toGetCodeTask(String s) {
        Call<BeanTool> codeBeanCall = RetrofitUtils.retrofit.create(NpApi.class).getCode(s, type);
        codeBeanCall.enqueue(new Callback<BeanTool>() {
            @Override
            public void onResponse(Call<BeanTool> call, Response<BeanTool> response) {
                BeanTool codeBean = response.body();
                if (codeBean != null) {
                    if (codeBean.getStatus().equals("0")){
                        initCancleTime();
                        initEndTime();
                    }
                    ToastUtils.show(RegisteredActivity.this, codeBean.getMessage(), Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<BeanTool> call, Throwable t) {
                ToastUtils.show(RegisteredActivity.this, getResources().getString(R.string.not_wlan_show), Toast.LENGTH_SHORT);
                initCancleTime();
                initEndTime();
            }
        });

    }

    private void toRegistData(String prove_code) {
        userName = et_tel.getText().toString().trim();
        if (userName.equals("") || prove_code.equals("")) {
            ToastUtils.showShort(this, "必须填写全部注册信息");
            return;
        }
        if (!MoudleUtils.isPhone(userName)) {
            ToastUtils.showShort(this, "手机号码格式不正确");
            return;
        }
        MoudleUtils.dialogShow(progressDialog);
        toGetCodeTaskIsTrue(userName, prove_code);
    }

    private void toGetCodeTaskIsTrue(final String userName, final String prove_code) {
        KeyBoardUtils.closeKeyboard(et_code, RegisteredActivity.this);
        Call<BeanTool> codeBeanCall = RetrofitUtils.retrofit.create(NpApi.class).getCodeIsTure(userName, type, prove_code);
        codeBeanCall.enqueue(new Callback<BeanTool>() {
            @Override
            public void onResponse(Call<BeanTool> call, Response<BeanTool> response) {
                MoudleUtils.dialogDismiss(progressDialog);
                BeanTool codeBean = response.body();
                if (codeBean == null) {
                    return;
                }
                if (codeBean.getStatus().equals("1")) {
                    Intent intent = new Intent();
                    intent.putExtra("userName", userName);
                    intent.putExtra("code", prove_code);
                    intent.putExtra("type", type);
                    startActivity(intent.setClass(RegisteredActivity.this, RegistIsEndActivity.class));
                } else {
                    ToastUtils.show(RegisteredActivity.this, codeBean.getMessage(), Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<BeanTool> call, Throwable t) {
                MoudleUtils.dialogDismiss(progressDialog);
                ToastUtils.show(RegisteredActivity.this, getResources().getString(R.string.not_wlan_show), Toast.LENGTH_SHORT);
            }
        });

    }

}
