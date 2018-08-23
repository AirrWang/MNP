package com.mm.medicalman.wxapi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mm.medicalman.Api.WfApi;
import com.mm.medicalman.Bean.GetCodeBean;
import com.mm.medicalman.Bean.UnionLoginBean;
import com.mm.medicalman.MmApplication;
import com.mm.medicalman.R;
import com.mm.medicalman.ThirdLoginRegisteActivity;
import com.mm.medicalman.Tool.JpushTool;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.Tool.RetrofitUtils;
import com.mm.medicalman.Tool.SavaDataLocalUtils;
import com.mm.medicalman.Tool.ToastUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mm.medicalman.LoginActivity.loginFinish;

/**
 * Created by NiPing and Airr Wang on 2017/10/24.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static String baseUrl = "https://api.weixin.qq.com/sns/";//baseUrl
    private Retrofit retrofit= new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())//配置gson解析json
            .build();;
    private final static String secret="442cc9e23018b28c501d55fc3fe072c5";
    private static final String APP_ID="wx72d9df8ab76f1af7";
    private ProgressDialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果没回调onResp，八成是这句没有写
        MmApplication.getInstance().api.handleIntent(getIntent(), this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq baseReq) {

    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            //如果errcode为零，表示用户同意授权
            case BaseResp.ErrCode.ERR_OK:
                // 得到所需的code
                //此处报错检查下载的sdk是否正确
                String code = ((SendAuth.Resp) baseResp).code;

//                Log.i("wxentry","code = "+code);
                // 获取code之后携带参数获取用户信息
                getToken(code);
                break;

            default:
                finish();
                break;
        }
    }

    private void getToken(String code) {
        WfApi restApi = retrofit.create(WfApi.class);

        Call<GetCodeBean> callBack = restApi.getCode(APP_ID,secret,code,"authorization_code");

        callBack.enqueue(new Callback<GetCodeBean>() {
            @Override
            public void onResponse(Call<GetCodeBean> call, Response<GetCodeBean> response) {
                String open_id="";
                String access_token="";
                String unionid="";
                access_token=response.body().getAccess_token();
                open_id=response.body().getOpenid();
                unionid=response.body().getUnionid();
                toUnionLogin(open_id,access_token,unionid);
            }

            @Override
            public void onFailure(Call<GetCodeBean> call, Throwable t) {

                MoudleUtils.toChekWifi(WXEntryActivity.this);

            }
        });
    }

    private void toUnionLogin(final String open_id, final String access_token, final String unionid) {
        dialog = new ProgressDialog(this);
        MoudleUtils.dialogShow(dialog);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        Call<UnionLoginBean> callBack = restApi.unionLogin(open_id,access_token,"1",unionid);
        callBack.enqueue(new Callback<UnionLoginBean>() {
            @Override
            public void onResponse(Call<UnionLoginBean> call, Response<UnionLoginBean> response) {

                if (response.body() == null) {
                    return;
                }

                switch (response.body().getStatus()) {
                    case "1":
                        if (response.body().getData().getIs_registered().equals("1")) {
                            SavaDataLocalUtils.saveDataString(WXEntryActivity.this, "token", response.body().getData().getToken());
                            SavaDataLocalUtils.saveDataString(WXEntryActivity.this, "user_id", response.body().getData().getUser_id());
                            JpushTool.mySetAlias(response.body().getData().getUser_id());
                            if (loginFinish != null) {
                                loginFinish.toFinish(true);
                            }
                        }else if (response.body().getData().getIs_registered().equals("2")){
                            Intent intent=new Intent();
                            intent.putExtra("open_id",open_id);
                            intent.putExtra("access_token",access_token);
                            intent.putExtra("unionid",unionid);
                            intent.setClass(WXEntryActivity.this, ThirdLoginRegisteActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case "0":
                        ToastUtils.initLoginAgain(WXEntryActivity.this, response.body().getErr_var());
                        ToastUtils.showShort(WXEntryActivity.this, response.body().getMessage());
                        break;
                }
                MoudleUtils.dialogDismiss(dialog);
                finish();
            }

            @Override
            public void onFailure(Call<UnionLoginBean> call, Throwable t) {
                MoudleUtils.toChekWifi(WXEntryActivity.this);
                MoudleUtils.dialogDismiss(dialog);
                finish();
            }
        });
    }
}
