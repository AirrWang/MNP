package com.mm.medicalman.Tool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.cache.common.CacheKey;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.Kyloading.KyLoadingBuilder;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mm.medicalman.Tool.HeadImageChangeTool.downLoadImage;
import static com.mm.medicalman.Tool.HeadImageChangeTool.getCachedImageOnDisk;

/**
 * Created by NiPing and Airr Wang on 2017/10/23.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class MoudleUtils {
    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }
    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }
    public static void textViewSetText(TextView textView, String s) {
        if (s == null) {
            s = "";
        }
        if (textView != null) {
            textView.setText(s);
        }
    }
    /**
     * et.setText(s);
     * et.setSelection(s.length());
     *
     * @param et
     * @param s
     */
    public static void editTextViewSetText(EditText et, String s) {
        if (s == null) {
            s = "";
        }
        if (et != null) {
            et.setText(s);
            et.setSelection(s.length());
        }
    }

    /**
     * 加载提示框 显示
     *
     * @param dialog
     */
    public static void dialogShow(ProgressDialog dialog) {
        try {

            if (dialog != null) {
                dialog.setTitle("正在加载...请耐心等待");
                dialog.setCancelable(false);
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载提示框 隐藏
     *
     * @param dialog
     */
    public static void dialogDismiss(ProgressDialog dialog) {
        try {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 网络不好提示
     *
     * @param context
     */
    public static void toChekWifi(Context context) {
        try {
            ToastUtils.showShort(context, context.getResources().getString(R.string.not_wlan_show));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* 检测手机号码格式
    *
    * @param str
    * @return
    */
    public static boolean isPhone(String str) {
        Pattern pattern = Pattern.compile("1[0-9]{10}");
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置webView为透明背景，和布局中的WebView的背景设为透明设同时使用才生效
     *
     * @param webViewLicense
     */
    public static void NoBgWebView(WebView webViewLicense) {
        if (webViewLicense != null) {
            webViewLicense.setBackgroundColor(0);
            webViewLicense.getBackground().setAlpha(0);
        }
    }
    /**
     * 下拉刷新控件隐藏
     *
     * @param swipeRefreshLayoutTj
     */
    public static void initSwipeRefreshLayoutFalse(SwipeRefreshLayout swipeRefreshLayoutTj) {
        if (swipeRefreshLayoutTj != null) {
            if (swipeRefreshLayoutTj.isRefreshing()) {
                swipeRefreshLayoutTj.setRefreshing(false);
            }
        }
    }

    /**
     * 下来刷新控件出现
     *
     * @param swipeRefreshLayoutTj
     */
    public static void initRefrushTrue(SwipeRefreshLayout swipeRefreshLayoutTj) {
        if (swipeRefreshLayoutTj != null) {
            if (!swipeRefreshLayoutTj.isRefreshing()) {
                swipeRefreshLayoutTj.setRefreshing(true);
            }
        }
    }

    /**
     * 下拉刷新控件不可用
     *
     * @param swipeRefreshLayoutTj
     */
    public static void initF(SwipeRefreshLayout swipeRefreshLayoutTj) {
        if (swipeRefreshLayoutTj != null) {
            if (swipeRefreshLayoutTj.isEnabled()) {
                swipeRefreshLayoutTj.setEnabled(false);
            }
        }
    }

    /**
     * 下拉刷新控件可用
     *
     * @param swipeRefreshLayoutTj
     */
    public static void initT(SwipeRefreshLayout swipeRefreshLayoutTj) {
        if (swipeRefreshLayoutTj != null) {
            if (!swipeRefreshLayoutTj.isEnabled()) {
                swipeRefreshLayoutTj.setEnabled(true);
            }
        }
    }
    /************************
     * 高斯模糊处理
     * @param bitmap
     * @param context
     * @return
     ***********************/

    public static Bitmap blurBitmap(Bitmap bitmap, Context context, float radius) {

        // 用需要创建高斯模糊bitmap创建一个空的bitmap
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        // 初始化Renderscript，该类提供了RenderScript context，创建其他RS类之前必须先创建这个类，其控制RenderScript的初始化，资源管理及释放
        RenderScript rs = RenderScript.create(context);

        // 创建高斯模糊对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // 创建Allocations，此类是将数据传递给RenderScript内核的主要方法，并制定一个后备类型存储给定类型
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //设定模糊度(注：Radius最大只能设置25.f)
        blurScript.setRadius(radius);

        // Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        // Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        // After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return outBitmap;
    }
    /**
     * Gone隐藏View
     *
     * @param view
     */
    public static void viewGone(View view) {
        if (view != null) {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Invisibily隐藏View
     *
     * @param view
     */
    public static void viewInvisibily(View view) {
        if (view != null) {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 显示View
     *
     * @param view
     */
    public static void viewShow(View view) {
        if (view != null) {
            if (view.getVisibility() != View.VISIBLE) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }
    /**
     * 载入提示框 显示
     * 可以动态改变提示内容
     * 动态控制点击空白区域是否关闭flag为true时关闭，
     * 按返回键是否关闭flagTwo为true时关闭
     * 每次show之前都要new一下，否则无效
     *
     * @param
     */
    public static void kyloadingShow(KyLoadingBuilder builder, String content, boolean flag, boolean flagTwo) {
        try {
            if (builder != null) {
                builder.setIcon(R.drawable.upload_again);
                builder.setText(content);
                builder.setOutsideTouchable(flag);//点击空白区域是否关闭 false不能关
                builder.setBackTouchable(flagTwo);//按返回键是否关闭 false不能关
                builder.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 载入提示框 显示
     *
     * @param
     */
    public static void kyloadingShow(KyLoadingBuilder builder) {
        try {
            if (builder != null) {
                builder.setIcon(R.drawable.upload_again);
                builder.setText("...请稍后...");
                builder.setOutsideTouchable(false);//点击空白区域是否关闭
                builder.setBackTouchable(false);//按返回键是否关闭
                builder.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 载入提示框 隐藏
     *
     * @param builder
     */
    public static void kyloadingDismiss(KyLoadingBuilder builder) {
        try {
            if (builder != null) {
                builder.dismiss();//关闭
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static String getVersionCode(Context context) {
        return String.valueOf(getPackageInfo(context).versionCode);
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }
    /**
     * 防止华为机型未加入白名单时按返回键回到桌面再锁屏后几秒钟进程被杀
     */
    public static void onBackPressed(Activity a) {
        try {
            Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
            launcherIntent.addCategory(Intent.CATEGORY_HOME);
            a.startActivity(launcherIntent);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
