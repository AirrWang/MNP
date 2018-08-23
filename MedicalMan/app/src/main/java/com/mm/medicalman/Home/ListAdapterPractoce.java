package com.mm.medicalman.Home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mm.medicalman.DBBean.Course;
import com.mm.medicalman.DBBean.Subject;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.DBUtil.DbCore;
import com.mm.medicalman.greendao.gen.SubjectDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class ListAdapterPractoce extends BaseAdapter {

    private List<Course> list;
    private List<Subject> list_1=new ArrayList<>();
    private Context context;
    private String type;

    public ListAdapterPractoce(Context context) {
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
                    R.layout.item_list_practice, null);
            myHolder = new MyHolder();

            myHolder.tv_1 = (TextView) view.findViewById(R.id.tv_1);
            myHolder.tv_2= (TextView) view.findViewById(R.id.tv_2);
            myHolder.tv_3= (TextView) view.findViewById(R.id.tv_3);
            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }
        list_1= DbCore.getDaoSession().getSubjectDao().queryBuilder().where(SubjectDao.Properties.C_id.eq(list.get(position).getC_id()),SubjectDao.Properties.Ability.eq(type)).build().listLazyUncached();
        myHolder.tv_1.setText(list.get(position).getSerial());
        myHolder.tv_2.setText(list.get(position).getName());
        myHolder.tv_3.setText(list_1.size()+"");
        final MyHolder finalMyHolder = myHolder;
        initSet(position, view, finalMyHolder);
        return view;
    }

    private void initSet(final int position, View view, final MyHolder finalMyHolder) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalMyHolder.tv_3.getText().toString().trim().equals("0")){

                }else {
                    Intent intent = new Intent();
                    intent.putExtra("type", type);
                    intent.putExtra("c_id", list.get(position).getC_id());
                    intent.putExtra("name", list.get(position).getName());
                    intent.putExtra("serial", list.get(position).getSerial());
                    intent.setClass(context, DoExerciseActivity.class);
                    context.startActivity(intent);
                }
            }
        });
    }


    public void setList(List<Course> list) {
        this.list = list;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    class MyHolder {
        TextView tv_1;
        TextView tv_2;
        TextView tv_3;
    }


}
