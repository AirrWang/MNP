package com.mm.medicalman.Home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mm.medicalman.DBBean.LoveSubject;
import com.mm.medicalman.DBBean.Subject;
import com.mm.medicalman.DBBean.TitleRecordExamination;
import com.mm.medicalman.DBBean.TitleRecordLove;
import com.mm.medicalman.DBBean.TopicRecord;
import com.mm.medicalman.Interface.Finish;
import com.mm.medicalman.Interface.UpAllSIzeInterface;
import com.mm.medicalman.Interface.UpIsTrueInterface;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.AppConfig;
import com.mm.medicalman.Tool.DBUtil.DBTools;
import com.mm.medicalman.Tool.DBUtil.DbCore;
import com.mm.medicalman.Tool.FrescoUtils;
import com.mm.medicalman.Tool.Kyloading.KyLoadingBuilder;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.Tool.SelectPopupWindow;
import com.mm.medicalman.Tool.ToastUtils;
import com.mm.medicalman.Tool.ViewPagerTransfomer.HomeCardVpTransformer;
import com.mm.medicalman.greendao.gen.LoveSubjectDao;
import com.mm.medicalman.greendao.gen.SubjectDao;
import com.mm.medicalman.greendao.gen.TitleRecordExaminationDao;
import com.mm.medicalman.greendao.gen.TitleRecordLoveDao;
import com.mm.medicalman.greendao.gen.TopicRecordDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NiPing and Airr Wang on 2017/11/3.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class MineLoveExamActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager vp;
    private String type = "";//3我的收藏，4我的错题，7随机练习，8模拟考试错题解析
    private String c_id = "";
    private String name = "";
    private List<Subject> list = new ArrayList<>();
    private ViewPagerAdapterDoExer adapterDoExer;
    private SimpleDraweeView sd_love;
    private int p = 0;
    private boolean a;
    private TextView tv_postion;
    private String serial = "";
    private TextView tv_right;
    private TextView tv_wrong;
    private int right = 0;
    private int wrong = 0;
    private TextView tv_total;
    private UpAllSIzeInterface upAllSIzeInterface;
    public static UpIsTrueInterface upIsTrueInterfaceMineLove;
    private LinearLayout ll_1;
    private SelectPopupWindow bottomWindow;
    private TextView tv_delete_is;
    private KyLoadingBuilder kyLoadingBuilder;
    private Finish mfinish;
    private String type_2="";//专业实务:1,实践能力:2
    private TextView shadowView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_exercise);
        initFinsh();
        initGetIntent();
        initToolbar();
        initId();
        initDataSize();
        initData();
        initIsTrue();
    }

    private void initFinsh() {
        mfinish = new Finish() {
            @Override
            public void Finish() {
                ToastUtils.showShort(MineLoveExamActivity.this, "暂无");
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                finish();
            }
        };
    }

    private void initIsTrue() {
        upIsTrueInterfaceMineLove = new UpIsTrueInterface() {
            @Override
            public void upIsTrue() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        initBottomView();
                    }
                });
            }
        };
    }

    private void initDataSize() {
        upAllSIzeInterface = new UpAllSIzeInterface() {
            @Override
            public void upAllSIze() {
                if (tv_total == null || list == null) return;
                tv_total.setText("/" + list.size());
            }
        };
    }

    private void initData() {
        kyLoadingBuilder = new KyLoadingBuilder(this);
        MoudleUtils.kyloadingShow(kyLoadingBuilder);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                initDataList();
                initAdapter();
                if (list != null && list.size() > 0) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            isLove();
                            initBottomView();
                            MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                        }
                    });
                } else {
                    MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                }
            }
        });
    }

    private void initDataList() {
        if (type.equals("3")) {
            QueryBuilder<Subject> queryBuilder = DbCore.getDaoSession().getSubjectDao().queryBuilder();
            queryBuilder.join(LoveSubject.class, LoveSubjectDao.Properties.Q_id).where(LoveSubjectDao.Properties.Is_love.eq("1"));
            list = queryBuilder.where(SubjectDao.Properties.C_id.eq(c_id)).build().listLazyUncached();
        } else if (type.equals("4")) {
            QueryBuilder<Subject> queryBuilder = DbCore.getDaoSession().getSubjectDao().queryBuilder();
            queryBuilder.join(TopicRecord.class, TopicRecordDao.Properties.Q_id).
                    where(TopicRecordDao.Properties.Error.gt("0"), TopicRecordDao.Properties.Is_remove.eq("2"));
            list = queryBuilder.where(SubjectDao.Properties.C_id.eq(c_id)).build().listLazyUncached();
        } else if (type.equals("7")) {
            QueryBuilder<Subject> queryBuilder = DbCore.getDaoSession().getSubjectDao().queryBuilder();
            list = queryBuilder.where(SubjectDao.Properties.Ability.eq(type_2)).orderRaw("RANDOM()").build().listLazyUncached();
        } else if (type.equals("8")) {
            QueryBuilder<Subject> queryBuilder = DbCore.getDaoSession().getSubjectDao().queryBuilder();
            queryBuilder.join(TitleRecordExamination.class, TitleRecordExaminationDao.Properties.Q_id).
                    where(TitleRecordExaminationDao.Properties.Is_right.eq("2"));//1对2错0未做
            list = queryBuilder.build().listLazyUncached();
        }
        if (list != null && list.size() > 0) {
            upAllSIzeInterface.upAllSIze();
        }
    }

    private void initGetIntent() {
        Intent intent = getIntent();
        c_id = intent.getStringExtra("c_id");
        name = intent.getStringExtra("name");
        serial = intent.getStringExtra("serial");
        type = intent.getStringExtra("type");
        if (type.equals("7")){
            type_2= intent.getStringExtra("type_2");
        }
    }

    private void initId() {
        shadowView=(TextView)findViewById(R.id.shadowView);
        tv_right = (TextView) findViewById(R.id.tv_right_exer);
        tv_wrong = (TextView) findViewById(R.id.tv_wrong);
        vp = (ViewPager) findViewById(R.id.vp_exercise);
        vp.setPageTransformer(true, new HomeCardVpTransformer());
        tv_delete_is = (TextView) findViewById(R.id.tv_delete_is);
        LinearLayout ll_love = (LinearLayout) findViewById(R.id.ll_love);
        LinearLayout ll_delet_is = (LinearLayout) findViewById(R.id.ll_delet_is);
        if (type.equals("4")) {
            MoudleUtils.viewShow(ll_delet_is);
            MoudleUtils.viewGone(ll_love);
            ll_delet_is.setOnClickListener(this);
        } else {
            MoudleUtils.viewGone(ll_delet_is);
            MoudleUtils.viewShow(ll_love);
            ll_love.setOnClickListener(this);
        }
        sd_love = (SimpleDraweeView) findViewById(R.id.sd_love);
        tv_postion = (TextView) findViewById(R.id.tv_postion);
        tv_total = (TextView) findViewById(R.id.tv_total);
        ll_1 = (LinearLayout) findViewById(R.id.ll_1);
        ll_1.setOnClickListener(this);
    }

    private void isLove() {
        if (!type.equals("4") && list != null && list.size() > 0) {
            LoveSubject loveSubject = DbCore.getDaoSession().getLoveSubjectDao().
                    queryBuilder().where(LoveSubjectDao.Properties.Q_id.eq(list.get(p).getQ_id())).build().unique();
            if (loveSubject != null) {
                if (loveSubject.getIs_love().equals("1")) {//收藏
                    FrescoUtils.setImage(sd_love, AppConfig.res + R.drawable.collect_selected);
                    a = true;
                } else if (loveSubject.getIs_love().equals("2")) {//未收藏
                    FrescoUtils.setImage(sd_love, AppConfig.res + R.drawable.collect_unselected);
                    a = false;
                }
            } else {                                      //未收藏
                FrescoUtils.setImage(sd_love, AppConfig.res + R.drawable.collect_unselected);
                a = false;
            }
        } else if (list != null && list.size() > 0) {
            TopicRecord topicRecord = DbCore.getDaoSession().getTopicRecordDao().
                    queryBuilder().where(TopicRecordDao.Properties.Q_id.eq(list.get(p).getQ_id())).build().unique();
            if (topicRecord != null) {
                if (topicRecord.getIs_remove().equals("2")) {
                    MoudleUtils.textViewSetText(tv_delete_is, "移除");
                    //未移除
                    a = false;
                } else if (topicRecord.getIs_remove().equals("1")) {
                    MoudleUtils.textViewSetText(tv_delete_is, "已移除");//已移除
                    a = true;
                }
            } else {
                MoudleUtils.textViewSetText(tv_delete_is, "已移除");//已移除
                a = true;
            }
        }
    }

    private void initAdapter() {
        if (list == null || list.size() == 0) {
            if (mfinish != null) {
                mfinish.Finish();
            }
        } else {
            adapterDoExer = new ViewPagerAdapterDoExer(list, this, vp, type);
            vp.setAdapter(adapterDoExer);
            vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    shadowView.setTranslationX(vp.getWidth()-positionOffsetPixels);
                }

                @Override
                public void onPageSelected(int position) {
                    initLoveAndNum(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    private void initLoveAndNum(int position) {
        p = position;
        isLove();
        tv_postion.setText(p + 1 + "");
    }

    private void initBottomView() {
        List<TitleRecordLove> list_right = new ArrayList<>();
        List<TitleRecordLove> list_wrong = new ArrayList<>();
        if (!type.equals("7")&&!type.equals("8")) {
            list_right = DbCore.getDaoSession().getTitleRecordLoveDao().queryBuilder().
                    where(TitleRecordLoveDao.Properties.C_id.eq(c_id), TitleRecordLoveDao.Properties.Is_right.eq("1")).
                    build().listLazyUncached();
            list_wrong = DbCore.getDaoSession().getTitleRecordLoveDao().queryBuilder()
                    .where(TitleRecordLoveDao.Properties.C_id.eq(c_id), TitleRecordLoveDao.Properties.Is_right.eq("2")).
                            build().listLazyUncached();
        } else {
            list_right = DbCore.getDaoSession().getTitleRecordLoveDao().queryBuilder().
                    where(TitleRecordLoveDao.Properties.Is_right.eq("1")).
                    build().listLazyUncached();
            list_wrong = DbCore.getDaoSession().getTitleRecordLoveDao().queryBuilder()
                    .where(TitleRecordLoveDao.Properties.Is_right.eq("2")).
                            build().listLazyUncached();
        }
        right = list_right.size();
        tv_right.setText(right + "");
        if (type.equals("8")) {
            wrong = list.size();
        } else {
            wrong = list_wrong.size();
        }
        tv_wrong.setText(wrong + "");
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
        toolbar_title.setText(name);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_love:
                if (!type.equals("4") && list != null && list.size() > 0) {
                    String s_q_id = "";
                    String s_type = "";
                    if (a) {
                        s_q_id = list.get(p).getQ_id();
                        s_type = "2";
                        FrescoUtils.setImage(sd_love, AppConfig.res + R.drawable.collect_unselected);
                        a = false;
                    } else {
                        s_q_id = list.get(p).getQ_id();
                        s_type = "1";
                        FrescoUtils.setImage(sd_love, AppConfig.res + R.drawable.collect_selected);
                        a = true;
                    }
                    LoveSubject ls = new LoveSubject(s_q_id, s_type, "2");
                    DbCore.getDaoSession().getLoveSubjectDao().insertOrReplaceInTx(ls);
                    DbCore.getDaoSession().getLoveSubjectDao().detachAll();
                }
                break;
            case R.id.ll_delet_is:
                if (type.equals("4") && list != null && list.size() > 0) {
                    if (!a) {
                        if (list == null || list.size() == 0 || list.size() <= p
                                || list.get(p).getQ_id() == null) return;
                        DBTools.rightErrAllNumRemove(list.get(p).getQ_id());
                        MoudleUtils.textViewSetText(tv_delete_is, "已移除");
                        a = true;
                    }
                }
                break;
            case R.id.ll_1://从下弹出弹窗
                initShowBottomIs();
                break;
            default:
                break;
        }

    }

    private void initShowBottomIs() {
        bottomWindow = new SelectPopupWindow(this, itemOnClick, p, list.size(), a, serial, name, list, type, wrong, right, vp);
        //显示窗口
        bottomWindow.showAtLocation(MineLoveExamActivity.this.findViewById(R.id.ll_1), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
        backgroundAlpha(0.5f);
        bottomWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_delet:
                    if (!type.equals("8") && list != null && list.size() > 0) {
                        if (vp == null) return;
                        DbCore.getDaoSession().getTitleRecordLoveDao().deleteAll();
                        initAdapter();
                        vp.setCurrentItem(0);
                        initLoveAndNum(0);
                        wrong = 0;
                        right = 0;
                        MoudleUtils.textViewSetText(tv_wrong, wrong + "");
                        MoudleUtils.textViewSetText(tv_right, right + "");
                        if (bottomWindow != null) {
                            if (bottomWindow.isShowing()) {
                                bottomWindow.dismiss();
                            }
                        }
                        if (upIsTrueInterfaceMineLove != null) {
                            upIsTrueInterfaceMineLove.upIsTrue();
                        }
                    }
                    break;
            }

        }
    };

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
}
