package com.mm.medicalman;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mm.medicalman.Api.WfApi;
import com.mm.medicalman.Bean.LoginBean;
import com.mm.medicalman.Interface.LoginFinish;
import com.mm.medicalman.Tool.JpushTool;
import com.mm.medicalman.Tool.KeyBoardUtils;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.Tool.RetrofitUtils;
import com.mm.medicalman.Tool.SavaDataLocalUtils;
import com.mm.medicalman.Tool.ToastUtils;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang on 2017/10/23.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_login_tel;
    private EditText et_login_psw;
    private TextView tv_forget;
    private Button btn_login;
    private TextView tv_register;
    private TextView tv_weixin_login;
    private ProgressDialog dialog;
    public static LoginFinish loginFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initId();
        loginFinish = new LoginFinish() {
            @Override
            public void toFinish(boolean b) {
                if (b) {
                    startActivity(new Intent().setClass(LoginActivity.this, MainActivity.class));
                    LoginActivity.this.finish();
                }
            }
        };
    }

    private void initId() {
        dialog = new ProgressDialog(this);
        et_login_tel = (EditText) findViewById(R.id.et_login_tel);
        et_login_psw = (EditText) findViewById(R.id.et_login_psw);
        tv_forget = (TextView) findViewById(R.id.tv_forget);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_weixin_login = (TextView) findViewById(R.id.tv_weixin_login);

        tv_forget.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_weixin_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget:
                startActivity(new Intent().putExtra("type", "2").setClass(this, RegisteredActivity.class));
                break;
            case R.id.btn_login:
                String s_tel = et_login_tel.getText().toString().trim();
                String s_pd = et_login_psw.getText().toString().trim();
                if (s_tel != null && s_pd != null) {
                    if (!s_tel.equals("") && !s_pd.equals("")) {
                        login(s_tel, s_pd);
                    } else {
                        ToastUtils.showShort(LoginActivity.this, "请输入手机号和密码");
                    }
                } else {
                    ToastUtils.showShort(LoginActivity.this, "请输入手机号和密码");
                }
                break;
            case R.id.tv_register:
                startActivity(new Intent().putExtra("type", "1").setClass(this, RegisteredActivity.class));
                break;
            case R.id.tv_weixin_login:
                wxLogin();
                break;

            default:
                break;
        }
    }

    private void wxLogin() {
        if (!MmApplication.getInstance().api.isWXAppInstalled()) {
            ToastUtils.showShort(this, "您还未安装微信客户端");
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        MmApplication.getInstance().api.sendReq(req);

    }

    /**
     * 执行登录接口
     *
     * @param s_tel
     * @param s_pd
     */

    private void login(String s_tel, String s_pd) {
        if (!MoudleUtils.isPhone(et_login_tel.getText().toString().trim())) {
            ToastUtils.showShort(this, "手机号不正确");
            return;
        } else if (et_login_psw.getText().toString().trim().equals("")) {
            ToastUtils.showShort(this, "密码不能为空");
            return;
        }
        MoudleUtils.dialogShow(dialog);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);

        Call<LoginBean> callBack = restApi.login(s_tel, s_pd);

        callBack.enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                LoginBean loginBean = response.body();
                toLoginFinishNext(loginBean);//登录成功后的操作
                MoudleUtils.dialogDismiss(dialog);//使登录按钮可按
            }

            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {

                MoudleUtils.toChekWifi(LoginActivity.this);
                MoudleUtils.dialogDismiss(dialog);//使登录按钮可按

            }
        });
    }

    /**
     * 原生手机号登录成功后操作
     *
     * @param loginBean
     */
    private void toLoginFinishNext(LoginBean loginBean) {
        if (loginBean != null) {
            if (loginBean.getStatus().equals("1")) {
                KeyBoardUtils.closeKeyboard(et_login_psw, this);
                toUserInfo(loginBean);
            } else {
                ToastUtils.show(LoginActivity.this, loginBean.getMessage(), 0);
            }
        } else {

        }
    }

    private void toUserInfo(LoginBean loginBean) {
        initSaveLoginData(loginBean);
        JpushTool.mySetAlias(loginBean.getData().getUser_id());
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void initSaveLoginData(LoginBean loginBean) {
        SavaDataLocalUtils.saveDataString(this, "token", loginBean.getData().getToken());
        SavaDataLocalUtils.saveDataString(this, "user_id", loginBean.getData().getUser_id());
    }


    /**
     * 锁定登录返回键，使其失效
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            MoudleUtils.onBackPressed(this);
            try {
                moveTaskToBack(false);
            } catch (Exception e) {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
