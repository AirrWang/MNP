package com.mm.medicalman.Mine;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.mm.medicalman.DBBean.MockResults;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.DBUtil.DbCore;
import com.mm.medicalman.greendao.gen.MockResultsDao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NiPing and Airr Wang on 2017/11/8.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class ExamHisActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.tv_max)
    TextView tv_max;
    @BindView(R.id.lv_exam_his)
    ListView lv_exam_his;
    @BindView(R.id.tv_right_per)
    TextView tv_right_per;
    @BindView(R.id.tv_exam_time)
    TextView tv_exam_time;

    private List<MockResults> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_his);
        ButterKnife.bind(this);
        initToolbar();
        initData();
    }

    private void initData() {
        list = DbCore.getDaoSession().getMockResultsDao().queryBuilder().build().listLazyUncached();
        if (list.size() == 0) {
            return;
        }
        tv_exam_time.setText(list.size() + "");
        initAvgAndMax();
        initAdapter();
    }

    private void initAvgAndMax() {
        double avg = 0;
        int max = 0;
        String sql = " SELECT AVG(percent) as avg , max(percent) as max FROM MOCK_RESULTS ";
        Cursor cursor = DbCore.getDaoMaster().getDatabase().rawQuery(sql, null);
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    avg = cursor.getDouble(cursor.getColumnIndex("avg"));
                    max = cursor.getInt(cursor.getColumnIndex("max"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        tv_max.setText("历史最高正确率" + max + "%");
        tv_right_per.setText(new BigDecimal(avg + "").setScale(0, BigDecimal.ROUND_HALF_UP) + "%");
    }

    private ListAdapterExamHis adapter;

    private void initAdapter() {
        if (adapter == null) {
            adapter = new ListAdapterExamHis(this);
            adapter.setList(list);
            lv_exam_his.setAdapter(adapter);
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
        toolbar_title.setText("考试历史");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }
}
