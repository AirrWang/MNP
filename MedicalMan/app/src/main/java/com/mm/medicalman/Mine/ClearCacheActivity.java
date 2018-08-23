package com.mm.medicalman.Mine;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.mm.medicalman.R;
import com.mm.medicalman.Tool.DataCleanManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by NiPing and Airr Wang on 2017/10/23.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class ClearCacheActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.mine_wipecache) Button mWipeCache;
    @BindView(R.id.mJindu) TextView mJindu;
    private boolean isOk = true;
    private int time = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_clear_cache);
        ButterKnife.bind(this);
        initToolbar();
    }
    @OnClick(R.id.mine_wipecache)void clear(){
        if(isOk){
            DataCleanManager.cleanApplicationData(this);
            start();
        }else{
            isOk=true;
            time=0;
            DataCleanManager.cleanApplicationData(this);
            start();
        }
    }
    @Override
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
        toolbar_title.setText("清除缓存");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            if(msg.what == 0) {
                mJindu.setText(time+"");
                time++;
                if (time >= 100) {
                    time=100;
                    isOk = false;
                }
            }
        }
    };
    private void start(){
        new Thread(){
            @Override
            public void run() {
                while(isOk){
                    try {
                        Thread.sleep(50);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
