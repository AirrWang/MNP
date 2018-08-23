package com.mm.medicalman;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.mm.medicalman.Interface.AppStatusTracker;
import com.mm.medicalman.Tool.DBUtil.DbCore;
import com.mm.medicalman.Tool.RetrofitUtils;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by NiPing and Airr Wang on 2017/10/23.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class MmApplication extends MultiDexApplication {
    private static MmApplication instance;
    private static final String APP_ID="wx72d9df8ab76f1af7";//
    public IWXAPI api;

    @Override
    public void onCreate() {
        super.onCreate();
        AppStatusTracker.init(this);
        instance=this;
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this, config);
        RetrofitUtils.init();//网络框架初始化
        //注册到微信
        api= WXAPIFactory.createWXAPI(this,APP_ID,true);
        api.registerApp(APP_ID);
        //初始化数据库
        DbCore.init(this);
        /**
         * 开启极光推送
         */
        JPushInterface.init(this);

    }

    public static MmApplication getInstance() {
        // 因为我们程序运行后，Application是首先初始化的，如果在这里不用判断instance是否为空
        return instance;
    }
}
