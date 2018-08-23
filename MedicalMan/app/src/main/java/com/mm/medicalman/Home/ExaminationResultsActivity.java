package com.mm.medicalman.Home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mm.medicalman.Api.NpApi;
import com.mm.medicalman.Bean.BeanTool;
import com.mm.medicalman.DBBean.MockResults;
import com.mm.medicalman.DBBean.Subject;
import com.mm.medicalman.MainActivity;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.DBUtil.DbCore;
import com.mm.medicalman.Tool.Kyloading.KyLoadingBuilder;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.Tool.RetrofitUtils;
import com.mm.medicalman.Tool.SPUtils;
import com.mm.medicalman.Tool.ToastUtils;
import com.mm.medicalman.greendao.gen.MockResultsDao;
import com.mm.medicalman.greendao.gen.TitleRecordExaminationDao;

import org.greenrobot.greendao.query.WhereCondition;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mm.medicalman.Home.ExaminationActivity.examinationAgain;
/**
 * Created by NiPing and Airr Wang on 2017/10/23.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */

public class ExaminationResultsActivity extends AppCompatActivity {
    private List<Subject> mlistAll = new ArrayList<>();
    private String mTIme;
    private RecyclerView rv_bottom;
    private TextView textView1, textView2, textView3, textView4, textView5;
    private String type;//5模拟考试，6历年试题考试
    private String name;
    private RelativeLayout r_below_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination_results);
        mlistAll = ExaminationActivity.listAll;
        initId();
        initToolbar();
        initGetData();
        initSetData();
    }

    private void initSetData() {
        final KyLoadingBuilder kyLoadingBuilder = new KyLoadingBuilder(this);
        MoudleUtils.kyloadingShow(kyLoadingBuilder);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                initData(mlistAll);
                initSet();
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
            }
        });
    }

    private void initSet() {
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbCore.getDaoSession().getTitleRecordLoveDao().deleteAll();
                if (type.equals("6")) {

                } else {
                    //   toType   8模拟考试错题解析
                    startActivity(new Intent().putExtra("type", "8").
                            putExtra("c_id", "-1").putExtra("name", "错题解析").
                            putExtra("serial", "-1").
                            setClass(ExaminationResultsActivity.this, MineLoveExamActivity.class));
                    //type 0：关闭，1：重新作答，2：错题分析
                    if (examinationAgain != null) {
                        examinationAgain.examinationAgain("2");
                    }
                }
            }
        });
        textView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("6")) {

                } else {
                    //type 0：关闭，1：重新作答，2：错题分析
                    if (examinationAgain != null) {
                        examinationAgain.examinationAgain("1");
                    }
                    finish();
                }
            }
        });
    }

    private void initId() {
        rv_bottom = (RecyclerView) findViewById(R.id.rv_bottom);
        textView1 = (TextView) findViewById(R.id.textView29);
        textView2 = (TextView) findViewById(R.id.textView30);
        textView3 = (TextView) findViewById(R.id.textView31);
        textView4 = (TextView) findViewById(R.id.textView32);
        textView5 = (TextView) findViewById(R.id.textView33);
        r_below_btn = (RelativeLayout) findViewById(R.id.r_below_btn);

    }

    private void initGetData() {
        Intent intent = getIntent();
        mTIme = intent.getStringExtra("mTime");
        type = intent.getStringExtra("type");
        name = intent.getStringExtra("name");
        if (type.equals("5")) {
            MoudleUtils.viewShow(r_below_btn);
        } else if (type.equals("6")) {
            MoudleUtils.viewGone(r_below_btn);
        }
    }


    private void initData(List<Subject> mlistAll) {
        if (mlistAll != null && mlistAll.size() > 0) {
            GridLayoutManager mLayoutManager = new GridLayoutManager(ExaminationResultsActivity.this, 6);
            rv_bottom.setLayoutManager(mLayoutManager);
            RecycleBottomAdapter mAdapter = new RecycleBottomAdapter(ExaminationResultsActivity.this, "8", null, null);
            mAdapter.updateData(mlistAll);
            rv_bottom.setAdapter(mAdapter);
            MoudleUtils.textViewSetText(textView1, "您已回答" + mlistAll.size() + "道题");
            //1对2错0未做
            int nRightI = DbCore.getDaoSession().getTitleRecordExaminationDao().queryBuilder().
                    where(TitleRecordExaminationDao.Properties.Is_right.eq("1")).
                    build().listLazyUncached().size();
            //1对2错0未做
            int nWrongI = DbCore.getDaoSession().getTitleRecordExaminationDao().queryBuilder().
                    where(TitleRecordExaminationDao.Properties.Is_right.eq("2")).
                    build().listLazyUncached().size();
            float nRightF = nRightI;
            float n = nRightF / mlistAll.size() * 100f;
            int nI = (int) n;
            if ((n - nI) >= 0.5) {
                nI = nI + 1;
            }
            MoudleUtils.textViewSetText(textView2, "答错" + nWrongI + "题");
            MoudleUtils.textViewSetText(textView3, "答题正确率" + nI + "%");
            MockResultsDao dao = DbCore.getDaoSession().getMockResultsDao();
            int nType = 0;
            //nType	num	考试类型,目前2个值,模拟考试:1,历史试卷考试:2
            if (type.equals("5")) {
                nType = 1;
            } else if (type.equals("6")) {
                nType = 2;
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String str = formatter.format(curDate);
            MockResults mockResults = new MockResults(null, nType + "", str,
                    mTIme, (long) nI, name, "2");
            dao.insertOrReplaceInTx(mockResults);
            dao.detachAll();
            initToHis();
        }
    }

    private void initToHis() {
        List<MockResults> mockResultsList = DbCore.getDaoSession().getMockResultsDao().queryBuilder().
                where(MockResultsDao.Properties.Is_up.eq("2")).build().listLazyUncached();
        if (mockResultsList != null && mockResultsList.size() > 0) {
            Gson gson = new Gson();
            // 将list集合变成json格式
            String str = gson.toJson(mockResultsList);
//            Log.d("dsfsd", str);
            initToPostHis(str);
        }
    }

    private void initToPostHis(String s) {
        String userid = (String) SPUtils.get(this, "user_id", "");
        String token = (String) SPUtils.get(this, "token", "");
        Call<BeanTool> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class)
                .mockResultsUp(token, userid, s);
        getSignBeanCall.enqueue(new Callback<BeanTool>() {
            @Override
            public void onResponse(Call<BeanTool> call, Response<BeanTool> response) {
                BeanTool bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        String sql = "update MOCK_RESULTS set is_up=1";
                        DbCore.getDaoMaster().getDatabase().execSQL(sql);
                    } else {
                        ToastUtils.initLoginAgain(ExaminationResultsActivity.this, bean.getErr_var());
                    }
                }
            }

            @Override
            public void onFailure(Call<BeanTool> call, Throwable t) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            initFinish();

        }
        return super.onOptionsItemSelected(item);
    }

    private void initFinish() {
        //type 0：关闭，1：重新作答，2：错题分析
        if (examinationAgain != null) {
            examinationAgain.examinationAgain("0");
        }
        finish();
    }

    /**
     * 锁定登录返回键，使其失效
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //type 0：关闭，1：重新作答，2：错题分析
            initFinish();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 页面标题
     */
    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("考试报告");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }
}
