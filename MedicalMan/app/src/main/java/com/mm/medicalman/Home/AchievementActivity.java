package com.mm.medicalman.Home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
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
import com.mm.medicalman.Bean.GetscoresBean;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.KeyBoardUtils;
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
 * Created by NiPing and Airr Wang on 2017/10/31.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class AchievementActivity extends AppCompatActivity{

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

    private ProgressDialog progressDialog;
    private TimeCount nRegist;
    private String userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);
        ButterKnife.bind(this);
        initToolbar();
        progressDialog = new ProgressDialog(this);
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
        userName = et_tel.getText().toString().trim();
        if (userName.equals("") || et_code.getText().toString().trim().equals("") || et_login_psw.getText().toString().trim().equals("")) {
            ToastUtils.showShort(this, "必须填写全部信息");
            return;
        }
        if (!MoudleUtils.isPhone(userName)) {
            ToastUtils.showShort(this, "手机号码格式不正确");
            return;
        }
        MoudleUtils.dialogShow(progressDialog);
        toGetScores();
    }

    private void toGetScores() {
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        String user_id = (String) SPUtils.get("user_id", "");
        String token = (String) SPUtils.get("token", "");
        Call<GetscoresBean> callBack = restApi.getscores(user_id,token,et_tel.getText().toString().trim(),et_code.getText().toString().trim(),et_login_psw.getText().toString().trim());

        callBack.enqueue(new Callback<GetscoresBean>() {
            @Override
            public void onResponse(Call<GetscoresBean> call, Response<GetscoresBean> response) {
                if (response.body()!=null){
                    if (response.body().getStatus().equals("0")){
                        ToastUtils.initLoginAgain(AchievementActivity.this,response.body().getErr_var());
                        ToastUtils.showShort(AchievementActivity.this, response.body().getMessage());
                    }else if (response.body().getStatus().equals("1")){
                        SPUtils.put(AchievementActivity.this,"tel",et_tel.getText().toString().trim());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("info", response.body().getData());
                        startActivity(new Intent().putExtras(bundle).setClass(AchievementActivity.this,QuerryScoreActivity.class));
                        KeyBoardUtils.closeKeyboard(et_code,AchievementActivity.this);
                        finish();
                    }
                }
                MoudleUtils.dialogDismiss(progressDialog);
            }

            @Override
            public void onFailure(Call<GetscoresBean> call, Throwable t) {
                MoudleUtils.toChekWifi(AchievementActivity.this);
                MoudleUtils.dialogDismiss(progressDialog);
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
    private void toGetCodeTask(String s) {
        Call<BeanTool> codeBeanCall = RetrofitUtils.retrofit.create(NpApi.class).getCode(s, "3");
        codeBeanCall.enqueue(new Callback<BeanTool>() {
            @Override
            public void onResponse(Call<BeanTool> call, Response<BeanTool> response) {
                BeanTool codeBean = response.body();
                if (codeBean != null) {
                    if (codeBean.getStatus().equals("0")){
                        initCancleTime();
                        initEndTime();
                    }
                    ToastUtils.show(AchievementActivity.this, codeBean.getMessage(), Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<BeanTool> call, Throwable t) {
                ToastUtils.show(AchievementActivity.this, getResources().getString(R.string.not_wlan_show), Toast.LENGTH_SHORT);
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
        toolbar_title.setText("学生成绩查询系统");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }
}
