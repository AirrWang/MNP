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
import com.mm.medicalman.DBBean.CourseNum;
import com.mm.medicalman.DBBean.LoveSubject;
import com.mm.medicalman.DBBean.Subject;
import com.mm.medicalman.DBBean.TitleRecordPracticeCompetence;
import com.mm.medicalman.DBBean.TitleRecordProfessionalPractice;
import com.mm.medicalman.Interface.Finish;
import com.mm.medicalman.Interface.UpAllSIzeInterface;
import com.mm.medicalman.Interface.UpIsTrueInterface;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.AppConfig;
import com.mm.medicalman.Tool.DBUtil.DbCore;
import com.mm.medicalman.Tool.FrescoUtils;
import com.mm.medicalman.Tool.Kyloading.KyLoadingBuilder;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.Tool.SelectPopupWindow;
import com.mm.medicalman.Tool.ToastUtils;
import com.mm.medicalman.Tool.ViewPagerTransfomer.HomeCardVpTransformer;
import com.mm.medicalman.greendao.gen.CourseDao;
import com.mm.medicalman.greendao.gen.CourseNumDao;
import com.mm.medicalman.greendao.gen.LoveSubjectDao;
import com.mm.medicalman.greendao.gen.SubjectDao;
import com.mm.medicalman.greendao.gen.TitleRecordPracticeCompetenceDao;
import com.mm.medicalman.greendao.gen.TitleRecordProfessionalPracticeDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NiPing and Airr Wang on 2017/11/1.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class DoExerciseActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager vp;
    private String type = "";//2个值,专业实务:1,实践能力:2
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
    public static UpIsTrueInterface upIsTrueInterfaceDoEx;
    private UpAllSIzeInterface upAllSIzeInterface;
    private LinearLayout ll_1;
    private SelectPopupWindow bottomWindow;
    private KyLoadingBuilder kyLoadingBuilder;
    private Finish mfinish;
    private TextView shadowView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_exercise);
        initFinish();
        initGetIntent();
        initToolbar();
        initId();
        initDataSize();
        intiData();
        initIsTrue();
    }

    private void initFinish() {
        mfinish = new Finish() {
            @Override
            public void Finish() {
                ToastUtils.showShort(DoExerciseActivity.this, "暂无");
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                finish();
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

    private void initIsTrue() {
        upIsTrueInterfaceDoEx = new UpIsTrueInterface() {
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

    private void intiData() {
        kyLoadingBuilder = new KyLoadingBuilder(this);
        MoudleUtils.kyloadingShow(kyLoadingBuilder);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                CourseNum courseNum = DbCore.getDaoSession().getCourseNumDao().queryBuilder().
                        where(CourseNumDao.Properties.Type.eq(type)).build().unique();
                list = DbCore.getDaoSession().getSubjectDao().queryBuilder().where(SubjectDao.Properties.C_id.eq(c_id), SubjectDao.Properties.Ability.eq(type)).orderAsc(SubjectDao.Properties.Q_num).build().listLazyUncached();
                if (list != null && list.size() > 0) {
                    upAllSIzeInterface.upAllSIze();
                }
                initAdapter(courseNum);
                if (list != null && list.size() > 0) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            isLove();
                            initBottomView();
                            MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                        }
                    });
                }
            }
        });
    }

    private void initGetIntent() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        c_id = intent.getStringExtra("c_id");
        name = intent.getStringExtra("name");
        serial = intent.getStringExtra("serial");
    }

    private void initId() {
        shadowView=(TextView)findViewById(R.id.shadowView);
        tv_right = (TextView) findViewById(R.id.tv_right_exer);
        tv_wrong = (TextView) findViewById(R.id.tv_wrong);
        vp = (ViewPager) findViewById(R.id.vp_exercise);
        vp.setPageTransformer(true, new HomeCardVpTransformer());
        LinearLayout ll_love = (LinearLayout) findViewById(R.id.ll_love);
        MoudleUtils.viewShow(ll_love);
        ll_love.setOnClickListener(this);
        sd_love = (SimpleDraweeView) findViewById(R.id.sd_love);
        tv_postion = (TextView) findViewById(R.id.tv_postion);
        tv_total = (TextView) findViewById(R.id.tv_total);
        ll_1 = (LinearLayout) findViewById(R.id.ll_1);
        ll_1.setOnClickListener(this);
    }

    private void isLove() {
        LoveSubject loveSubject = DbCore.getDaoSession().getLoveSubjectDao().queryBuilder().where(LoveSubjectDao.Properties.Q_id.eq(list.get(p).getQ_id())).build().unique();
        if (loveSubject != null) {
            if (loveSubject.getIs_love().equals("1"))
            {//收藏
                FrescoUtils.setImage(sd_love, AppConfig.res + R.drawable.collect_selected);
                a = true;
            } else if (loveSubject.getIs_love().equals("2")) {//未收藏
                FrescoUtils.setImage(sd_love, AppConfig.res + R.drawable.collect_unselected);
                a = false;
            }
        } else {                                     //未收藏
            FrescoUtils.setImage(sd_love, AppConfig.res + R.drawable.collect_unselected);
            a = false;
        }
    }

    private void initAdapter(CourseNum courseNum) {
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
                    final int po = position;
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            p = po;
                            isLove();
                            tv_postion.setText(po + 1 + "");
                            initNumPosion(po);
                        }
                    });
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
            if (courseNum != null) {
                vp.setCurrentItem(Integer.parseInt(courseNum.getC_vp_num()));
            } else {
                vp.setCurrentItem(0);
                initNumPosion(0);
            }
        }
    }

    private void initNumPosion(int po) {
        CourseNumDao dao = DbCore.getDaoSession().getCourseNumDao();
        CourseNum courseNum = new CourseNum(list.get(po).getC_id(),
                list.get(po).getAbility(), list.get(po).getQ_id(),
                po + "", type
        );
        dao.insertOrReplaceInTx(courseNum);
        dao.detachAll();
    }

    private void initBottomView() {
        if (type.equals("1") && list != null && list.size() > 0) {//专业实务
            List<TitleRecordProfessionalPractice> list_right = DbCore.getDaoSession().getTitleRecordProfessionalPracticeDao().queryBuilder().where(TitleRecordProfessionalPracticeDao.Properties.C_id.eq(c_id), TitleRecordProfessionalPracticeDao.Properties.Is_right.eq("1")).build().listLazyUncached();
            List<TitleRecordProfessionalPractice> list_wrong = DbCore.getDaoSession().getTitleRecordProfessionalPracticeDao().queryBuilder().where(TitleRecordProfessionalPracticeDao.Properties.C_id.eq(c_id), TitleRecordProfessionalPracticeDao.Properties.Is_right.eq("2")).build().listLazyUncached();
            right = list_right.size();
            tv_right.setText(right + "");
            wrong = list_wrong.size();
            tv_wrong.setText(wrong + "");
        } else if (type.equals("2") && list != null && list.size() > 0) {//实践能力
            List<TitleRecordPracticeCompetence> list_right = DbCore.getDaoSession().getTitleRecordPracticeCompetenceDao().queryBuilder().where(TitleRecordPracticeCompetenceDao.Properties.C_id.eq(c_id), TitleRecordPracticeCompetenceDao.Properties.Is_right.eq("1")).build().listLazyUncached();
            List<TitleRecordPracticeCompetence> list_wrong = DbCore.getDaoSession().getTitleRecordPracticeCompetenceDao().queryBuilder().where(TitleRecordPracticeCompetenceDao.Properties.C_id.eq(c_id), TitleRecordPracticeCompetenceDao.Properties.Is_right.eq("2")).build().listLazyUncached();
            right = list_right.size();
            tv_right.setText(right + "");
            wrong = list_wrong.size();
            tv_wrong.setText(wrong + "");
        } else {
            finish();
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
                String s_type = "";
                if (a) {
                    s_type = "2";
                    FrescoUtils.setImage(sd_love, AppConfig.res + R.drawable.collect_unselected);
                    a = false;
                } else {
                    s_type = "1";
                    FrescoUtils.setImage(sd_love, AppConfig.res + R.drawable.collect_selected);
                    a = true;
                }
                LoveSubject ls = new LoveSubject(list.get(p).getQ_id(), s_type, "2");
                DbCore.getDaoSession().getLoveSubjectDao().insertOrReplaceInTx(ls);
                DbCore.getDaoSession().getLoveSubjectDao().detachAll();
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
        bottomWindow.showAtLocation(DoExerciseActivity.this.findViewById(R.id.ll_1), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
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
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            if (vp == null) return;
                            if (type.equals("2") && list != null && list.size() > 0) {
                                List<TitleRecordPracticeCompetence> listDelete = new ArrayList<>();
                                listDelete = DbCore.getDaoSession().getTitleRecordPracticeCompetenceDao().
                                        queryBuilder().where(TitleRecordPracticeCompetenceDao.Properties.C_id.eq(c_id))
                                        .build().list();
                                if (listDelete != null && listDelete.size() > 0) {
                                    DbCore.getDaoSession().getTitleRecordPracticeCompetenceDao().deleteInTx(listDelete);
                                }
                            } else if (type.equals("1")) {
                                List<TitleRecordProfessionalPractice> listDelete = new ArrayList<>();
                                listDelete = DbCore.getDaoSession().getTitleRecordProfessionalPracticeDao().
                                        queryBuilder().where(TitleRecordProfessionalPracticeDao.Properties.C_id.eq(c_id))
                                        .build().list();
                                if (listDelete != null && listDelete.size() > 0) {
                                    DbCore.getDaoSession().getTitleRecordProfessionalPracticeDao().deleteInTx(listDelete);
                                }
                            }
                            initAdapter(null);
                            wrong = 0;
                            right = 0;
                            MoudleUtils.textViewSetText(tv_wrong, wrong + "");
                            MoudleUtils.textViewSetText(tv_right, right + "");
                            initLoveAndNum();
                            if (bottomWindow != null) {
                                if (bottomWindow.isShowing()) {
                                    bottomWindow.dismiss();
                                }
                            }
                            if (upIsTrueInterfaceDoEx != null) {
                                upIsTrueInterfaceDoEx.upIsTrue();
                            }
                        }
                    });
                    break;
            }
        }
    };

    private void initLoveAndNum() {
        p = 0;
        isLove();
        tv_postion.setText(p + 1 + "");
    }

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
