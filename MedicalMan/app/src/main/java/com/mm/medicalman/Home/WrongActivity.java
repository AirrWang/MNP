package com.mm.medicalman.Home;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.mm.medicalman.DBBean.Course;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.DBUtil.DbCore;
import com.mm.medicalman.greendao.gen.CourseDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NiPing and Airr Wang on 2017/11/2.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class WrongActivity extends AppCompatActivity{

    private ListView listView;
    private List<Course> list=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love);
        initToolbar();
        listView = (ListView) findViewById(R.id.lv_love);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                list= DbCore.getDaoSession().getCourseDao().queryBuilder().orderAsc(CourseDao.Properties.C_num).build().listLazyUncached();
                initAdapter();
            }
        });
    }

    private ListAdapterMineLove adapter;
    private void initAdapter() {
        if (adapter == null) {
            adapter = new ListAdapterMineLove(this, "4");
            adapter.setList(list);
            listView.setAdapter(adapter);
        } else {
            adapter.setList(list);
            adapter.notifyDataSetChanged();
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
        TextView toolbar_title= (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("我的错题");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }
}
