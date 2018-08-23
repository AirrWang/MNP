package com.mm.medicalman.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mm.medicalman.Bean.GetyearsBean;
import com.mm.medicalman.DBBean.Course;
import com.mm.medicalman.DBBean.LoveSubject;
import com.mm.medicalman.DBBean.Subject;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.DBUtil.DbCore;
import com.mm.medicalman.greendao.gen.LoveSubjectDao;
import com.mm.medicalman.greendao.gen.SubjectDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class ListAdapterYearsPaper extends BaseAdapter {

    private List<GetyearsBean.GetyearsData.Detail> list;
    private Context context;

    public ListAdapterYearsPaper(Context context) {
        super();
        this.context = context;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = convertView;
        MyHolder myHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.item_list_over_years, null);
            myHolder = new MyHolder();

            myHolder.tv_1 = (TextView) view.findViewById(R.id.tv_1);

            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }

        myHolder.tv_1.setText(list.get(position).getName());

        return view;
    }


    public void setList(List<GetyearsBean.GetyearsData.Detail> list) {
        this.list = list;
    }


    class MyHolder {
        TextView tv_1;
    }


}
