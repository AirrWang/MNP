package com.mm.medicalman.Mine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.mm.medicalman.R;
import com.mm.medicalman.Tool.MoudleUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NiPing and Airr Wang on 2017/10/23.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class AboutActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.mine_version) TextView mine_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_about);
        ButterKnife.bind(this);
        initToolbar();
        initData();
    }

    private void initData() {
        MoudleUtils.textViewSetText(mine_version,"当前版本 "+ MoudleUtils.getVersionName(this));
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
        toolbar_title.setText("关于医护人才");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }
}
