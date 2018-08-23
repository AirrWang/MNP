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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mm.medicalman.LoginActivity.loginFinish;

/**
 * Created by NiPing and Airr Wang on 2017/10/23.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class ThirdLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_login_tel;
    private EditText et_login_psw;
    private Button btn_login;
    private ProgressDialog dialog;
    private TextView textView_return;
    private String open_id;
    private String access_token;
    private String unionid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_third);
        initId();
        Intent intent = getIntent();
        open_id = intent.getStringExtra("open_id");
        access_token = intent.getStringExtra("access_token");
        unionid = intent.getStringExtra("unionid");
    }

    private void initId() {
        dialog = new ProgressDialog(this);
        et_login_tel = (EditText) findViewById(R.id.et_login_tel);
        et_login_psw = (EditText) findViewById(R.id.et_login_psw);
        btn_login = (Button) findViewById(R.id.btn_login);

        btn_login.setOnClickListener(this);
        textView_return = (TextView) findViewById(R.id.textView_return);
        textView_return.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                String s_tel = et_login_tel.getText().toString().trim();
                String s_pd = et_login_psw.getText().toString().trim();
                if (s_tel != null && s_pd != null) {
                    if (!s_tel.equals("") && !s_pd.equals("")) {
                        login(s_tel, s_pd);
                    } else {
                        ToastUtils.showShort(ThirdLoginActivity.this, "请输入手机号和密码");
                    }
                } else {
                    ToastUtils.showShort(ThirdLoginActivity.this, "请输入手机号和密码");
                }
                break;
            case R.id.textView_return:
                finish();
                break;
            default:
                break;
        }
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

        Call<LoginBean> callBack = restApi.unionBind(open_id, access_token, "1", unionid, s_tel, s_pd);

        callBack.enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                LoginBean loginBean = response.body();
                toLoginFinishNext(loginBean);//登录成功后的操作
                MoudleUtils.dialogDismiss(dialog);//使登录按钮可按
            }

            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {

                MoudleUtils.toChekWifi(ThirdLoginActivity.this);
                MoudleUtils.dialogDismiss(dialog);//使登录按钮可按

            }
        });
    }

    /**
     * 登录成功后操作
     *
     * @param loginBean
     */
    private void toLoginFinishNext(LoginBean loginBean) {
        if (loginBean != null) {
            if (loginBean.getStatus().equals("1")) {
                KeyBoardUtils.closeKeyboard(et_login_psw, this);
                toUserInfo(loginBean);
            } else {
                ToastUtils.show(ThirdLoginActivity.this, loginBean.getMessage(), 0);
            }
        } else {

        }
    }

    private void toUserInfo(LoginBean loginBean) {
        initSaveLoginData(loginBean);
        JpushTool.mySetAlias(loginBean.getData().getUser_id());
        if (loginFinish != null) {
            loginFinish.toFinish(true);
        }
        finish();
    }

    private void initSaveLoginData(LoginBean loginBean) {
        SavaDataLocalUtils.saveDataString(this, "token", loginBean.getData().getToken());
        SavaDataLocalUtils.saveDataString(this, "user_id", loginBean.getData().getUser_id());
    }


}
