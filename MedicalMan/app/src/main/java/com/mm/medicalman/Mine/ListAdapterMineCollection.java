package com.mm.medicalman.Mine;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mm.medicalman.AcH5PostActivity;
import com.mm.medicalman.Bean.InformationBean;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.AppConfig;
import com.mm.medicalman.Tool.FrescoUtils;
import com.mm.medicalman.Tool.SPUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mm.medicalman.Tool.MoudleUtils.textViewSetText;

/**
 * Created by NiPing and AirrWang
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */

public class ListAdapterMineCollection extends BaseAdapter {

    public List<InformationBean.BeanRegisterData.BeanRegisterDataList> getDataSource() {
        return dataSource;
    }

    public void setDataSource(List<InformationBean.BeanRegisterData.BeanRegisterDataList> dataSource) {
        this.dataSource = dataSource;
    }

    private List<InformationBean.BeanRegisterData.BeanRegisterDataList> dataSource;
    private Context context;


    public ListAdapterMineCollection(Context context) {
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collect, null);
            holder = new ViewHolder(view);

            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        // 绑定数据
        FrescoUtils.setImage(holder.sd_pic, dataSource.get(position).getCover());
        textViewSetText(holder.tv_author, dataSource.get(position).getAuthor());
        if (dataSource.get(position).getType().equals("1")) {
            textViewSetText(holder.tv_num, "");
            textViewSetText(holder.tv_haved_people, "");
        } else if (dataSource.get(position).getType().equals("2")) {
            textViewSetText(holder.tv_haved_people, "已报名" + dataSource.get(position).getApp_num() + "人");
            textViewSetText(holder.tv_num, "共" + dataSource.get(position).getLength() + "课时");
        }
        textViewSetText(holder.tv_title, dataSource.get(position).getTitle());
        //1：无；2:热门；3：重点
        if (dataSource.get(position).getLabel().equals("1")) {
            holder.text_type.setBackgroundColor(context.getResources().getColor(R.color.mm_ziti_bai));
            textViewSetText(holder.text_type, "");
        } else if (dataSource.get(position).getLabel().equals("2")) {
            holder.text_type.setBackgroundColor(context.getResources().getColor(R.color.c_ca9a3f));
            textViewSetText(holder.text_type, "热门");
        } else if (dataSource.get(position).getLabel().equals("3")) {
            holder.text_type.setBackgroundColor(context.getResources().getColor(R.color.c_d1685b));
            textViewSetText(holder.text_type, "重点");
        }
        textViewSetText(holder.tv_author, dataSource.get(position).getAuthor());


        if (dataSource == null) {
            return null;
        } else {
            return view;
        }
    }




    static class ViewHolder {

        @BindView(R.id.sd_pic)
        SimpleDraweeView sd_pic;
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_author)
        TextView tv_author;
        @BindView(R.id.tv_haved_people)
        TextView tv_haved_people;
        @BindView(R.id.tv_num)
        TextView tv_num;
        @BindView(R.id.text_type)
        TextView text_type;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
