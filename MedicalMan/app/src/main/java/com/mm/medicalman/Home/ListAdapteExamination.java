package com.mm.medicalman.Home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mm.medicalman.DBBean.Subject;
import com.mm.medicalman.DBBean.TitleRecordExamination;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.AppConfig;
import com.mm.medicalman.Tool.DBUtil.DbCore;
import com.mm.medicalman.Tool.FrescoUtils;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.Tool.ToastUtils;
import com.mm.medicalman.greendao.gen.TitleRecordExaminationDao;

import java.util.List;

import static com.mm.medicalman.Home.ExaminationActivity.examinationTimeEnd;
import static com.mm.medicalman.Tool.DBUtil.DBTools.rightErrAllNum;

/**
 * Created by NiPing and Airr Wang on 2017/10/23.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class ListAdapteExamination extends BaseAdapter {
    private ViewPager vp;
    private View view;
    private int pos;
    private String[] s = {"0", "0", "0", "0", "0"};
    private String type;//5模拟考试，6历年试题考试

    public List<Subject> getDataSource() {
        return dataSource;
    }

    public void setDataSource(List<Subject> dataSource) {
        this.dataSource = dataSource;
    }

    private List<Subject> dataSource;
    private Context context;
    private TitleRecordExaminationDao dao;
    private TitleRecordExamination bean;
    private int size;
    private LinearLayout ll_1;
    private TextView shadowView;

    public ListAdapteExamination(TextView shadowView, Context context, int position, View view, ViewPager vp, String type, int size) {
        this.shadowView = shadowView;
        this.context = context;
        this.pos = position;
        this.view = view;
        this.vp = vp;
        this.type = type;
        dao = DbCore.getDaoSession().getTitleRecordExaminationDao();
        this.size = size;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 5;

    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = null;
        ViewHolder holder = null;
        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_exercise_options, null);
            holder = new ViewHolder();
            holder.tv_1 = (TextView) view.findViewById(R.id.tv_1);
            holder.sd_1 = (SimpleDraweeView) view.findViewById(R.id.sd_1);
            holder.ll_1 = (LinearLayout) view.findViewById(R.id.ll_1);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        initSaveRightNum(pos);
        if (position == 0) {
            initSetData(position, holder, dataSource.get(pos).getOption1(), holder.sd_1, AppConfig.res + R.drawable.option_a);
        } else if (position == 1) {
            initSetData(position, holder, dataSource.get(pos).getOption2(), holder.sd_1, AppConfig.res + R.drawable.option_b);
        } else if (position == 2) {
            initSetData(position, holder, dataSource.get(pos).getOption3(), holder.sd_1, AppConfig.res + R.drawable.option_c);
        } else if (position == 3) {
            initSetData(position, holder, dataSource.get(pos).getOption4(), holder.sd_1, AppConfig.res + R.drawable.option_d);
        } else if (position == 4) {
            initSetData(position, holder, dataSource.get(pos).getOption5(), holder.sd_1, AppConfig.res + R.drawable.option_e);
        }
        initSet(holder, position);

        if (dataSource == null) {
            return null;
        } else {
            return view;
        }
    }

    private void initSetData(int position, ViewHolder holder, String option1, SimpleDraweeView sd_1, String url) {
        holder.tv_1.setText(option1);
        if (s[position].equals("0")) {
            FrescoUtils.setImage(sd_1, url);
        } else if (s[position].equals("1")) {
            FrescoUtils.setImage(sd_1, AppConfig.res + R.drawable.exercise_wrong);
            if (type.equals("6")) {
                initShow();
            }
        } else if (s[position].equals("2")) {
            FrescoUtils.setImage(sd_1, AppConfig.res + R.drawable.exercise_right);
        }
    }


    private void initShow() {
        if (ll_1 == null) {
            ll_1 = (LinearLayout) view.findViewById(R.id.ll_bottom);
        }
        MoudleUtils.viewShow(ll_1);
    }

    private void initSaveRightNum(int pos) {
        if (bean == null) {
            bean = dao.queryBuilder()
                    .where(TitleRecordExaminationDao.Properties.Q_id.eq(dataSource.get(pos).getQ_id()))
                    .build().unique();
        } else {
            s[0] = bean.getS1();
            s[1] = bean.getS2();
            s[2] = bean.getS3();
            s[3] = bean.getS4();
            s[4] = bean.getS5();
        }
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            if (vp != null && vp.getCurrentItem() == pos) {
                vp.setCurrentItem(pos + 1);
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程

        }
    }

    private TimeCount m_timeCount;


    private void initSet(final ViewHolder holder, final int position) {
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                shadowView.setTranslationX(vp.getWidth() - positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                if (m_timeCount != null && position != pos) {
                    m_timeCount.cancel();
                    m_timeCount = null;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        holder.ll_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean == null) {
                    if (pos < size) {
                        String a = position + 1 + "";
                        if (!a.equals(dataSource.get(pos).getAnswer())) {
                            s[position] = "1";
                            s[Integer.parseInt(dataSource.get(pos).getAnswer()) - 1] = "2";
                            initErr("2");
                            long time;
                            if (type.equals("6")) {
                                time = 5000;
                            } else {
                                time = 300;
                            }
                            if (m_timeCount == null) {
                                m_timeCount = new TimeCount(time, 1000);//开始计时m分钟
                                m_timeCount.start();
                            }
                        } else {
                            s[position] = "2";
                            initErr("1");
                            vp.setCurrentItem(pos + 1);
                        }
                        notifyDataSetChanged();
                        if (pos == size - 1) {
                            initToPost(context);
                        }
                    }
                } else {
                    if (pos == size - 1) {
                        initToPost(context);
                    } else {
                        vp.setCurrentItem(pos + 1);
                    }
                }
            }
        });
    }

    /**
     * 重新登录的弹框提示
     */
    private AlertDialog alertDialog;

    public void initToPost(final Context context) {
        alertDialog = new AlertDialog.Builder(context).setTitle("")
                .setMessage("您已答题完毕\n\n是否确认提交")
                .setPositiveButton("确认提交", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        if (examinationTimeEnd != null) {
                            examinationTimeEnd.examinationTimeEnd();
                        }
                    }

                }).setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create(); // 创建对话框
        if (alertDialog != null) {
            alertDialog.setCancelable(false);
            if (!alertDialog.isShowing()) {
                alertDialog.show(); // 显示
            }
        }
    }

    /**
     * type 1对2错
     *
     * @param type
     */
    private void initErr(String type) {
        if (bean == null) {
            bean = new TitleRecordExamination(dataSource.get(pos).getC_id(), dataSource.get(pos).getQ_id(),
                    s[0], s[1], s[2], s[3], s[4], type);
            dao.insertOrReplaceInTx(bean);
            dao.detachAll();
        } else {
            bean.setS1(s[0]);
            bean.setS1(s[1]);
            bean.setS1(s[2]);
            bean.setS1(s[3]);
            bean.setS1(s[4]);
            bean.setIs_right(type);
        }
        if (this.type.equals("5")) {
            rightErrAllNum(dataSource.get(pos).getQ_id(), type);
        }
    }


    static class ViewHolder {
        TextView tv_1;
        SimpleDraweeView sd_1;
        LinearLayout ll_1;
    }
}
