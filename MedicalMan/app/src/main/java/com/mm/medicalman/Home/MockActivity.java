package com.mm.medicalman.Home;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mm.medicalman.Api.NpApi;
import com.mm.medicalman.Bean.CollectBean;
import com.mm.medicalman.Bean.MockRuleBean;
import com.mm.medicalman.Bean.MockRuleMyBean;
import com.mm.medicalman.DBBean.LoveSubject;
import com.mm.medicalman.DBBean.MockResults;
import com.mm.medicalman.DBBean.MockRule;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.AppConfig;
import com.mm.medicalman.Tool.DBUtil.DbCore;
import com.mm.medicalman.Tool.FrescoUtils;
import com.mm.medicalman.Tool.Kyloading.KyLoadingBuilder;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.Tool.RetrofitUtils;
import com.mm.medicalman.Tool.SPUtils;
import com.mm.medicalman.Tool.ToastUtils;
import com.mm.medicalman.greendao.gen.DBcurrvDao;
import com.mm.medicalman.greendao.gen.LoveSubjectDao;
import com.mm.medicalman.greendao.gen.MockResultsDao;
import com.mm.medicalman.greendao.gen.MockRuleDao;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Created by NiPing and Airr Wang on 2017/10/23.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class MockActivity extends AppCompatActivity {

    private KyLoadingBuilder kyLoadingBuilder;
    @BindView(R.id.textView4)
    SimpleDraweeView simpleDraweeViewPic;
    @BindView(R.id.textView9)
    TextView textView2;
    @BindView(R.id.textView11)
    TextView textView4;
    @BindView(R.id.textView13)
    TextView textView6;
    @BindView(R.id.textView15)
    TextView textView8;
    @BindView(R.id.button4)
    Button button1;
    @BindView(R.id.button5)
    Button button2;
    @BindView(R.id.button6)
    Button button3;
    @BindView(R.id.textView6)
    TextView tv_name;
    private String type;
    private TextView tv_latest_achievements;
    private LinearLayout ll_his;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);
        initToolbar();
        ButterKnife.bind(this);
        initTask();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        ll_his = (LinearLayout) findViewById(R.id.ll_his);
        tv_latest_achievements = (TextView) findViewById(R.id.tv_latest_achievements);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("模拟考试");
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }

    private void initTask() {
        kyLoadingBuilder = new KyLoadingBuilder(this);
        MoudleUtils.kyloadingShow(kyLoadingBuilder);
        String userid = (String) SPUtils.get(this, "user_id", "");
        String token = (String) SPUtils.get(this, "token", "");
        Call<MockRuleBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class)
                .mockRule(token, userid);
        getSignBeanCall.enqueue(new Callback<MockRuleBean>() {
            @Override
            public void onResponse(Call<MockRuleBean> call, Response<MockRuleBean> response) {
                final MockRuleBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        final MockRuleBean.MockRuleBeanData beanData = bean.getData();
                        if (beanData != null) {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    MockRuleDao dao = DbCore.getDaoSession().getMockRuleDao();
                                    MockRule mockRule = new MockRule("1", beanData.getMinute(), beanData.getQ_num(),
                                            beanData.getRule_score(), beanData.getRule_question(), beanData.getQ_difficulty().getType(),
                                            beanData.getQ_difficulty().getNum(), beanData.getQ_class().getC_id(), beanData.getQ_class().getNum()
                                    );
                                    dao.insertOrReplaceInTx(mockRule);
                                    dao.detachAll();
                                    initDataMockRule(mockRule);
                                    MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                                }
                            });
                        } else {
                            initGetNewRuleDefeat();
                        }
                    } else {
                        initGetNewRuleDefeat();
                        ToastUtils.initLoginAgain(MockActivity.this, bean.getErr_var());
                    }
                } else {
                    initGetNewRuleDefeat();
                }
            }

            @Override
            public void onFailure(Call<MockRuleBean> call, Throwable t) {
                initGetNewRuleDefeat();
//                MoudleUtils.toChekWifi(MockActivity.this);
            }
        });
    }

    private void initGetNewRuleDefeat() {
        MockRuleDao dao = DbCore.getDaoSession().getMockRuleDao();
        MockRule mockRule = dao.queryBuilder()
                .where(MockRuleDao.Properties.Id.eq(1))
                .build().unique();
        if (mockRule == null) {
            finish();
            ToastUtils.showShort(this, "同步答题规则失败，请检查网络后重试");
            MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
            return;
        }
        initDataMockRule(mockRule);
        MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
    }

    private void initDataMockRule(MockRule mockRule) {
        String url = (String) SPUtils.get("informationPicNp", AppConfig.res + R.color.colorPrimaryDark);
        String name = (String) SPUtils.get("informationNicknameNp", "");
        FrescoUtils.setImage(simpleDraweeViewPic, url);
        MoudleUtils.textViewSetText(tv_name, name);
        initAchievementsLaster();
        MoudleUtils.textViewSetText(textView2, mockRule.getMinute() + "分钟");
        MoudleUtils.textViewSetText(textView4, mockRule.getQ_num() + "题");
        MoudleUtils.textViewSetText(textView6, mockRule.getRule_score());
        MoudleUtils.textViewSetText(textView8, mockRule.getRule_question());
        initSet(mockRule);
    }

    private void initAchievementsLaster() {
        MockResults mockResults = DbCore.getDaoSession().getMockResultsDao().queryBuilder().
                orderDesc(MockResultsDao.Properties.Record_id).limit(1).build().unique();
        if (mockResults == null) {
            MoudleUtils.viewGone(ll_his);
        } else {
            MoudleUtils.viewShow(ll_his);
            MoudleUtils.textViewSetText(tv_latest_achievements, "正确率" + mockResults.getPercent() + "%  " + mockResults.getMinute());
        }
    }

    private void initSet(final MockRule mockRule) {
        //1全真模拟，2智能分析，3优先未做题
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "1";
                initDataToNext(mockRule);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "2";
                initDataToNext(mockRule);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "3";
                initDataToNext(mockRule);
            }
        });
    }

    private void initDataToNext(MockRule mockRule) {
        DbCore.getDaoSession().getTitleRecordExaminationDao().deleteAll();
        MockRuleMyBean mockRuleMyBean = new MockRuleMyBean("1", mockRule.getMinute(), mockRule.getQ_num(),
                mockRule.getRule_score(), mockRule.getRule_question(), mockRule.getQ_difficulty_type(),
                mockRule.getQ_difficulty_num(), mockRule.getQ_class_c_id(), mockRule.getQ_class_num()
        );
        Bundle bundle = new Bundle();
        bundle.putSerializable("mockRuleNp", mockRuleMyBean);
        startActivity(new Intent().putExtras(bundle).putExtra("type", type).setClass(MockActivity.this,
                ExaminationActivity.class));
    }
}
