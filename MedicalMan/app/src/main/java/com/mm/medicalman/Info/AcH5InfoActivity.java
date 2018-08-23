package com.mm.medicalman.Info;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mm.medicalman.Api.WfApi;
import com.mm.medicalman.Bean.BeanTool;
import com.mm.medicalman.Bean.CollectStatusBean;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.AppConfig;
import com.mm.medicalman.Tool.FrescoUtils;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.Tool.RetrofitUtils;
import com.mm.medicalman.Tool.SPUtils;
import com.mm.medicalman.Tool.ToastUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mm.medicalman.Tool.MoudleUtils.NoBgWebView;

/**
 * Created by NiPing and Airr Wang
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 * 普通h5跳转
 */
public class AcH5InfoActivity extends AppCompatActivity implements View.OnClickListener {
    private WebView webViewLicense;
    private String url = "";//加载的h5页面的网址
    private String article_id="";
    private ProgressBar progressBar;
    private int s=0;
    private SimpleDraweeView toolbar_sd;
    private ProgressDialog dialog;
    private String collect="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac_h5_zx);
        toolbar_sd = (SimpleDraweeView) findViewById(R.id.toolbar_sd);
        toolbar_sd.setOnClickListener(this);
        initToolbar();
        initId();
        NoBgWebView(webViewLicense);
        initGetUrl();
        initData();
        initSet();
    }

    private void initData() {
        String user_id = (String) SPUtils.get("user_id", "");
        String token = (String) SPUtils.get("token", "");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        Call<CollectStatusBean> callBack = restApi.collectStatus(user_id,token, Integer.parseInt(article_id));

        callBack.enqueue(new Callback<CollectStatusBean>() {

            @Override
            public void onResponse(Call<CollectStatusBean> call, Response<CollectStatusBean> response) {
                CollectStatusBean bean = response.body();
                if (bean == null) {
                    finish();
                    return;
                }
                if (bean.getStatus().equals("1")) {
                    if (bean.getData().getStatus()==0){  //未收藏
                        FrescoUtils.setImage(toolbar_sd, AppConfig.res+R.drawable.information_collect_unselected);
                        s=0;
                    }else {                     //已收藏
                        FrescoUtils.setImage(toolbar_sd, AppConfig.res+R.drawable.information_collect_selected);
                        s=1;
                    }
                } else {
                    ToastUtils.initLoginAgain(AcH5InfoActivity.this, bean.getErr_var());
                    ToastUtils.showShort(AcH5InfoActivity.this, bean.getMessage());
                }
            }

            @Override
            public void onFailure(Call<CollectStatusBean> call, Throwable t) {
                MoudleUtils.toChekWifi(AcH5InfoActivity.this);
            }
        });


    }

    private TextView textView;

    /**
     * 页面标题
     */
    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("超空");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }

    /**
     * 接收传递过来的url
     */
    private void initGetUrl() {
        Intent intent = getIntent();
        url = intent.getStringExtra("acurl");
        article_id=intent.getStringExtra("article_id");
    }

    /**
     * h5页面的进度条加载
     */
    private void initSet() {
        webViewLicense.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
        webViewLicense.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
                if (newProgress >= 100) {
                    progressBar.setVisibility(View.GONE);
                }
//                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                textView.setText(title);//
            }
        });
        initWeb();
    }

    /**
     * 页面返回键返回操作
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {

            if (webViewLicense.canGoBack()) {
                webViewLicense.goBack();// 返回前一个页面
                return true;
            } else {
                this.finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * findViewById
     */
    private void initId() {

        webViewLicense = (WebView) findViewById(R.id.webViewLicense);
        progressBar = (ProgressBar) findViewById(R.id.index_progressBar);
        webViewLicense.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

    }

    /**
     * 手机返回键方法重写返回键操作进行返回前一个h5页面
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webViewLicense.canGoBack()) {
            webViewLicense.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * webView的JS交互配置
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initWeb() {
        webViewLicense.setWebViewClient(new WebViewClient());
        WebSettings settings = webViewLicense.getSettings();
        settings.setJavaScriptEnabled(true);

        webViewLicense.loadUrl(url);

        webViewLicense.onResume();
    }

    @Override
    public void onClick(View v) {
        if (s==0){
            collect="add";
            initCollect();
        }else {
            collect="del";
            initCollect();
        }
    }

    private void initCollect() {
        dialog = new ProgressDialog(this);
        MoudleUtils.dialogShow(dialog);
        String user_id = (String) SPUtils.get("user_id", "");
        String token = (String) SPUtils.get("token", "");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        Call<BeanTool> callBack = restApi.collect(user_id,token, Integer.parseInt(article_id),collect);

        callBack.enqueue(new Callback<BeanTool>() {

            @Override
            public void onResponse(Call<BeanTool> call, Response<BeanTool> response) {
                BeanTool bean = response.body();
                if (bean == null) {
                    finish();
                    return;
                }
                if (bean.getStatus().equals("1")) {
                   initData();
                } else {
                    ToastUtils.initLoginAgain(AcH5InfoActivity.this, bean.getErr_var());
                    ToastUtils.showShort(AcH5InfoActivity.this, bean.getMessage());
                }
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<BeanTool> call, Throwable t) {
                MoudleUtils.toChekWifi(AcH5InfoActivity.this);
                MoudleUtils.dialogDismiss(dialog);
            }
        });
    }
}
