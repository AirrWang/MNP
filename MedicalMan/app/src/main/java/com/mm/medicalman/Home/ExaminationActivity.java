package com.mm.medicalman.Home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;

import com.mm.medicalman.Api.NpApi;
import com.mm.medicalman.Bean.MockRuleMyBean;
import com.mm.medicalman.Bean.OveYearsQuestionBean;
import com.mm.medicalman.DBBean.Subject;
import com.mm.medicalman.DBBean.TopicRecord;
import com.mm.medicalman.Interface.ExaminationAgain;
import com.mm.medicalman.Interface.ExaminationTimeEnd;
import com.mm.medicalman.Interface.Finish;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.DBUtil.DbCore;
import com.mm.medicalman.Tool.Kyloading.KyLoadingBuilder;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.Tool.RetrofitUtils;
import com.mm.medicalman.Tool.SPUtils;
import com.mm.medicalman.Tool.ToastUtils;
import com.mm.medicalman.Tool.ViewPagerTransfomer.HomeCardVpTransformer;
import com.mm.medicalman.greendao.gen.SubjectDao;
import com.mm.medicalman.greendao.gen.TopicRecordDao;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang on 2017/11/1.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class ExaminationActivity extends AppCompatActivity {
    private String type;//1全真模拟，2智能分析，3优先未做题,4历年试题
    private MockRuleMyBean mockRule;
    private ViewPager vp;
    public static List<Subject> listAll = new ArrayList<>();
    private ViewPagerAdapterDoExer adapterDoExer;
    private long page = 1;
    private KyLoadingBuilder kyLoadingBuilder;
    private String id;
    private TimeCount m_timeCount;
    private TextView toolbar_title;
    private String mm;
    private long mTime;
    public static ExaminationAgain examinationAgain;
    public static ExaminationTimeEnd examinationTimeEnd;
    private boolean falgIsEnd;
    private Finish mfinish;
    private String eName = "";
    private long nowTimeS;
    private TextView shadowView;

    /**
     * 题目难度,容易:1,中等:2,困难:3
     * 题目数量 , 用英文逗号分隔 , 与type一一对应
     * "q_difficulty": {
     * "type": "1,2,3",
     * "num": "20,30,50"
     * },
     * 章节c_id , 用英文逗号分隔 , 与num一一对应
     * 题目数量 , 用英文逗号分隔 , 与c_id一一对应
     * "q_class": {
     * "q_class": "1,2,3,4,5",
     * "num": "10,20,20,20,30"
     * },
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_examination);
        initFinish();
        initGetIntent();
        initToolbar();
        initId();
        kyLoadingBuilder = new KyLoadingBuilder(this);
        MoudleUtils.kyloadingShow(kyLoadingBuilder);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                switch (type) {
                    case "1":
                        initData();
                        initDataAdapter();
                        break;
                    case "2":
                        initData2();
                        initDataAdapter();
                        break;
                    case "3":
                        initData3();
                        initDataAdapter();
                        break;
                    case "4":
                        initData4();
                        break;
                }
//                Log.d("sjdflshfjk", listAll.size() + "");
            }
        });
        initAgain();
    }

    private void initFinish() {
        mfinish = new Finish() {
            @Override
            public void Finish() {
                ToastUtils.showShort(ExaminationActivity.this, "暂无");
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                initClear();
                finish();
            }
        };
    }

    private void initAgain() {
        //type0：关闭，1：重新作答，2：错题分析
        examinationAgain = new ExaminationAgain() {
            @Override
            public void examinationAgain(String type) {
                switch (type) {
                    case "0":
                        initClear();
                        finish();
                        break;
                    case "1":
                        initAgainVp();
                        break;
                    case "2":
                        break;
                }
            }
        };
    }

    private void initClear() {
        if (listAll != null) {
            listAll.clear();
        }
        if (m_timeCount != null) {
            m_timeCount.cancel();
        }
    }

    private void initAgainVp() {
        kyLoadingBuilder = new KyLoadingBuilder(ExaminationActivity.this);
        MoudleUtils.kyloadingShow(kyLoadingBuilder);
        DbCore.getDaoSession().getTitleRecordExaminationDao().deleteAll();
        if (type.equals("4")) {
            initDataAdapterTrue();
        } else {
            initDataAdapter();
        }
        falgIsEnd = false;
    }

    private void initData4() {
        initToGetDatainitToGetData();
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            if (!falgIsEnd && listAll != null && listAll.size() > 0) {
                falgIsEnd = true;
                String toType;
                String name;
                if (!type.equals("4")) {
                    toType = "5";
                    name = "模拟考试";
                } else {
                    toType = "6";
                    name = eName;
                }
                //toType考试:5， 历年真题：6,
                ToastUtils.showShort(ExaminationActivity.this, "考试时间到了，已经自动交卷");
                MoudleUtils.textViewSetText(toolbar_title, "倒计时00:00");
                String toMyTime = "";
                if (mTime < 10) {
                    toMyTime = "0" + mTime;
                }
                startActivity(new Intent().putExtra("name", name).putExtra("type", toType).
                        putExtra("mTime", toMyTime + ":00").setClass(ExaminationActivity.this,
                        ExaminationResultsActivity.class));
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            nowTimeS = millisUntilFinished;
            long nm = millisUntilFinished / 60000;
            long ns = (millisUntilFinished - nm * 60000) / 1000;
            if (nm < 1) {
                nm = 0;
            }
            if (nm < 10) {
                if (ns < 10) {
                    MoudleUtils.textViewSetText(toolbar_title, "倒计时0" + nm + ":0" + ns);
                } else {
                    MoudleUtils.textViewSetText(toolbar_title, "倒计时0" + nm + ":" + ns);
                }
            } else {
                if (ns < 10) {
                    MoudleUtils.textViewSetText(toolbar_title, "倒计时" + nm + ":0" + ns);
                } else {
                    MoudleUtils.textViewSetText(toolbar_title, "倒计时" + nm + ":" + ns);
                }
            }
        }
    }

    /**
     * 重新登录的弹框提示
     */
    private AlertDialog alertDialog;

    public void initFinish(final Context context) {
        alertDialog = new AlertDialog.Builder(context).setTitle("")
                .setMessage("您确定放弃此次考试？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        initClear();
                        finish();
                    }

                }).setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create(); // 创建对话框.create(); // 创建对话框
        if (alertDialog != null) {
            alertDialog.setCancelable(false);
            if (!alertDialog.isShowing()) {
                alertDialog.show(); // 显示
            }
        }
    }

    private void initToGetDatainitToGetData() {
        String userid = (String) SPUtils.get(ExaminationActivity.this, "user_id", "");
        String token = (String) SPUtils.get(ExaminationActivity.this, "token", "");
        Call<OveYearsQuestionBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class)
                .OveYearsQuestion(token, userid, page, id);
        getSignBeanCall.enqueue(new Callback<OveYearsQuestionBean>() {
            @Override
            public void onResponse(Call<OveYearsQuestionBean> call, Response<OveYearsQuestionBean> response) {
                final OveYearsQuestionBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        if (bean.getData() != null) {
                            if (page <= Long.parseLong(bean.getData().getLast_page())) {
                                initToData(bean);
                            } else {
                                initDataAdapterTrue();
                            }
                        } else {
                            MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                        }
                    } else {
                        MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                        ToastUtils.initLoginAgain(ExaminationActivity.this, bean.getErr_var());
                    }
                } else {
                    MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                }
            }

            @Override
            public void onFailure(Call<OveYearsQuestionBean> call, Throwable t) {
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
            }
        });
    }

    private void initToData(final OveYearsQuestionBean bean) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                final List<Subject> list = bean.getData().getList();
                if (list == null || list.size() == 0) {
                    page++;
                    initToGetDatainitToGetData();
                } else {
                    page++;
                    listAll.addAll(list);
                    initToGetDatainitToGetData();
                }
            }
        });
    }

    private void initData2() {
        String s = "";
        String q_difficulty_type[] = intToStrings(mockRule.getQ_difficulty_type());
        String q_difficulty_num[] = intToStrings(mockRule.getQ_difficulty_num());
        String q_class_c_id[] = intToStrings(mockRule.getQ_class_c_id());
        String q_class_num[] = intToStrings(mockRule.getQ_class_num());
        QueryBuilder<Subject> queryBuilder = DbCore.getDaoSession().getSubjectDao().queryBuilder();
        queryBuilder.join(TopicRecord.class, TopicRecordDao.Properties.Q_id).where(TopicRecordDao.Properties.Error.gt("0"),
                TopicRecordDao.Properties.Is_remove.gt("2"));
        float n = Float.parseFloat(mockRule.getQ_num());
        for (int i = 0; i < q_class_c_id.length; i++) {
            for (int j = 0; j < q_difficulty_type.length; j++) {
                float q_difficulty_num_f_proportion = Float.parseFloat(q_difficulty_num[j]) / n;
                int q_class_num_i = Integer.parseInt(q_class_num[i]);
                int listNum = (int) (q_difficulty_num_f_proportion * q_class_num_i);
                q_class_num[i] = (q_class_num_i - listNum) + "";
//                Log.d("sjdflshfjk", n + ",%:" + q_difficulty_num_f_proportion + "," +
//                        "每章数量：" + q_class_num_i + "每章占困难度的数量：" + listNum);
                List<Subject> list = queryBuilder.where(SubjectDao.Properties.C_id.
                        eq(q_class_c_id[i]), SubjectDao.Properties.Difficulty.
                        eq(q_difficulty_type[j])).orderRaw("RANDOM()").limit(listNum)
                        .build().list();
                s = initSQid(s, list);
                listAll.addAll(list);
            }
        }
        if (listAll.size() < n) {
            for (int i = 0; i < q_class_c_id.length; i++) {
                for (int j = 0; j < q_difficulty_type.length; j++) {
                    float q_difficulty_num_f_proportion = Float.parseFloat(q_difficulty_num[j]) / n;
                    int q_class_num_i = Integer.parseInt(q_class_num[i]);
                    int listNum = (int) (q_difficulty_num_f_proportion * q_class_num_i);
//                Log.d("sjdflshfjk", n + ",%:" + q_difficulty_num_f_proportion + "," +
//                        "每章数量：" + q_class_num_i + "每章占困难度的数量：" + listNum);
                    List<Subject> list = DbCore.getDaoSession().getSubjectDao().
                            queryBuilder().where(SubjectDao.Properties.C_id.
                            eq(q_class_c_id[i]), SubjectDao.Properties.Difficulty.
                            eq(q_difficulty_type[j]), new WhereCondition.StringCondition("q_id not in(" + s + ")")).orderRaw("RANDOM()").limit(listNum)
                            .build().list();
                    s = initSQid(s, list);
                    listAll.addAll(list);
                }
            }
        }
        if (listAll.size() < n) {
            List<Subject> listNot = DbCore.getDaoSession().getSubjectDao().
                    queryBuilder().where(new WhereCondition.StringCondition("q_id not in(" + s + ")")).orderRaw("RANDOM()").limit((int) (n - listAll.size()))
                    .build().list();
            listAll.addAll(listNot);
        }
    }

    private void initData3() {
        String s = "";
        String q_difficulty_type[] = intToStrings(mockRule.getQ_difficulty_type());
        String q_difficulty_num[] = intToStrings(mockRule.getQ_difficulty_num());
        String q_class_c_id[] = intToStrings(mockRule.getQ_class_c_id());
        String q_class_num[] = intToStrings(mockRule.getQ_class_num());
        float n = Float.parseFloat(mockRule.getQ_num());
        for (int i = 0; i < q_class_c_id.length; i++) {
            for (int j = 0; j < q_difficulty_type.length; j++) {
                float q_difficulty_num_f_proportion = Float.parseFloat(q_difficulty_num[j]) / n;
                int q_class_num_i = Integer.parseInt(q_class_num[i]);
                int listNum = (int) (q_difficulty_num_f_proportion * q_class_num_i);
                q_class_num[i] = (q_class_num_i - listNum) + "";
//                Log.d("sjdflshfjk", n + ",%:" + q_difficulty_num_f_proportion + "," +
//                        "每章数量：" + q_class_num_i + "每章占困难度的数量：" + listNum);
                List<Subject> list = DbCore.getDaoSession().getSubjectDao().queryBuilder().where(SubjectDao.Properties.C_id.
                        eq(q_class_c_id[i]), SubjectDao.Properties.Difficulty.
                        eq(q_difficulty_type[j]), new WhereCondition.StringCondition("q_id not in" + "(select q_id from TOPIC_RECORD)")).orderRaw("RANDOM()").limit(listNum)
                        .build().list();
                s = initSQid(s, list);
                listAll.addAll(list);
            }
        }
        if (listAll.size() < n) {
            for (int i = 0; i < q_class_c_id.length; i++) {
                for (int j = 0; j < q_difficulty_type.length; j++) {
                    float q_difficulty_num_f_proportion = Float.parseFloat(q_difficulty_num[j]) / n;
                    int q_class_num_i = Integer.parseInt(q_class_num[i]);
                    int listNum = (int) (q_difficulty_num_f_proportion * q_class_num_i);
//                Log.d("sjdflshfjk", n + ",%:" + q_difficulty_num_f_proportion + "," +
//                        "每章数量：" + q_class_num_i + "每章占困难度的数量：" + listNum);
                    List<Subject> list = DbCore.getDaoSession().getSubjectDao().
                            queryBuilder().where(new WhereCondition.StringCondition("q_id not in(" + s + ")"), SubjectDao.Properties.C_id.
                            eq(q_class_c_id[i]), SubjectDao.Properties.Difficulty.
                            eq(q_difficulty_type[j])).orderRaw("RANDOM()").limit(listNum)
                            .build().list();
                    s = initSQid(s, list);
                    listAll.addAll(list);
                }
            }
        }
        if (listAll.size() < n) {
            List<Subject> listNot = DbCore.getDaoSession().getSubjectDao().
                    queryBuilder().where(new WhereCondition.StringCondition("q_id not in(" + s + ")")).orderRaw("RANDOM()").limit((int) (n - listAll.size()))
                    .build().list();
            listAll.addAll(listNot);
        }
    }

    private void initData() {
        String s = "";
        String q_difficulty_type[] = intToStrings(mockRule.getQ_difficulty_type());
        String q_difficulty_num[] = intToStrings(mockRule.getQ_difficulty_num());
        String q_class_c_id[] = intToStrings(mockRule.getQ_class_c_id());
        String q_class_num[] = intToStrings(mockRule.getQ_class_num());
        float n = Float.parseFloat(mockRule.getQ_num());
        for (int i = 0; i < q_class_c_id.length; i++) {
            for (int j = 0; j < q_difficulty_type.length; j++) {
                float q_difficulty_num_f_proportion = Float.parseFloat(q_difficulty_num[j]) / n;
                int q_class_num_i = Integer.parseInt(q_class_num[i]);
                int listNum = (int) (q_difficulty_num_f_proportion * q_class_num_i);
//                Log.d("sjdflshfjk", n + ",%:" + q_difficulty_num_f_proportion + "," +
//                        "每章数量：" + q_class_num_i + "每章占困难度的数量：" + listNum);
                List<Subject> list = DbCore.getDaoSession().getSubjectDao().
                        queryBuilder().where(SubjectDao.Properties.C_id.
                        eq(q_class_c_id[i]), SubjectDao.Properties.Difficulty.
                        eq(q_difficulty_type[j])).orderRaw("RANDOM()").limit(listNum)
                        .build().list();
                s = initSQid(s, list);
                listAll.addAll(list);
            }
        }
        if (listAll.size() < n) {
            List<Subject> listNot = DbCore.getDaoSession().getSubjectDao().queryBuilder().where(new
                    WhereCondition.StringCondition("q_id not in(" + s + ")")).
                    orderRaw("RANDOM()").limit((int) (n - listAll.size())).build().list();
            listAll.addAll(listNot);
        }
    }

    private String initSQid(String s, List<Subject> list) {
        if (list != null && list.size() > 0) {
            if (s.length() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    s = s + "," + list.get(k).getQ_id();
                }
            } else {
                for (int k = 0; k < list.size(); k++) {
                    if (k != list.size() - 1) {
                        s = s + list.get(k).getQ_id() + ",";
                    } else {
                        s = s + list.get(k).getQ_id();
                    }
                }
            }
        }
        return s;
    }

    /***
     * 用来查重
     */
    private void initIsCf() {
        String s = "";
        for (int i = 0; i < listAll.size(); i++) {
            s = listAll.get(i).getQ_id();
            for (int j = 0; j < listAll.size(); j++) {
                if (s.equals(listAll.get(j).getQ_id()) && i != j) {
                    Log.d("sjdflshfjk sdfds," + i + "," + j + "：", s);
                }
            }
        }
    }

    private String[] intToStrings(String s) {
        String[] q_difficulty_type;
        if (s.indexOf(",") >= 0) {
            q_difficulty_type = s.split(",");
        } else {
            q_difficulty_type = new String[1];
            q_difficulty_type[0] = s;
        }
        return q_difficulty_type;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        initClear();
    }

    private void initGetIntent() {
        initClear();
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        id = intent.getStringExtra("id");
        eName = intent.getStringExtra("eName");
        if (type.equals("4")) {
            mm = intent.getStringExtra("mmNp");
            if (mm == null) {
                mm = "100";
            }
        } else {
            mockRule = (MockRuleMyBean) intent.getSerializableExtra("mockRuleNp");
        }
    }

    private void initId() {
        shadowView=(TextView)findViewById(R.id.shadowView);
        vp = (ViewPager) findViewById(R.id.vp_exercise);
        vp.setPageTransformer(true, new HomeCardVpTransformer());
    }

    private void initDataAdapter() {

        if (listAll == null || listAll.size() == 0) {
            if (mfinish != null) {
                mfinish.Finish();
            }
        } else {
            adapterDoExer = new ViewPagerAdapterDoExer(shadowView,listAll, this, vp, "5");
            vp.setAdapter(adapterDoExer);
            mTime = Long.parseLong(mockRule.getMinute());
            m_timeCount = new TimeCount(mTime * 60 * 1000, 1000);//开始计时m分钟
            m_timeCount.start();
            initTimeEnd();
            vp.setCurrentItem(0);
            MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
        }
    }

    private void initTimeEnd() {
        examinationTimeEnd = new ExaminationTimeEnd() {
            @Override
            public void examinationTimeEnd() {
//                Log.d("zdsjfnjks1", "" + nowTimeS);
                falgIsEnd = true;
                m_timeCount.cancel();
                long endTime = mTime * 60 * 1000 - nowTimeS;
//                Log.d("zdsjfnjks2", endTime + "");

                long nm = endTime / 60000;
                long ns = (endTime - nm * 60000) / 1000;
                ns = ns + 1;
//                Log.d("sdfsdfs2", endTime + "," + nm + "," + ns);
                String toMyTime = "";
                if (nm < 10) {
                    if (ns < 10) {
                        toMyTime = "0" + nm + ":0" + ns;
                    } else {
                        toMyTime = "0" + nm + ":" + ns;
                    }
                } else {
                    if (ns < 10) {
                        toMyTime = nm + ":0" + ns;
                    } else {
                        toMyTime = nm + ":" + ns;
                    }
                }
                String name;
                String toType;
                if (!type.equals("4")) {
                    toType = "5";
                    name = "模拟考试";
                } else {
                    toType = "6";
                    name = eName;
                }
                startActivity(new Intent().putExtra("name", name).putExtra("type", toType).putExtra("mTime", toMyTime).
                        setClass(ExaminationActivity.this, ExaminationResultsActivity.class));
            }
        };
    }


    private void initDataAdapterTrue() {
        if (listAll == null || listAll.size() == 0) {
            if (mfinish != null) {
                mfinish.Finish();
            }
        } else {
            adapterDoExer = new ViewPagerAdapterDoExer(shadowView,listAll, this, vp, "6");
            vp.setAdapter(adapterDoExer);
            mTime = Long.parseLong(mm);
            m_timeCount = new TimeCount(mTime * 60000, 1000);//开始计时m分钟
            m_timeCount.start();
            initTimeEnd();
            vp.setCurrentItem(0);
            MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            initFinish(this);
        }

        return super.onOptionsItemSelected(item);
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
            initFinish(this);
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
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }
}
