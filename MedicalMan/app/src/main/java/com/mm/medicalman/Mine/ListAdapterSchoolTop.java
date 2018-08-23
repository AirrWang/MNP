package com.mm.medicalman.Mine;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mm.medicalman.Bean.MySchoolBean;
import com.mm.medicalman.R;

import java.util.List;
/**
 * Created by NiPing and Airr Wang on 2017/10/23.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */

public class ListAdapterSchoolTop extends BaseAdapter {

    public List<MySchoolBean.MySchoolDetailBean.ScoreslistBean.ScoresDetBean> getDataSource() {
        return dataSource;
    }

    public void setDataSource(List<MySchoolBean.MySchoolDetailBean.ScoreslistBean.ScoresDetBean> dataSource) {
        this.dataSource = dataSource;
    }

    private List<MySchoolBean.MySchoolDetailBean.ScoreslistBean.ScoresDetBean> dataSource;
    private Context context;


    public ListAdapterSchoolTop(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (dataSource != null) {
            return dataSource.size();
        } else {
            return 0;
        }

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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_querry_score, null);
            holder = new ViewHolder();

            holder.tv_scores = (TextView) view.findViewById(R.id.tv_scores);
            holder.tv_kemu = (TextView) view.findViewById(R.id.tv_kemu);
            holder.ll_scores= (LinearLayout) view.findViewById(R.id.ll_scores);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }


        holder.tv_kemu.setText(dataSource.get(position).getSubject());
        if (dataSource.get(position).getGrade().equals("-1")){
            holder.tv_scores.setText("缺考！");
        }else {
            holder.tv_scores.setText(dataSource.get(position).getGrade());
        }
        if (position==0){
            holder.ll_scores.setBackgroundColor(Color.parseColor("#e8f2f4"));
        }else {
            if (position%2==0){
                holder.ll_scores.setBackgroundColor(Color.parseColor("#e8f2f4"));
            }else {
                holder.ll_scores.setBackgroundColor(context.getResources().getColor(R.color.mm_ziti_bai));
            }
        }
        if (dataSource == null) {
            return null;
        } else {
            return view;
        }
    }




    static class ViewHolder {
        TextView tv_kemu;
        TextView tv_scores;
        LinearLayout ll_scores;
    }
}
