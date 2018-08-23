package com.mm.medicalman.Home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mm.medicalman.DBBean.TopicRecord;
import com.mm.medicalman.Mine.ExamHisActivity;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.ATProgressView;
import com.mm.medicalman.Tool.DBUtil.DbCore;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.greendao.gen.SubjectDao;
import com.mm.medicalman.greendao.gen.TopicRecordDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mm.medicalman.MainActivity.mainTongBu;

/**
 * Created by NiPing and Airr Wang on 2017/10/25.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class HomeNurseExamActivity extends AppCompatActivity implements View.OnClickListener {

    private ATProgressView pv_nurse_exam;
    private ProgressDialog dialog;
    @BindView(R.id.ll_mock)
    LinearLayout ll_mock;
    @BindView(R.id.ll_mine_love)
    LinearLayout ll_mine_love;
    @BindView(R.id.ll_years)
    LinearLayout ll_years;
    @BindView(R.id.tv_right_proportion)
    TextView tv_right_proportion;
    @BindView(R.id.tv_num_all)
    TextView tv_num_all;
    @BindView(R.id.ll_mine_wrong)
    LinearLayout ll_mine_wrong;
    @BindView(R.id.ll_exam_his)
    LinearLayout ll_exam_his;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nurse_exam);
        ButterKnife.bind(this);
        initToolbar();
        dialog = new ProgressDialog(this);
        MoudleUtils.dialogShow(dialog);
        initId();
        MoudleUtils.dialogDismiss(dialog);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                initData();
                if (mainTongBu != null) {
                    mainTongBu.tongBu();
                }
            }
        });
    }

    private void initData() {
        String sql = "delete from TOPIC_RECORD where q_id not in(select q_id from SUBJECT)";
        DbCore.getDaoMaster().getDatabase().execSQL(sql);

        QueryBuilder<TopicRecord> queryBuilder = DbCore.getDaoSession().getTopicRecordDao().queryBuilder();
        List<TopicRecord> listAll = queryBuilder.where(TopicRecordDao.Properties.Is_remove.eq("2")).build().list();
        float n = 0, nall = 0;
        if (listAll != null && listAll.size() > 0) {
            nall = listAll.size();
            List<TopicRecord> list = DbCore.getDaoSession().getTopicRecordDao().queryBuilder().where(
                    TopicRecordDao.Properties.Right.gt("0"), TopicRecordDao.Properties.Is_remove.eq("2")).build().listLazyUncached();
            if (list != null && list.size() > 0) {
                float listN = list.size();
                n = listN / nall;
            }
        }
        int nEnd = (int) (n * 100f);
        int nallEnd = (int) nall;
        if (n >= 1) {
            n = 0.99f;
        }
        pv_nurse_exam.setProress(n);
        pv_nurse_exam.startCountdown();
        MoudleUtils.textViewSetText(tv_right_proportion, nEnd + "");
        MoudleUtils.textViewSetText(tv_num_all, "累计答题" + nallEnd + "道");
    }

    @OnClick(R.id.ll_mock)
    void ToMock() {
        startActivity(new Intent().setClass(this, MockActivity.class));
    }

    @OnClick(R.id.ll_mine_love)
    void ToLove() {
        startActivity(new Intent().setClass(this, LoveActivity.class));
    }

    @OnClick(R.id.ll_years)
    void ToYears() {
        startActivity(new Intent().setClass(this, YearsActivity.class));
    }

    @OnClick(R.id.ll_mine_wrong)
    void ToWrong() {
        startActivity(new Intent().setClass(this, WrongActivity.class));
    }

    @OnClick(R.id.ll_exam_his)
    void ToRecord() {
        startActivity(new Intent().setClass(this, ExamHisActivity.class));
    }

    private void initId() {
        pv_nurse_exam = (ATProgressView) findViewById(R.id.pv_nurse_exam);
        pv_nurse_exam.setProress(0f);
        pv_nurse_exam.startCountdown();
        LinearLayout ll_practice = (LinearLayout) findViewById(R.id.ll_practice);
        ll_practice.setOnClickListener(this);
        LinearLayout ll_practice_1 = (LinearLayout) findViewById(R.id.ll_practice_1);
        ll_practice_1.setOnClickListener(this);
        TextView tv_1 = (TextView) findViewById(R.id.text1);
        tv_1.setText("共" + DbCore.getDaoSession().getSubjectDao().queryBuilder().where(SubjectDao.Properties.Ability.eq("2")).build().listLazyUncached().size() + "道");
        TextView tv_2 = (TextView) findViewById(R.id.text2);
        tv_2.setText("共" + DbCore.getDaoSession().getSubjectDao().queryBuilder().where(SubjectDao.Properties.Ability.eq("1")).build().listLazyUncached().size() + "道");
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
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("护士职业资格考试");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_practice:
                startActivity(new Intent().putExtra("type", "2").setClass(HomeNurseExamActivity.this, PracticeActivity.class));
                break;
            case R.id.ll_practice_1:
                startActivity(new Intent().putExtra("type", "1").setClass(HomeNurseExamActivity.this, PracticeActivity.class));
                break;
            default:
                break;
        }
    }
}
