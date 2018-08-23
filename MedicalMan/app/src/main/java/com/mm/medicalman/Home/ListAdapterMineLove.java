package com.mm.medicalman.Home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mm.medicalman.DBBean.Course;
import com.mm.medicalman.DBBean.LoveSubject;
import com.mm.medicalman.DBBean.Subject;
import com.mm.medicalman.DBBean.TopicRecord;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.DBUtil.DbCore;
import com.mm.medicalman.greendao.gen.LoveSubjectDao;
import com.mm.medicalman.greendao.gen.SubjectDao;
import com.mm.medicalman.greendao.gen.TitleRecordLoveDao;
import com.mm.medicalman.greendao.gen.TopicRecordDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class ListAdapterMineLove extends BaseAdapter {

    private List<Course> list;
    private List<Subject> list_1 = new ArrayList<>();
    private Context context;
    private String type;//3我的收藏，4我的错题


    public ListAdapterMineLove(Context context, String type) {
        super();
        this.context = context;
        this.type = type;
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
                    R.layout.item_list_practice, null);
            myHolder = new MyHolder();

            myHolder.tv_1 = (TextView) view.findViewById(R.id.tv_1);
            myHolder.tv_2 = (TextView) view.findViewById(R.id.tv_2);
            myHolder.tv_3 = (TextView) view.findViewById(R.id.tv_3);
            myHolder.ll = (LinearLayout) view.findViewById(R.id.ll);
            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }
        myHolder.tv_1.setText(list.get(position).getSerial());
        myHolder.tv_2.setText(list.get(position).getName());

        if (type.equals("3")) {//收藏
            QueryBuilder<Subject> queryBuilder = DbCore.getDaoSession().getSubjectDao().queryBuilder();
            queryBuilder.join(LoveSubject.class, LoveSubjectDao.Properties.Q_id).where(LoveSubjectDao.Properties.Is_love.eq("1"));
            list_1 = queryBuilder.where(SubjectDao.Properties.C_id.eq(list.get(position).getC_id())).build().listLazyUncached();
            myHolder.tv_3.setText(list_1.size() + "");
            final MyHolder finalMyHolder = myHolder;
            myHolder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalMyHolder.tv_3.getText().toString().equals("0")) {

                    }else {
                        DbCore.getDaoSession().getTitleRecordLoveDao().deleteAll();
                        context.startActivity(new Intent().putExtra("type", type).
                                putExtra("c_id", list.get(position).getC_id()).
                                putExtra("name", list.get(position).getName()).
                                putExtra("serial", list.get(position).getSerial()).
                                setClass(context, MineLoveExamActivity.class));
                    }
                }
            });
        } else if (type.equals("4")) {//错题
            QueryBuilder<Subject> queryBuilder = DbCore.getDaoSession().getSubjectDao().queryBuilder();
            queryBuilder.join(TopicRecord.class, TopicRecordDao.Properties.Q_id).where(TopicRecordDao.Properties.Error.gt("0")
                    , TopicRecordDao.Properties.Is_remove.eq("2"));
            list_1 = queryBuilder.where(SubjectDao.Properties.C_id.eq(list.get(position).getC_id())).build().listLazyUncached();
            myHolder.tv_3.setText(list_1.size() + "");
            final MyHolder finalMyHolder = myHolder;
            myHolder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalMyHolder.tv_3.getText().toString().equals("0")) {

                    }else {
                        DbCore.getDaoSession().getTitleRecordLoveDao().deleteAll();
                        context.startActivity(new Intent().putExtra("type", type).
                                putExtra("c_id", list.get(position).getC_id()).putExtra("name", list.get(position).getName()).
                                putExtra("serial", list.get(position).getSerial()).
                                setClass(context, MineLoveExamActivity.class));
                    }
                }
            });
        }


        return view;
    }


    public void setList(List<Course> list) {
        this.list = list;
    }


    class MyHolder {
        TextView tv_1;
        TextView tv_2;
        TextView tv_3;
        LinearLayout ll;
    }


}
