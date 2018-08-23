package com.mm.medicalman;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mm.medicalman.Api.NpApi;
import com.mm.medicalman.Bean.BeanRegister;
import com.mm.medicalman.Bean.BeanTool;
import com.mm.medicalman.Tool.KeyBoardUtils;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.Tool.NetworkUtils;
import com.mm.medicalman.Tool.PwdCheckUtil;
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
 * /**
 * Created by NiPing and AirrWang on 2017/10/23.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 */

public class RegistIsEndActivity extends AppCompatActivity {
    @BindView(R.id.editText)
    EditText et_pwd1;
    @BindView(R.id.editText2)
    EditText et_pwd2;
    @BindView(R.id.button3)
    Button btn_register;
    @BindView(R.id.textView_return)
    TextView textView_return;
    private String userName = "", prove_code = "", type = "1";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_pwd);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        initGetIntentData();
    }

    @OnClick(R.id.textView_return)
    void ToReturn() {
        finish();
    }

    @OnClick(R.id.button3)
    void ToRegister() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            MoudleUtils.toChekWifi(this);
            return;
        }
        String pwd1 = et_pwd1.getText().toString().trim();
        String pwd2 = et_pwd2.getText().toString().trim();
        if (pwd1.equals("") || pwd2.equals("")) {
            ToastUtils.showShort(RegistIsEndActivity.this, "必须填写全部注册信息");
            return;
        }
        if (pwd1.length() < 8 || pwd1.length() > 16) {
            ToastUtils.showShort(RegistIsEndActivity.this, "密码位数为8-16");
            return;
        }
        if (!PwdCheckUtil.isLetterDigit(pwd1)) {
            ToastUtils.showShortNotInternet(RegistIsEndActivity.this, "密码必须同时含有数字和字母");
            return;
        }
        if (!pwd2.equals(pwd1)) {
            ToastUtils.showShort(RegistIsEndActivity.this, "两次密码不一致");
            return;
        }
        switch (type) {
            case "1":
                MoudleUtils.dialogShow(progressDialog);
                initRegisterTask(pwd1);
                break;
            case "2":
                MoudleUtils.dialogShow(progressDialog);
                initForgetPwdTask(pwd1);
                break;
        }
    }

    private void initRegisterTask(String pwd) {
        KeyBoardUtils.closeKeyboard(et_pwd2,RegistIsEndActivity.this);
        Call<BeanRegister> codeBeanCall = RetrofitUtils.retrofit.create(NpApi.class).register(userName, pwd, prove_code);
        codeBeanCall.enqueue(new Callback<BeanRegister>() {
            @Override
            public void onResponse(Call<BeanRegister> call, Response<BeanRegister> response) {
                MoudleUtils.dialogDismiss(progressDialog);
                BeanRegister codeBean = response.body();
                if (codeBean == null) {
                    return;
                }
                if (codeBean.getStatus().equals("1")) {
                    if (codeBean.getData() == null) return;
                    String user_id = codeBean.getData().getUser_id();
                    SPUtils.put(RegistIsEndActivity.this, "user_id", user_id);
                    String token = codeBean.getData().getToken();
                    SPUtils.put(RegistIsEndActivity.this, "token", token);
                    startActivity(new Intent().setClass(RegistIsEndActivity.this, MainActivity.class));
                    finish();
                } else {
                    ToastUtils.show(RegistIsEndActivity.this, codeBean.getMessage(), Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<BeanRegister> call, Throwable t) {
                MoudleUtils.dialogDismiss(progressDialog);
                ToastUtils.show(RegistIsEndActivity.this, getResources().getString(R.string.not_wlan_show), Toast.LENGTH_SHORT);
            }
        });
    }

    private void initForgetPwdTask(String pwd) {
        KeyBoardUtils.closeKeyboard(et_pwd2,RegistIsEndActivity.this);
        Call<BeanTool> codeBeanCall = RetrofitUtils.retrofit.create(NpApi.class).findPwd(userName, pwd, prove_code);
        codeBeanCall.enqueue(new Callback<BeanTool>() {
            @Override
            public void onResponse(Call<BeanTool> call, Response<BeanTool> response) {
                MoudleUtils.dialogDismiss(progressDialog);
                BeanTool codeBean = response.body();
                if (codeBean == null) {
                    return;
                }
                ToastUtils.show(RegistIsEndActivity.this, codeBean.getMessage(), Toast.LENGTH_SHORT);
                if (codeBean.getStatus().equals("1")) {
                    startActivity(new Intent().setClass(RegistIsEndActivity.this, LoginActivity.class));
                    finish();
                }
            }


            @Override
            public void onFailure(Call<BeanTool> call, Throwable t) {
                MoudleUtils.dialogDismiss(progressDialog);
                ToastUtils.show(RegistIsEndActivity.this, getResources().getString(R.string.not_wlan_show), Toast.LENGTH_SHORT);
            }
        });

    }

    /**
     * 接收注册接口post传输参数时的参数值
     */
    private void initGetIntentData() {
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        prove_code = intent.getStringExtra("code");
        type = intent.getStringExtra("type");
    }
}
