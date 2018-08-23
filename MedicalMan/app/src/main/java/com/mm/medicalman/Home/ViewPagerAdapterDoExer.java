package com.mm.medicalman.Home;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mm.medicalman.DBBean.Subject;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.AppConfig;
import com.mm.medicalman.Tool.FrescoUtils;
import com.mm.medicalman.Tool.MixtureTextView;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.Tool.MyListView;

import java.util.List;

/**
 * Created by NiPing and Airr Wang on 2017/11/1.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class ViewPagerAdapterDoExer extends PagerAdapter {
    private ViewPager vp;
    List<Subject> data;
    Context context;
    private String type;//  专业实务:1   ,实践能力:2，  收藏：3，  错题分析:4， 模拟考试:5， 历年真题：6,  随机练习：7，错题解析：8
    private TextView shadowView;

    public ViewPagerAdapterDoExer(TextView shadowView,List<Subject> data, Context context, ViewPager vp, String type) {
        // TODO Auto-generated constructor stub
        this.shadowView=shadowView;
        this.data = data;
        this.context = context;
        this.vp = vp;
        this.type = type;
    }
    public ViewPagerAdapterDoExer(List<Subject> data, Context context, ViewPager vp, String type) {
        // TODO Auto-generated constructor stub
        this.data = data;
        this.context = context;
        this.vp = vp;
        this.type = type;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.do_exer_layout, null);
        MixtureTextView mtv_1 = (MixtureTextView) view.findViewById(R.id.mtv_1);
        mtv_1.setText(position + 1 + "、" + data.get(position).getQuestion());
        MyListView mlv_options = (MyListView) view.findViewById(R.id.mlv_options);
        initView(position, view);

        initAdapter(mlv_options, position, view);

        container.addView(view);
        return view;

    }

    private void initView(int position, View view) {
        if (data.get(position).getPoint()!=null){
            TextView tv_point = (TextView) view.findViewById(R.id.tv_point);
            MoudleUtils.textViewSetText(tv_point,"考点    "+data.get(position).getPoint());
        }
        if (data.get(position).getAnswer()!=null) {
            TextView tv_answer = (TextView) view.findViewById(R.id.tv_answer);
            if (data.get(position).getAnswer().equals("1")) {
                tv_answer.setText("答案    A");
            } else if (data.get(position).getAnswer().equals("2")) {
                tv_answer.setText("答案    B");
            } else if (data.get(position).getAnswer().equals("3")) {
                tv_answer.setText("答案    C");
            } else if (data.get(position).getAnswer().equals("4")) {
                tv_answer.setText("答案    D");
            } else if (data.get(position).getAnswer().equals("5")) {
                tv_answer.setText("答案    E");
            }
        }
        if (data.get(position).getAnalysis()!=null) {
            TextView tv_analysis = (TextView) view.findViewById(R.id.tv_analysis);
            tv_analysis.setText(data.get(position).getAnalysis());
        }
        if (data.get(position).getDifficulty()!=null) {
            LinearLayout ll_nd = (LinearLayout) view.findViewById(R.id.ll_nd);
            MoudleUtils.viewShow(ll_nd);
            SimpleDraweeView sd_1 = (SimpleDraweeView) view.findViewById(R.id.sd_1);
            SimpleDraweeView sd_2 = (SimpleDraweeView) view.findViewById(R.id.sd_2);
            SimpleDraweeView sd_3 = (SimpleDraweeView) view.findViewById(R.id.sd_3);
            if (data.get(position).getDifficulty().equals("1")) {
                FrescoUtils.setImage(sd_1, AppConfig.res + R.drawable.difficulty_have);
            } else if (data.get(position).getDifficulty().equals("2")) {
                FrescoUtils.setImage(sd_1, AppConfig.res + R.drawable.difficulty_have);
                FrescoUtils.setImage(sd_2, AppConfig.res + R.drawable.difficulty_have);
            } else if (data.get(position).getDifficulty().equals("3")) {
                FrescoUtils.setImage(sd_1, AppConfig.res + R.drawable.difficulty_have);
                FrescoUtils.setImage(sd_2, AppConfig.res + R.drawable.difficulty_have);
                FrescoUtils.setImage(sd_3, AppConfig.res + R.drawable.difficulty_have);
            }
        }else {
            LinearLayout ll_nd = (LinearLayout) view.findViewById(R.id.ll_nd);
            MoudleUtils.viewInvisibily(ll_nd);
        }
    }




    private void initAdapter(MyListView mlv_options, int position, View view) {
        //type,专业实务:1,实践能力:2，收藏：3，错题分析：4   ,考试:5， 历年真题：6,  随机练习：7
        switch (type) {
            case "1":
                ListAdapterProfessionalPractice adapter1 = new ListAdapterProfessionalPractice(context, position, view, vp);
                adapter1.setDataSource(data);
                mlv_options.setAdapter(adapter1);
                break;
            case "2":
                ListAdapterExerOptions adapter2 = new ListAdapterExerOptions(context, position, view, vp);
                adapter2.setDataSource(data);
                mlv_options.setAdapter(adapter2);
                break;
            case "3":
            case "4":
            case "7":
            case "8":
                ListAdapterLove adapter3 = new ListAdapterLove(context, position, view, vp,type);
                adapter3.setDataSource(data);
                mlv_options.setAdapter(adapter3);
                break;
            case "5":
            case "6":
                ListAdapteExamination adapter5 = new ListAdapteExamination(shadowView,context, position, view, vp, type,data.size());
                adapter5.setDataSource(data);
                mlv_options.setAdapter(adapter5);
                break;

        }

    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
