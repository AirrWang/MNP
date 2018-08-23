package com.mm.medicalman.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.mm.medicalman.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by NiPing and Airr Wang on 2017/10/23.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class CampusActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.tv_achievements)
    TextView tv_achievements;
    @BindView(R.id.tv_schedule)
    TextView tv_schedule;
    @BindView(R.id.tv_sigin)
    TextView tv_sigin;
    @BindView(R.id.tv_open_scllo)
    TextView tv_open_scllo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus);
        ButterKnife.bind(this);
        initToolbar();

    }

    @OnClick(R.id.tv_open_scllo)
    void OpenScllo() {
        startActivity(new Intent().setClass(this, OpenSclloActivity.class));
    }

    @OnClick(R.id.tv_achievements)
    void Achievements() {
        startActivity(new Intent().setClass(this, AchievementActivity.class));
    }

    @OnClick(R.id.tv_schedule)
    void Schedule() {
        startActivity(new Intent().setClass(this, TimeTableActivity.class));
    }

    @OnClick(R.id.tv_sigin)
    void ToSigin() {
        startActivity(new Intent().setClass(this, SiginActivity.class));
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
        toolbar_title.setText("校园通");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }
}
