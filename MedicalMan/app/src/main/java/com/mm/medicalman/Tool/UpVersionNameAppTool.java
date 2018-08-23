package com.mm.medicalman.Tool;

import android.content.Context;
import android.content.Intent;

import com.mm.medicalman.Api.NpApi;
import com.mm.medicalman.Bean.GetNewVersionBean;
import com.mm.medicalman.Mine.MineUpDataActivity;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.Kyloading.KyLoadingBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * /**
 * Created by NiPing and AirrWang on 2017/10/27.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 */

public class UpVersionNameAppTool {
    public static void initTaskUpdata(final KyLoadingBuilder builderUp, final Context context, final String name) {
        if ( builderUp != null) {
            MoudleUtils.kyloadingShow(builderUp, "正在检查新版本...", false, false);
        }
        NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
        Call<GetNewVersionBean> callBack = restApi.update();
        callBack.enqueue(new Callback<GetNewVersionBean>() {
            @Override
            public void onResponse(Call<GetNewVersionBean> call, Response<GetNewVersionBean> response) {
                GetNewVersionBean getuodata = response.body();
                if (getuodata != null) {
                    if (getuodata.getStatus().equals("1")) {
                        initData(builderUp,context, getuodata, name);
                    } else if (getuodata.getStatus().equals("0")) {
                        if (builderUp!=null) {
                            ToastUtils.show(context, getuodata.getMessage(), 0);
                        }
                    }
                }
                if (builderUp!=null) {
                    MoudleUtils.kyloadingDismiss(builderUp);
                }
            }

            @Override
            public void onFailure(Call<GetNewVersionBean> call, Throwable t) {
                if (builderUp!=null) {
                    MoudleUtils.kyloadingDismiss(builderUp);
                }
                ToastUtils.show(context, context.getResources().getString(R.string.not_wlan_show), 0);
            }
        });

    }

    private static void initData(KyLoadingBuilder builderUp,Context context, GetNewVersionBean getuodata, String name) {
        if (getuodata.getData() != null) {
            String versonName = getuodata.getData().getV_name();
            String versonCode = getuodata.getData().getV_num();
            String mVname = MoudleUtils.getVersionName(context);
            String mVcode = MoudleUtils.getVersionCode(context);
            if (!versonName.equals(mVname)) {
                initToUp(context, getuodata, name);
            } else {
                if (!versonCode.equals(mVcode)) {
                    //已有新版本
                    initToUp(context, getuodata, name);
                } else {
                    //已是最新版本
                    if (builderUp!=null) {
                        ToastUtils.showShortNotInternet(context, "已是最新版本");
                    }
                }
            }

        }

    }

    private static void initToUp(Context context, GetNewVersionBean getuodata, String name) {
        //已有新版本
        Intent intent = new Intent();
        intent.putExtra("url", getuodata.getData().getDown_url());
        intent.putExtra("isupdate", getuodata.getData().getIs_must());
        intent.putExtra("verdec", getuodata.getData().getContent());
        intent.putExtra("vername", getuodata.getData().getV_name());
        intent.putExtra("name", name);
        intent.setClass(context, MineUpDataActivity.class);//检查更新
        context.startActivity(intent);
    }
}
