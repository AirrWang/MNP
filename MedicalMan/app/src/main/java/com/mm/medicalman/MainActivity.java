package com.mm.medicalman;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.mm.medicalman.Api.NpApi;
import com.mm.medicalman.Bean.BeanTool;
import com.mm.medicalman.Bean.ClassBean;
import com.mm.medicalman.Bean.CollectBean;
import com.mm.medicalman.Bean.DbUpBean;
import com.mm.medicalman.Bean.LearnBean;
import com.mm.medicalman.Bean.MockResultsBean;
import com.mm.medicalman.Bean.QuestBean;
import com.mm.medicalman.DBBean.Course;
import com.mm.medicalman.DBBean.DBcurrv;
import com.mm.medicalman.DBBean.LoveSubject;
import com.mm.medicalman.DBBean.MockResults;
import com.mm.medicalman.DBBean.Subject;
import com.mm.medicalman.DBBean.TopicRecord;
import com.mm.medicalman.Home.HomeFragment;
import com.mm.medicalman.Info.InfoFragment;
import com.mm.medicalman.Interface.MainTongBu;
import com.mm.medicalman.Interface.MainUpAgain;
import com.mm.medicalman.Interface.TokenErr;
import com.mm.medicalman.Mine.MineFragment;
import com.mm.medicalman.Tool.DBUtil.DbCore;
import com.mm.medicalman.Tool.JpushTool;
import com.mm.medicalman.Tool.Kyloading.KyLoadingBuilder;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.Tool.RetrofitUtils;
import com.mm.medicalman.Tool.SPUtils;
import com.mm.medicalman.Tool.SavaDataLocalUtils;
import com.mm.medicalman.Tool.ToastUtils;
import com.mm.medicalman.Tool.UpVersionNameAppTool;
import com.mm.medicalman.greendao.gen.CourseDao;
import com.mm.medicalman.greendao.gen.DBcurrvDao;
import com.mm.medicalman.greendao.gen.LoveSubjectDao;
import com.mm.medicalman.greendao.gen.MockResultsDao;
import com.mm.medicalman.greendao.gen.SubjectDao;
import com.mm.medicalman.greendao.gen.TopicRecordDao;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang on 2017/10/23.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private RadioGroup radioGroup;
    private List<Fragment> fragmentList = new ArrayList<>();
    private RadioButton rb_home;
    private RadioButton rb_info;
    private RadioButton rb_mine;
    private long page = 1;
    private KyLoadingBuilder kyLoadingBuilder;
    public static TokenErr tokenErr;
    private long pageMock = 1;
    public static MainTongBu mainTongBu;
    public static MainUpAgain mainUpAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tokenErr = null;
        mainTongBu = null;
        mainUpAgain = null;
        initTokenErrFinsh();
        initId();
        init();
        initTagAliasAgain();
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.setOffscreenPageLimit(2);
        //页面监听
        initListen();
        UpVersionNameAppTool.initTaskUpdata(null, this, "main");
        initDbUpTask();
    }
    private void initTagAliasAgain() {
        String user_id = (String) SPUtils.get("user_id", "");
        if (user_id != null && !user_id.equals("")) {
            JpushTool.mySetAlias(user_id);
        }
    }

    private void initTokenErrFinsh() {
        tokenErr = new TokenErr() {
            @Override
            public void toFinishMain(boolean b) {
                if (b) {
                    finish();
                }
            }
        };
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
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            MoudleUtils.onBackPressed(this);
            try {
                moveTaskToBack(false);
            } catch (Exception e) {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initDbUpTask() {
        String quest_v = "0", class_v = "0";
        final String num = "1";
        final DBcurrvDao dao = DbCore.getDaoSession().getDBcurrvDao();
        DBcurrv dBcurrv = new DBcurrv(num, quest_v, class_v);
        List<DBcurrv> list = dao.queryBuilder()
                .where(DBcurrvDao.Properties.Num.eq("1"))
                .orderAsc(DBcurrvDao.Properties.Num)
                .build().listLazyUncached();
        if (list == null || list.size() == 0) {
            dao.insertOrReplaceInTx(dBcurrv);
            dao.detachAll();
        } else {
//            Log.d("mmmmmmm:", list.size() + "");
            quest_v = list.get(0).getQuest_v();
            class_v = list.get(0).getClass_v();
//            Log.d("mmmmmmmquest_v:", quest_v);
//            Log.d("mmmmmmmclass_v:", class_v);
        }
        /**
         *
         quest_up	num	题目表是否需要更新,两个值,是:1,否:2
         quest_new	num	最新题目表版本
         class_up	num	题目分类表是否需要更新,两个值,是:1,否:2
         class_new	num	最新题目分类表版本
         */
        final String finalClass_v = class_v;
        final String finalQuest_v = quest_v;
        String userid = (String) SPUtils.get(this, "user_id", "");
        String token = (String) SPUtils.get(this, "token", "");
        Call<DbUpBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class)
                .dbCheckUpdate(token, userid, quest_v, class_v);
        getSignBeanCall.enqueue(new Callback<DbUpBean>() {
            @Override
            public void onResponse(Call<DbUpBean> call, Response<DbUpBean> response) {
                DbUpBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        if (bean.getData() != null) {
                            if (bean.getData().getQuest_up().equals("1")) {
                                kyLoadingBuilder = new KyLoadingBuilder(MainActivity.this);
                                MoudleUtils.kyloadingShow(kyLoadingBuilder, "正在同步题库...", false, false);
                                initUpQuest(num, dao, bean.getData().getClass_up(), finalQuest_v, finalClass_v,
                                        bean.getData().getQuest_new(), bean.getData().getClass_new());
                            } else if (bean.getData().getClass_up().equals("1")) {
                                kyLoadingBuilder = new KyLoadingBuilder(MainActivity.this);
                                MoudleUtils.kyloadingShow(kyLoadingBuilder, "正在同步题库...", false, false);
                                initUpClass(num, dao, finalClass_v, bean.getData().getClass_new(), bean.getData().getQuest_new());
                            } else {
                                String mockResults = (String) SPUtils.get("mockResultsNp", "0");
                                if (mockResults.equals("0")) {
                                    kyLoadingBuilder = new KyLoadingBuilder(MainActivity.this);
                                    MoudleUtils.kyloadingShow(kyLoadingBuilder, "正在同步题库...", false, false);
                                }
                                initToLoveLearnMock();
                            }
                        } else {
                            MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                        }
                    } else {
                        MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                        ToastUtils.initLoginAgain(MainActivity.this, bean.getErr_var());
                    }
                } else {
                    MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                }
                initDbUpTaskAgain();
            }

            @Override
            public void onFailure(Call<DbUpBean> call, Throwable t) {
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                MoudleUtils.toChekWifi(MainActivity.this);
                initDbUpTaskAgain();
            }
        });
    }

    /**
     * 此版本第一次同步题库失败，再次进行同步回调
     */
    private void initDbUpTaskAgain() {
        if (mainUpAgain == null) {
            mainUpAgain = new MainUpAgain() {
                @Override
                public void upAgain() {
                    initDbUpTask();
                }
            };
        }
    }

    private void initToLoveLearnMock() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                List<Subject> listDelete = DbCore.getDaoSession().getSubjectDao().
                        queryBuilder().where(SubjectDao.Properties.Deleted.eq("1"))
                        .build().list();
                if (listDelete != null && listDelete.size() > 0) {
                    DbCore.getDaoSession().getSubjectDao().deleteInTx(listDelete);
                }
                List<Course> listDeleteCourse = DbCore.getDaoSession().getCourseDao().
                        queryBuilder().where(CourseDao.Properties.Deleted.eq("1"))
                        .build().list();
                if (listDeleteCourse != null && listDeleteCourse.size() > 0) {
                    DbCore.getDaoSession().getCourseDao().deleteInTx(listDeleteCourse);
                    DbCore.getDaoSession().getCourseDao().detachAll();
                }
                String love = (String) SPUtils.get("isLoveNp", "0");
                String learn = (String) SPUtils.get("isLearnNp", "0");
                String mockResults = (String) SPUtils.get("mockResultsNp", "0");
                initLoveTask(love, learn, mockResults);
            }
        });
    }

    private void initLoveTask(String love, String learn, String mockResults) {
        if (love.equals("0")) {
            initTaskLove(learn, mockResults);
        } else {
            initLearnTask(learn, mockResults);
        }
    }

    private void initToBu() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                String sql = "SELECT group_concat(q_id) as str_q_ids,group_concat(is_love) as str_is_loves FROM LOVE_SUBJECT where is_love_up!=1";
                Cursor cursor = DbCore.getDaoMaster().getDatabase().rawQuery(sql, null);
                String str_q_ids = "", str_is_loves = "";
                if (cursor != null) {
                    try {
                        while (cursor.moveToNext()) {
                            str_q_ids = cursor.getString(cursor.getColumnIndex("str_q_ids"));
                            str_is_loves = cursor.getString(cursor.getColumnIndex("str_is_loves"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                }
                if (str_q_ids == null || str_q_ids.equals("") || str_is_loves == null || str_is_loves.equals("")) {
                    initToLearn();
                } else {
                    initToPostLoveTaskUp(str_q_ids, str_is_loves);
                }
            }
        });
    }

    private void initToLearn() {
        String sql = "SELECT group_concat(q_id) as str_q_ids,group_concat(error) as str_errors,group_concat(right) as str_rights" +
                " FROM TOPIC_RECORD where is_up!=1";
        Cursor cursor = DbCore.getDaoMaster().getDatabase().rawQuery(sql, null);
        String str_q_ids = "", str_rights = "", str_errors = "";
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    str_q_ids = cursor.getString(cursor.getColumnIndex("str_q_ids"));
                    str_rights = cursor.getString(cursor.getColumnIndex("str_rights"));
                    str_errors = cursor.getString(cursor.getColumnIndex("str_errors"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (str_q_ids == null || str_q_ids.equals("")) {
            initToHis();
        } else {
            initToUpLearn(str_q_ids, str_rights, str_errors);
        }
    }


    private void initToHis() {
        List<MockResults> mockResultsList = DbCore.getDaoSession().getMockResultsDao().queryBuilder().
                where(MockResultsDao.Properties.Is_up.notEq("1")).build().listLazyUncached();
        if (mockResultsList != null && mockResultsList.size() > 0) {
            Gson gson = new Gson();
            // 将list集合变成json格式
            String str = gson.toJson(mockResultsList);
//            Log.d("dsfsd", str);
            initToPostHis(str);
        } else {
            initInterfaceTongBu();
        }
    }

    private void initInterfaceTongBu() {
        if (mainTongBu == null) {
            mainTongBu = new MainTongBu() {
                @Override
                public void tongBu() {
                    initToBu();
                }
            };
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
                        ToastUtils.initLoginAgain(MainActivity.this, bean.getErr_var());
                    }
                }
                initInterfaceTongBu();
            }

            @Override
            public void onFailure(Call<BeanTool> call, Throwable t) {
                initInterfaceTongBu();
            }
        });
    }

    private void initToPostLoveTaskUp(final String str_q_ids, String str_is_loves) {
        String userid = (String) SPUtils.get(this, "user_id", "");
        String token = (String) SPUtils.get(this, "token", "");
        Call<BeanTool> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class)
                .collectUp(token, userid, str_q_ids, str_is_loves);
        getSignBeanCall.enqueue(new Callback<BeanTool>() {
            @Override
            public void onResponse(Call<BeanTool> call, Response<BeanTool> response) {
                BeanTool bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        List<LoveSubject> list = DbCore.getDaoSession().getLoveSubjectDao().queryBuilder().where(LoveSubjectDao.Properties.Is_love.eq("2")).
                                build().listLazyUncached();
                        if (list != null && list.size() > 0) {
                            DbCore.getDaoSession().getLoveSubjectDao().deleteInTx(list);
                            DbCore.getDaoSession().getLoveSubjectDao().detachAll();
                        }
                        String sql = "update LOVE_SUBJECT set is_love_up=1";
                        DbCore.getDaoMaster().getDatabase().execSQL(sql);
                    } else {
                        ToastUtils.initLoginAgain(MainActivity.this, bean.getErr_var());
                    }
                }
                initToLearn();
            }

            @Override
            public void onFailure(Call<BeanTool> call, Throwable t) {
                initToLearn();
            }
        });
    }

    private void initToUpLearn(final String str_q_ids, String str_rights, String str_errors) {
        String userid = (String) SPUtils.get(this, "user_id", "");
        String token = (String) SPUtils.get(this, "token", "");
        Call<BeanTool> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class)
                .learnUp(token, userid, str_q_ids, str_rights, str_errors);
        getSignBeanCall.enqueue(new Callback<BeanTool>() {
            @Override
            public void onResponse(Call<BeanTool> call, Response<BeanTool> response) {
                BeanTool bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        List<TopicRecord> list = DbCore.getDaoSession().getTopicRecordDao().queryBuilder().where(TopicRecordDao.Properties.Is_remove.eq("1")).
                                build().listLazyUncached();
                        if (list != null && list.size() > 0) {
                            DbCore.getDaoSession().getTopicRecordDao().deleteInTx(list);
                            DbCore.getDaoSession().getTopicRecordDao().detachAll();
                        }
                        String sql = "update TOPIC_RECORD set is_up=1";
                        DbCore.getDaoMaster().getDatabase().execSQL(sql);
                    } else {
                        ToastUtils.initLoginAgain(MainActivity.this, bean.getErr_var());
                    }
                }
                initToHis();
            }

            @Override
            public void onFailure(Call<BeanTool> call, Throwable t) {
                initToHis();
            }
        });
    }

    private void initLearnTask(String learn, String mockResults) {
        if (learn.equals("0")) {
            initTaskLearn(learn, mockResults);
        } else {
            initMockTask(learn, mockResults);
        }
    }

    private void initMockTask(String learn, String mockResults) {
        if (mockResults.equals("0")) {
            initTaskMockResults();
        } else {
            MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
            initToBu();
        }
    }

    private void initTaskMockResults() {
        String userid = (String) SPUtils.get(this, "user_id", "");
        String token = (String) SPUtils.get(this, "token", "");
        Call<MockResultsBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class)
                .mockResultsDown(token, userid, pageMock);
        getSignBeanCall.enqueue(new Callback<MockResultsBean>() {
            @Override
            public void onResponse(Call<MockResultsBean> call, Response<MockResultsBean> response) {
                final MockResultsBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        if (bean.getData() != null) {
                            if (pageMock <= Long.parseLong(bean.getData().getLast_page())) {
                                new Handler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        final List<MockResults> list = bean.getData().getList();
                                        if (list == null || list.size() == 0) {
                                            pageMock++;
                                            initTaskMockResults();
                                        } else {
                                            final MockResultsDao dao = DbCore.getDaoSession().getMockResultsDao();
                                            dao.getSession().startAsyncSession().runInTx(new Runnable() {
                                                @Override
                                                public void run() {
                                                    dao.insertOrReplaceInTx(list);
                                                    dao.detachAll();
                                                }
                                            });
                                            dao.getSession().startAsyncSession().waitForCompletion();
                                            pageMock++;
                                            initTaskMockResults();
                                        }
                                    }
                                });
                            } else {
                                SavaDataLocalUtils.saveDataString(MainActivity.this, "mockResultsNp", "1");
                                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                                initToBu();
                            }
                        } else {
                            MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                        }
                    } else {
                        MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                        ToastUtils.initLoginAgain(MainActivity.this, bean.getErr_var());
                    }
                } else {
                    MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                }
            }

            @Override
            public void onFailure(Call<MockResultsBean> call, Throwable t) {
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
            }
        });

    }

    private void initTaskLove(final String learn, final String mockResults) {
        String userid = (String) SPUtils.get(this, "user_id", "");
        String token = (String) SPUtils.get(this, "token", "");
        Call<CollectBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class)
                .collectDown(token, userid);
        getSignBeanCall.enqueue(new Callback<CollectBean>() {
            @Override
            public void onResponse(Call<CollectBean> call, Response<CollectBean> response) {
                final CollectBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        if (bean.getData() != null) {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    String s = bean.getData().getSql();
                                    if (s == null || s.equals("")) {
                                        SavaDataLocalUtils.saveDataString(MainActivity.this, "isLoveNp", "1");
                                        initLearnTask(learn, mockResults);
                                        return;
                                    }
                                    DbCore.getDaoSession().getLoveSubjectDao().deleteAll();
                                    String str_sql = "insert into LOVE_SUBJECT (q_id,is_love) values" + s;
                                    DbCore.getDaoMaster().getDatabase().execSQL(str_sql);
                                    String sql = "update LOVE_SUBJECT set is_love_up=1";
                                    DbCore.getDaoMaster().getDatabase().execSQL(sql);
                                    SavaDataLocalUtils.saveDataString(MainActivity.this, "isLoveNp", "1");
                                    initLearnTask(learn, mockResults);
                                }
                            });
                        } else {
                            MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                        }
                    } else {
                        MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                        ToastUtils.initLoginAgain(MainActivity.this, bean.getErr_var());
                    }
                } else {
                    MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                }
            }

            @Override
            public void onFailure(Call<CollectBean> call, Throwable t) {
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
            }
        });

    }

    private void initTaskLearn(final String learn, final String mockResults) {
        String userid = (String) SPUtils.get(this, "user_id", "");
        String token = (String) SPUtils.get(this, "token", "");
        Call<LearnBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class)
                .learnDown(token, userid);
        getSignBeanCall.enqueue(new Callback<LearnBean>() {
            @Override
            public void onResponse(Call<LearnBean> call, Response<LearnBean> response) {
                final LearnBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        if (bean.getData() != null) {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    String str1 = bean.getData().getSql();
                                    if (str1 == null || str1.equals("")) {
                                        SavaDataLocalUtils.saveDataString(MainActivity.this, "isLearnNp", "1");
                                        initMockTask(learn, mockResults);
                                        return;
                                    }
                                    DbCore.getDaoSession().getTopicRecordDao().deleteAll();
                                    String str_sql = "insert into TOPIC_RECORD (q_id,right,error) values " + str1;
                                    DbCore.getDaoMaster().getDatabase().execSQL(str_sql);
                                    String sql = "update TOPIC_RECORD set is_up=1,is_remove=2";
                                    DbCore.getDaoMaster().getDatabase().execSQL(sql);
                                    SavaDataLocalUtils.saveDataString(MainActivity.this, "isLearnNp", "1");
                                    initMockTask(learn, mockResults);
                                }
                            });
                        } else {
                            MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                        }
                    } else {
                        MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                        ToastUtils.initLoginAgain(MainActivity.this, bean.getErr_var());
                    }
                } else {
                    MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                }
            }

            @Override
            public void onFailure(Call<LearnBean> call, Throwable t) {
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
            }
        });

    }

    private void initUpClass(final String num, final DBcurrvDao dao, String finalClass_v, final String class_new, final String quset_new) {
        String userid = (String) SPUtils.get(this, "user_id", "");
        String token = (String) SPUtils.get(this, "token", "");
        Call<ClassBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class)
                .classDown(token, userid, finalClass_v, class_new);
        getSignBeanCall.enqueue(new Callback<ClassBean>() {
            @Override
            public void onResponse(Call<ClassBean> call, Response<ClassBean> response) {
                final ClassBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        if (bean.getData() != null) {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    if (bean.getData().getList() != null && bean.getData().getList().size() > 0) {
                                        final CourseDao mdao = DbCore.getDaoSession().getCourseDao();
                                        mdao.getSession().startAsyncSession().runInTx(new Runnable() {
                                            @Override
                                            public void run() {
                                                mdao.insertOrReplaceInTx(bean.getData().getList());
                                                mdao.detachAll();
                                            }
                                        });
                                        mdao.getSession().startAsyncSession().waitForCompletion();
                                    }
                                    initEndDb(num, quset_new, class_new, dao);
                                }
                            });
                        } else {
                            MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                        }
                    } else {
                        MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                        ToastUtils.initLoginAgain(MainActivity.this, bean.getErr_var());
                    }
                } else {
                    MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                }
            }

            @Override
            public void onFailure(Call<ClassBean> call, Throwable t) {
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
            }
        });

    }

    private void initUpQuest(final String num, final DBcurrvDao dao, final String classUpIs, final String finalQuestV, final String classUp, final String finalQuest_v_new, final String class_up_new) {
        String userid = (String) SPUtils.get(this, "user_id", "");
        String token = (String) SPUtils.get(this, "token", "");
        Call<QuestBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class)
                .questDown(token, userid, finalQuestV, finalQuest_v_new, page);
        getSignBeanCall.enqueue(new Callback<QuestBean>() {
            @Override
            public void onResponse(Call<QuestBean> call, Response<QuestBean> response) {
                final QuestBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        if (bean.getData() != null) {
                            long last_p = Long.parseLong(bean.getData().getLast_page());
                            if (page <= last_p) {
                                initUpQuestData(bean.getData().getList(), num, dao, classUpIs, finalQuestV, classUp, finalQuest_v_new, class_up_new);
                            } else {
                                if (classUpIs.equals("1")) {
                                    initUpClass(num, dao, classUp, class_up_new, finalQuest_v_new);
                                } else {
                                    initEndDb(num, finalQuest_v_new, class_up_new, dao);
                                }
                            }
                        } else {
                            MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                        }
                    } else {
                        MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                        ToastUtils.initLoginAgain(MainActivity.this, bean.getErr_var());
                    }
                } else {
                    MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                }
            }

            @Override
            public void onFailure(Call<QuestBean> call, Throwable t) {
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
            }
        });

    }

    private void initUpQuestData(final List<Subject> list, final String num, final DBcurrvDao dao, final String classUpIs, final String finalQuestV, final String classUp, final String finalQuest_v_new, final String class_up_new) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                final SubjectDao mdao = DbCore.getDaoSession().getSubjectDao();
                mdao.getSession().startAsyncSession().runInTx(new Runnable() {
                    @Override
                    public void run() {
                        mdao.insertOrReplaceInTx(list);
                        mdao.detachAll();
                    }
                });
                mdao.getSession().startAsyncSession().waitForCompletion();
                page++;
                initUpQuest(num, dao, classUpIs, finalQuestV, classUp,
                        finalQuest_v_new, class_up_new);
            }
        });
    }

    private void initEndDb(String num, String finalQuest_v_new, String class_up_new, DBcurrvDao dao) {
        DBcurrv dBcurrv = new DBcurrv(num, finalQuest_v_new,
                class_up_new);
        dao.insertOrReplaceInTx(dBcurrv);
        dao.detachAll();
        initToLoveLearnMock();
    }


    private void initId() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        rb_home = (RadioButton) findViewById(R.id.home);
        rb_info = (RadioButton) findViewById(R.id.info);
        rb_mine = (RadioButton) findViewById(R.id.mine);
        bitmapFit(rb_home, R.drawable.home_bottom_selector, getResources().getDimensionPixelSize(R.dimen.dbl_icon_w), getResources().getDimensionPixelSize(R.dimen.dbl_icon_h));
        bitmapFit(rb_info, R.drawable.information_bottom_selector, getResources().getDimensionPixelSize(R.dimen.dbl_icon_w), getResources().getDimensionPixelSize(R.dimen.dbl_icon_h));
        bitmapFit(rb_mine, R.drawable.personal_bottom_selector, getResources().getDimensionPixelSize(R.dimen.dbl_icon_w), getResources().getDimensionPixelSize(R.dimen.dbl_icon_h));
    }

    private void bitmapFit(RadioButton rb, int drawableResId, int w, int h) {
        //定义底部标签图片大小
        Drawable drawableWeiHui = getResources().getDrawable(drawableResId);
        drawableWeiHui.setBounds(0, 0, w, h);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        rb.setCompoundDrawables(null, drawableWeiHui, null, null);//只放上面
    }

    private void init() {
        HomeFragment chatFragment = new HomeFragment();
        InfoFragment contactsFragment = new InfoFragment();
        MineFragment meFragment = new MineFragment();

        fragmentList.add(chatFragment);
        fragmentList.add(contactsFragment);
        fragmentList.add(meFragment);
    }

    private void initListen() {
        // ViewPager页面切换监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radioGroup.check(R.id.home);
                        break;
                    case 1:
                        radioGroup.check(R.id.info);
                        break;
                    case 2:
                        radioGroup.check(R.id.mine);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // RadioGroup选中状态改变监听
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.home:
                        /**
                         * setCurrentItem第二个参数控制页面切换动画
                         * true:打开/false:关闭
                         */
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.info:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.mine:
                        viewPager.setCurrentItem(2);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public interface MyTouchListener {
        public void onTouchEvent(MotionEvent event);

    }

    // 保存MyTouchListener接口的列表
    private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<MainActivity.MyTouchListener>();

    /**
     * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
     *
     * @param listener
     */
    public void registerMyTouchListener(MyTouchListener listener) {
        myTouchListeners.add(listener);
    }

    /**
     * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
     *
     * @param listener
     */
    public void unRegisterMyTouchListener(MyTouchListener listener) {
        myTouchListeners.remove(listener);
    }

    /**
     * 分发触摸事件给所有注册了MyTouchListener的接口
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyTouchListener listener : myTouchListeners) {
            listener.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }
}
