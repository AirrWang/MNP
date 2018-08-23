package com.mm.medicalman.Home;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mm.medicalman.DBBean.Subject;
import com.mm.medicalman.DBBean.TitleRecordProfessionalPractice;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.AppConfig;
import com.mm.medicalman.Tool.DBUtil.DbCore;
import com.mm.medicalman.Tool.FrescoUtils;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.greendao.gen.TitleRecordProfessionalPracticeDao;

import java.util.List;

import static com.mm.medicalman.Tool.DBUtil.DBTools.rightErrAllNum;

/**
 * Created by NiPing and Airr Wang on 2017/10/23.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class ListAdapterProfessionalPractice extends BaseAdapter {
    private ViewPager vp;
    private View view;
    private int pos;
    private String[] s = {"0", "0", "0", "0", "0"};
    private LinearLayout ll_1;

    public List<Subject> getDataSource() {
        return dataSource;
    }

    public void setDataSource(List<Subject> dataSource) {
        this.dataSource = dataSource;
    }

    private List<Subject> dataSource;
    private Context context;
    private TitleRecordProfessionalPracticeDao dao;
    private TitleRecordProfessionalPractice titleRecordProfessionalPractice;

    public ListAdapterProfessionalPractice(Context context, int position, View view, ViewPager vp) {
        this.context = context;
        this.pos = position;
        this.view = view;
        this.vp = vp;
        dao = DbCore.getDaoSession().getTitleRecordProfessionalPracticeDao();
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

    private void initShow() {
        if (ll_1 == null) {
            ll_1 = (LinearLayout) view.findViewById(R.id.ll_bottom);
        }
        MoudleUtils.viewShow(ll_1);
    }

    private void initSetData(int position, ViewHolder holder, String option1, SimpleDraweeView sd_1, String url) {
        holder.tv_1.setText(option1);
        if (s[position].equals("0")) {
            FrescoUtils.setImage(sd_1, url);
        } else if (s[position].equals("1")) {
            FrescoUtils.setImage(sd_1, AppConfig.res + R.drawable.exercise_wrong);
            initShow();
        } else if (s[position].equals("2")) {
            FrescoUtils.setImage(sd_1, AppConfig.res + R.drawable.exercise_right);
        }
    }


    private void initSaveRightNum(int pos) {
        if (titleRecordProfessionalPractice == null) {
            titleRecordProfessionalPractice = dao.queryBuilder()
                    .where(TitleRecordProfessionalPracticeDao.Properties.Q_id.eq(dataSource.get(pos).getQ_id()))
                    .build().unique();
        } else {
            s[0] = titleRecordProfessionalPractice.getS1();
            s[1] = titleRecordProfessionalPractice.getS2();
            s[2] = titleRecordProfessionalPractice.getS3();
            s[3] = titleRecordProfessionalPractice.getS4();
            s[4] = titleRecordProfessionalPractice.getS5();
        }
    }

    private void initSet(final ViewHolder holder, final int position) {
        holder.ll_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleRecordProfessionalPractice==null) {
                    String a = position + 1 + "";
                    if (!a.equals(dataSource.get(pos).getAnswer())) {
                        s[position] = "1";
                        s[Integer.parseInt(dataSource.get(pos).getAnswer()) - 1] = "2";
                        initShow();
                        initErr("2");
                    } else {
                        s[position] = "2";
                        initErr("1");
                        vp.setCurrentItem(pos + 1);
                    }

                    notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * type 1对2错
     *
     * @param type
     */
    private void initErr(String type) {
        if (titleRecordProfessionalPractice == null) {
            titleRecordProfessionalPractice = new TitleRecordProfessionalPractice(dataSource.get(pos).getC_id(), dataSource.get(pos).getQ_id(),
                    s[0], s[1], s[2], s[3], s[4], type);
            dao.insertOrReplaceInTx(titleRecordProfessionalPractice);
            dao.detachAll();
        } else {
            titleRecordProfessionalPractice.setS1(s[0]);
            titleRecordProfessionalPractice.setS1(s[1]);
            titleRecordProfessionalPractice.setS1(s[2]);
            titleRecordProfessionalPractice.setS1(s[3]);
            titleRecordProfessionalPractice.setS1(s[4]);
            titleRecordProfessionalPractice.setIs_right(type);
        }
        rightErrAllNum(dataSource.get(pos).getQ_id(), type);
    }


    static class ViewHolder {
        TextView tv_1;
        SimpleDraweeView sd_1;
        LinearLayout ll_1;
    }
}
