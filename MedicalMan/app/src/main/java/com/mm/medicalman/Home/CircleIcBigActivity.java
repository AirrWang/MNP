package com.mm.medicalman.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mm.medicalman.R;
import com.mm.medicalman.Tool.MultiTouchViewPager;

/**
 * Created by NiPing and Airr Wang
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class CircleIcBigActivity extends AppCompatActivity {
    private MultiTouchViewPager viewPager;
    private ViewPagerAdapterCireBigPic vpAdapter;
    private String picStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_ic_big);
        initId();
        initGetPictureData();
        setViewPagerAdapter();
    }

    private void initGetPictureData() {
        Intent intent=getIntent();
        picStr = intent.getStringExtra("touxiang");
    }

    private void initId() {
        viewPager = (MultiTouchViewPager) findViewById(R.id.vp_circle);

    }

    private void setViewPagerAdapter() {
        if (vpAdapter == null) {
            vpAdapter = new ViewPagerAdapterCireBigPic(picStr,
                    CircleIcBigActivity.this);
            viewPager.setAdapter(vpAdapter);
        } else {
            vpAdapter.notifyDataSetChanged();
        }
        viewPager.setCurrentItem(1);
    }
}
