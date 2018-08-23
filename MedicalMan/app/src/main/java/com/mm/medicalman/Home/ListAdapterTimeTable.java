package com.mm.medicalman.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mm.medicalman.Bean.NoticeBean;
import com.mm.medicalman.R;

import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class ListAdapterTimeTable extends BaseAdapter {

    private List<NoticeBean.Dextail.DetailBean> list;
    private Context context;

    public ListAdapterTimeTable(Context context) {
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
                    R.layout.item_list_time, null);
            myHolder = new MyHolder();

            myHolder.tv_item = (TextView) view.findViewById(R.id.tv_item);
            myHolder.tv_time= (TextView) view.findViewById(R.id.tv_item_time);
            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }
        myHolder.tv_item.setText(position+1+"、"+list.get(position).getTitle());
        myHolder.tv_time.setText(list.get(position).getCreate_time());

        return view;
    }


    public void setList(List<NoticeBean.Dextail.DetailBean> list) {
        this.list = list;
    }


    class MyHolder {
        TextView tv_item;
        TextView tv_time;
    }


}
