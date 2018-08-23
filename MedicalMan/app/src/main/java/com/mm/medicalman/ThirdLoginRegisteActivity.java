package com.mm.medicalman;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mm.medicalman.Api.NpApi;
import com.mm.medicalman.Api.WfApi;
import com.mm.medicalman.Bean.BeanTool;
import com.mm.medicalman.Bean.UnionLoginBean;
import com.mm.medicalman.Home.AchievementActivity;
import com.mm.medicalman.Tool.AppConfig;
import com.mm.medicalman.Tool.JpushTool;
import com.mm.medicalman.Tool.KeyBoardUtils;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.Tool.RetrofitUtils;
import com.mm.medicalman.Tool.SavaDataLocalUtils;
import com.mm.medicalman.Tool.ToastUtils;
import com.mm.medicalman.wxapi.WXEntryActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mm.medicalman.LoginActivity.loginFinish;

/**
 * Created by NiPing and Airr Wang on 2017/10/30.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class ThirdLoginRegisteActivity extends AppCompatActivity {
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
    @BindView(R.id.et_login_psw)
    EditText et_login_psw;
    @BindView(R.id.tv_1)
    TextView tv_1;
    @BindView(R.id.tv_registration_protocol)
    TextView tv_registration_protocol;

    private ProgressDialog progressDialog;
    private TimeCount nRegist;
    private String open_id;
    private String access_token;
    private String unionid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_register);
        ButterKnife.bind(this);
        initGet();
        progressDialog = new ProgressDialog(this);
        tv_registration_protocol.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv_registration_protocol.getPaint().setAntiAlias(true);//抗锯齿
    }

    private void initGet() {
        Intent intent = getIntent();
        open_id = intent.getStringExtra("open_id");
        access_token = intent.getStringExtra("access_token");
        unionid = intent.getStringExtra("unionid");
    }
    @OnClick(R.id.tv_1)
    void ToLogin(){
        Intent intent=new Intent();
        intent.putExtra("open_id",open_id);
        intent.putExtra("access_token",access_token);
        intent.putExtra("unionid",unionid);
        intent.setClass(ThirdLoginRegisteActivity.this, ThirdLoginActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.tv_registration_protocol)
    void registrationProtocol() {
        Intent intent = new Intent();
        intent.putExtra("acurl", AppConfig.h5Protocol);
        intent.setClass(ThirdLoginRegisteActivity.this, AcH5Activity.class);
        startActivity(intent);
    }
    @OnClick(R.id.textView_return)
    void ToReturn() {
        finish();
    }

    @OnClick(R.id.btn_code)
    void initSetCode() {
        getCode();
    }

    @OnClick(R.id.button3)
    void thirdRegister() {
        toRegistData();
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
        nRegist = new ThirdLoginRegisteActivity.TimeCount(60000, 1000);//开始计时60s
        nRegist.start();
        toGetCodeTask(s);
    }
    private void toGetCodeTask(String s) {
        Call<BeanTool> codeBeanCall = RetrofitUtils.retrofit.create(NpApi.class).getCode(s, "1");
        codeBeanCall.enqueue(new Callback<BeanTool>() {
            @Override
            public void onResponse(Call<BeanTool> call, Response<BeanTool> response) {
                BeanTool codeBean = response.body();
                if (codeBean != null) {
                    ToastUtils.show(ThirdLoginRegisteActivity.this, codeBean.getMessage(), Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<BeanTool> call, Throwable t) {
                ToastUtils.show(ThirdLoginRegisteActivity.this, getResources().getString(R.string.not_wlan_show), Toast.LENGTH_SHORT);
            }
        });

    }
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            nRegist = null;
            btn_code.setClickable(true);
            btn_code.setBackgroundResource(R.drawable.btn_regist_5dp_coding);
            btn_code.setText("获取验证码");
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            btn_code.setText((millisUntilFinished / 1000) + "秒");
        }
    }

    private void toRegistData() {
        String userName = et_tel.getText().toString().trim();
        if (userName.equals("") || et_code.getText().toString().trim().equals("") || et_login_psw.getText().toString().trim().equals("")) {
            ToastUtils.showShort(this, "必须填写全部注册信息");
            return;
        }
        if (!MoudleUtils.isPhone(userName)) {
            ToastUtils.showShort(this, "手机号码格式不正确");
            return;
        }
        MoudleUtils.dialogShow(progressDialog);
        toGetCodeTaskIsTrue(userName, et_login_psw.getText().toString().trim(), et_code.getText().toString().trim());
    }

    private void toGetCodeTaskIsTrue(final String userName, String psw, final String prove_code) {
        KeyBoardUtils.closeKeyboard(et_code, ThirdLoginRegisteActivity.this);
        Call<UnionLoginBean> codeBeanCall = RetrofitUtils.retrofit.create(WfApi.class).unionRegister(open_id, access_token, "1", unionid, userName, psw, prove_code);
        codeBeanCall.enqueue(new Callback<UnionLoginBean>() {
            @Override
            public void onResponse(Call<UnionLoginBean> call, Response<UnionLoginBean> response) {
                MoudleUtils.dialogDismiss(progressDialog);
                UnionLoginBean codeBean = response.body();
                if (codeBean == null) {
                    return;
                }
                if (codeBean.getStatus().equals("1")) {
                    //TODO
                    SavaDataLocalUtils.saveDataString(ThirdLoginRegisteActivity.this, "token", response.body().getData().getToken());
                    SavaDataLocalUtils.saveDataString(ThirdLoginRegisteActivity.this, "user_id", response.body().getData().getUser_id());
                    JpushTool.mySetAlias(codeBean.getData().getUser_id());
                    if (loginFinish != null) {
                        loginFinish.toFinish(true);
                    }
                    finish();
                } else {
                    ToastUtils.show(ThirdLoginRegisteActivity.this, codeBean.getMessage(), Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<UnionLoginBean> call, Throwable t) {
                MoudleUtils.dialogDismiss(progressDialog);
                ToastUtils.show(ThirdLoginRegisteActivity.this, getResources().getString(R.string.not_wlan_show), Toast.LENGTH_SHORT);
            }
        });

    }
}
