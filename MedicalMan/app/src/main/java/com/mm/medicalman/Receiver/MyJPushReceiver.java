package com.mm.medicalman.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mm.medicalman.AcH5PostActivity;
import com.mm.medicalman.Interface.AppStatusTracker;
import com.mm.medicalman.MainActivity;
import com.mm.medicalman.MmApplication;
import com.mm.medicalman.Tool.SPUtils;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by NiPing and Airr Wang
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */

public class MyJPushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
//        Log.d(TAG, "onReceive - " + intent.getAction());

        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {

            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            System.out.println("收到了extra自定义消息@@消息内容是:" + extra);
            if (MmApplication.getInstance() == null) {
                Intent launchIntent = context.getPackageManager().
                        getLaunchIntentForPackage("com.mm.medicalman");
                launchIntent.setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                context.startActivity(launchIntent);
            } else {
                if (!(SPUtils.get("token", "") + "").equals("")) {
                    if (!AppStatusTracker.getInstance().isForground()) {
                        Intent launchIntent = new Intent();
                        launchIntent.setFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                        launchIntent.setClass(context, MainActivity.class);
                        context.startActivity(launchIntent);
                    }
                    try {
                        JSONObject jsonObject;
                        jsonObject = new JSONObject(extra);
                        String type = jsonObject.getString("type");
                        switch (type) {
                            case "h5":
                            case "version":
                                String url = jsonObject.getString("url");
                                Intent intentUrl = new Intent();
                                intentUrl.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intentUrl.putExtra("acurl", url);
                                String user_id = (String) (SPUtils.get(context, "user_id", ""));
                                String token = (String) SPUtils.get(context, "token", "");
                                intentUrl.putExtra("urlParameter", "token=" + token + "&" + "user_id=" + user_id);
                                intentUrl.setClass(context, AcH5PostActivity.class);
                                context.startActivity(intentUrl);
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Intent launchIntent = context.getPackageManager().
                            getLaunchIntentForPackage("com.mm.medicalman");
                    launchIntent.setFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    context.startActivity(launchIntent);
                }
            }

        } else if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {


        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
                .getAction())) {
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
//            String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//            String title = bundle.getString(JPushInterface.EXTRA_TITLE);
//            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
//
//            System.out.println("收到了自定义消息@@消息内容是:" + content);
//            System.out.println("收到了自定义消息@@消息title是:" + title);
//            System.out.println("收到了自定义消息@@消息extra是:" + extra);
            //**************解析推送过来的json数据并存放到集合中 begin******************


            //**************解析推送过来的json数据并存放到集合中 end******************

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
                .getAction())) {
//            System.out.println("收到了通知");
            // 在这里可以做些统计，或者做些其他工作
        } else {
//            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }
}

