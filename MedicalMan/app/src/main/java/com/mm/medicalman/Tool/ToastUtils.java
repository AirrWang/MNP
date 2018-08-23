package com.mm.medicalman.Tool;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import com.mm.medicalman.LoginActivity;
import com.mm.medicalman.Tool.MyToast.ToastCompat;

import static com.mm.medicalman.MainActivity.tokenErr;
import static com.mm.medicalman.Tool.DBUtil.DBTools.deleteDb;


/**
 * Created by NiPing and Airr Wang
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class ToastUtils {

    private static AlertDialog alertDialog = null;

    private ToastUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    public static boolean isShow = true;


    public static void showShort(Context context, String message) {
        try {
            if (isShow) {
                initToShowMsg(context, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 一些于界面无关的提示可以用这个，慎重使用
     *
     * @param message
     */
    public static void showShortNotInternet(Context context, String message) {
        try {
            if (isShow) {
                initToShowMsg(context, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initToShowMsg(Context context, String message) {
        ToastCompat.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    public static void show(Context context, String message, int duration) {
        try {
            if (isShow) {
                initToShowMsg(context, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重新登录的弹框提示
     */
    public synchronized static void initLoginAgain(final Context context, String err_var) {
        if (err_var == null || !err_var.equals("token")) return;
        if (alertDialog == null && context != null) {
            alertDialog = new AlertDialog.Builder(context).setTitle("请重新登录")
                    .setMessage("1.可能此账号已在其他设备登录\n\n2.可能清除了用户账户信息等等")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            JpushTool.mySetAlias("");
                            deleteDb();
                            SPUtils.clear(context);
                            alertDialog = null;
                            if (tokenErr != null) {
                                tokenErr.toFinishMain(true);
                            }
                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        }

                    }).create(); // 创建对话框
            if (alertDialog != null) {
                alertDialog.setCancelable(false);
                if (!alertDialog.isShowing()) {
                    alertDialog.show(); // 显示
                }
            }
        }
    }
}
