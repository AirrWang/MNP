package com.mm.medicalman.Home;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.facebook.drawee.view.SimpleDraweeView;
import com.mm.medicalman.DBBean.Subject;
import com.mm.medicalman.DBBean.TitleRecordExamination;
import com.mm.medicalman.DBBean.TitleRecordLove;
import com.mm.medicalman.DBBean.TitleRecordPracticeCompetence;
import com.mm.medicalman.DBBean.TitleRecordProfessionalPractice;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.AppConfig;
import com.mm.medicalman.Tool.DBUtil.DbCore;
import com.mm.medicalman.Tool.FrescoUtils;
import com.mm.medicalman.Tool.SelectPopupWindow;
import com.mm.medicalman.greendao.gen.TitleRecordExaminationDao;
import com.mm.medicalman.greendao.gen.TitleRecordLoveDao;
import com.mm.medicalman.greendao.gen.TitleRecordPracticeCompetenceDao;
import com.mm.medicalman.greendao.gen.TitleRecordProfessionalPracticeDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NiPing and Airr Wang on 2017/8/22.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class RecycleBottomAdapter extends RecyclerView.Adapter<RecycleBottomAdapter.ViewHolder> {
    private String type; // type 3我的收藏， 4我的错题， 7随机练习,  专业实务:1, 实践能力:2，考试报告：8
    private List<Subject> list = null;
    private Context context;
    private ViewPager vp;
    private SelectPopupWindow pw;

    /**
     * Item拖拽滑动帮助
     */

    public RecycleBottomAdapter(Context context, String type, ViewPager vp, SelectPopupWindow selectPopupWindow) {
        super();
        this.context = context;
        this.type = type;
        this.vp = vp;
        this.pw = selectPopupWindow;
    }

    public void updateData(List<Subject> data) {
        this.list = data;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_cardedit, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv_1.setText(position + 1 + "");
        // 绑定数据
        if (type.equals("1")) {//专业实务
            TitleRecordProfessionalPractice td = DbCore.getDaoSession().getTitleRecordProfessionalPracticeDao().queryBuilder().where(TitleRecordProfessionalPracticeDao.Properties.Q_id.eq(list.get(position).getQ_id())).unique();
            if (td != null) {
                if (td.getIs_right().equals("1")) {//对
                    FrescoUtils.setImage(holder.sd_1, AppConfig.res + R.drawable.right);
                    holder.tv_1.setTextColor(Color.parseColor("#30a2eb"));
                } else if (td.getIs_right().equals("2")) {//错
                    FrescoUtils.setImage(holder.sd_1, AppConfig.res + R.drawable.wrong);
                    holder.tv_1.setTextColor(Color.parseColor("#fa8080"));
                } else {
                    FrescoUtils.setImage(holder.sd_1, AppConfig.res + R.drawable.oval_gray);
                    holder.tv_1.setTextColor(Color.parseColor("#666666"));
                }
            } else {
                FrescoUtils.setImage(holder.sd_1, AppConfig.res + R.drawable.oval_gray);
                holder.tv_1.setTextColor(Color.parseColor("#666666"));
            }
        } else if (type.equals("2")) {//实践能力
            TitleRecordPracticeCompetence td = DbCore.getDaoSession().getTitleRecordPracticeCompetenceDao().queryBuilder().where(TitleRecordPracticeCompetenceDao.Properties.Q_id.eq(list.get(position).getQ_id())).unique();
            if (td != null) {
                if (td.getIs_right().equals("1")) {//对
                    FrescoUtils.setImage(holder.sd_1, AppConfig.res + R.drawable.right);
                    holder.tv_1.setTextColor(Color.parseColor("#30a2eb"));
                } else if (td.getIs_right().equals("2")) {//错
                    FrescoUtils.setImage(holder.sd_1, AppConfig.res + R.drawable.wrong);
                    holder.tv_1.setTextColor(Color.parseColor("#FA8080"));
                } else {
                    FrescoUtils.setImage(holder.sd_1, AppConfig.res + R.drawable.oval_gray);
                    holder.tv_1.setTextColor(Color.parseColor("#666666"));
                }
            } else {
                FrescoUtils.setImage(holder.sd_1, AppConfig.res + R.drawable.oval_gray);
                holder.tv_1.setTextColor(Color.parseColor("#666666"));
            }
        } else if (type.equals("3") || type.equals("4") || type.equals("7")) {//我的收藏||我的错题
            TitleRecordLove td = DbCore.getDaoSession().getTitleRecordLoveDao().queryBuilder().where(TitleRecordLoveDao.Properties.Q_id.eq(list.get(position).getQ_id())).unique();
            if (td != null) {
                if (td.getIs_right().equals("1")) {//对
                    FrescoUtils.setImage(holder.sd_1, AppConfig.res + R.drawable.right);
                    holder.tv_1.setTextColor(Color.parseColor("#30a2eb"));
                } else if (td.getIs_right().equals("2")) {//错
                    FrescoUtils.setImage(holder.sd_1, AppConfig.res + R.drawable.wrong);
                    holder.tv_1.setTextColor(Color.parseColor("#FA8080"));
                } else {
                    FrescoUtils.setImage(holder.sd_1, AppConfig.res + R.drawable.oval_gray);
                    holder.tv_1.setTextColor(Color.parseColor("#666666"));
                }
            } else {
                FrescoUtils.setImage(holder.sd_1, AppConfig.res + R.drawable.oval_gray);
                holder.tv_1.setTextColor(Color.parseColor("#666666"));
            }
        } else if (type.equals("8")) {
            TitleRecordExamination td = DbCore.getDaoSession().getTitleRecordExaminationDao().queryBuilder().where(TitleRecordExaminationDao.Properties.Q_id.eq(list.get(position).getQ_id())).unique();
            if (td != null) {
                if (td.getIs_right().equals("1")) {//对
                    FrescoUtils.setImage(holder.sd_1, AppConfig.res + R.drawable.right);
                    holder.tv_1.setTextColor(Color.parseColor("#30a2eb"));
                } else if (td.getIs_right().equals("2")) {//错
                    FrescoUtils.setImage(holder.sd_1, AppConfig.res + R.drawable.wrong);
                    holder.tv_1.setTextColor(Color.parseColor("#FA8080"));
                } else {
                    FrescoUtils.setImage(holder.sd_1, AppConfig.res + R.drawable.oval_gray);
                    holder.tv_1.setTextColor(Color.parseColor("#666666"));
                }
            } else {
                FrescoUtils.setImage(holder.sd_1, AppConfig.res + R.drawable.oval_gray);
                holder.tv_1.setTextColor(Color.parseColor("#666666"));
            }
        }
        holder.rl_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null) {
                    pw.dismiss();
                }
                if (vp != null) {
                    vp.setCurrentItem(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView sd_1;
        TextView tv_1;
        RelativeLayout rl_1;


        public ViewHolder(View itemView) {
            super(itemView);
            sd_1 = (SimpleDraweeView) itemView.findViewById(R.id.sd_1);
            tv_1 = (TextView) itemView.findViewById(R.id.tv_1);
            rl_1 = (RelativeLayout) itemView.findViewById(R.id.rl_1);
        }

    }
}
