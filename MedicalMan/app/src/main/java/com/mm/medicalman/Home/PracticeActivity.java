package com.mm.medicalman.Home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mm.medicalman.DBBean.Course;
import com.mm.medicalman.DBBean.Subject;
import com.mm.medicalman.DBBean.TopicRecord;
import com.mm.medicalman.Interface.UpAllSIzeInterface;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.DBUtil.DbCore;
import com.mm.medicalman.Tool.Kyloading.KyLoadingBuilder;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.greendao.gen.CourseDao;
import com.mm.medicalman.greendao.gen.SubjectDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NiPing and Airr Wang on 2017/10/31.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class PracticeActivity extends AppCompatActivity {

    private ListView listview;
    private View headerView;
    private List<Course> list = new ArrayList<>();
    private List<Subject> listAll = new ArrayList<>();
    private int nHaved;
    private TextView tv_total;
    private String type;//专业实务:1,实践能力:2
    private KyLoadingBuilder kyLoadingBuilder;
    private Button btn_random;
    private UpAllSIzeInterface upAllSIzeInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practive);
        headerView = getLayoutInflater().inflate(R.layout.activity_practive_head, null);
        initGetData();
        initToolbar();
        initId();
        initDataSize();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                QueryBuilder<TopicRecord> queryBuilder = DbCore.getDaoSession().getTopicRecordDao().queryBuilder();
                queryBuilder.join(Subject.class, SubjectDao.Properties.Q_id).
                        where(SubjectDao.Properties.Ability.eq(type));
                nHaved = queryBuilder.build().listLazyUncached().size();
                listAll = DbCore.getDaoSession().getSubjectDao().queryBuilder().
                        where(SubjectDao.Properties.Ability.eq(type)).build().listLazyUncached();
                if (listAll != null && listAll.size() > 0) {
                    upAllSIzeInterface.upAllSIze();
                }
            }
        });
    }

    private void initDataSize() {
        upAllSIzeInterface = new UpAllSIzeInterface() {
            @Override
            public void upAllSIze() {
                if (tv_total == null || list == null) return;
                tv_total.setText("已答" + nHaved + "题／共" + listAll.size() + "题");
            }
        };
    }

    private void initData() {
        kyLoadingBuilder = new KyLoadingBuilder(this);
        MoudleUtils.kyloadingShow(kyLoadingBuilder);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                list = DbCore.getDaoSession().getCourseDao().queryBuilder().
                        orderAsc(CourseDao.Properties.C_num).build().listLazyUncached();
                initAdapter();
                initSet();
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
            }
        });
    }

    private void initGetData() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
    }

    private void initSet() {
        btn_random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //type:3我的收藏，4我的错题，7随机练习
                DbCore.getDaoSession().getTitleRecordLoveDao().deleteAll();
                startActivity(new Intent().putExtra("type", "7").
                        putExtra("c_id", "-1").putExtra("name", "随机练习").
                        putExtra("serial", "-1").putExtra("type_2",type).
                        setClass(PracticeActivity.this, MineLoveExamActivity.class));
            }
        });
    }

    private void initId() {
        tv_total = (TextView) headerView.findViewById(R.id.tv_total);
        listview = (ListView) findViewById(R.id.lv_practive);
        listview.addHeaderView(headerView);
        btn_random = (Button) headerView.findViewById(R.id.btn_random);
        TextView tv_type = (TextView) headerView.findViewById(R.id.tv_type);
        if (type.equals("1")) {
            tv_type.setText("专业实务");
        } else if (type.equals("2")) {
            tv_type.setText("实践能力");
        } else {
            finish();
        }
    }

    private ListAdapterPractoce adapter;

    private void initAdapter() {
        if (adapter == null) {
            adapter = new ListAdapterPractoce(this);
            adapter.setList(list);
            adapter.setType(type);
            listview.setAdapter(adapter);
        } else {
            adapter.setList(list);
            adapter.setType(type);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 页面标题
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("章节练习");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
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
}
