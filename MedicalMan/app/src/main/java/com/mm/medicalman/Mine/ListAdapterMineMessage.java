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
import com.mm.medicalman.Bean.ArticleListBean;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.AppConfig;
import com.mm.medicalman.Tool.FrescoUtils;
import com.mm.medicalman.Tool.SPUtils;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */

public class ListAdapterMineMessage extends BaseAdapter {

    public List<ArticleListBean.ArticleListDetail.DetailBeanArtList> getDataSource() {
        return dataSource;
    }

    public void setDataSource(List<ArticleListBean.ArticleListDetail.DetailBeanArtList> dataSource) {
        this.dataSource = dataSource;
    }

    private List<ArticleListBean.ArticleListDetail.DetailBeanArtList> dataSource;
    private Context context;


    public ListAdapterMineMessage(Context context) {
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_mine_message, null);
            holder = new ViewHolder();
            holder.tv_title= (TextView) view.findViewById(R.id.tv_title);
            holder.tv_intro= (TextView) view.findViewById(R.id.tv_intro);
            holder.tv_time= (TextView) view.findViewById(R.id.tv_time);
            holder.sd_cover= (SimpleDraweeView) view.findViewById(R.id.sd_cover);
            holder.sd_cicle= (SimpleDraweeView) view.findViewById(R.id.sd_cicle);
            holder.ll_item_message= (LinearLayout) view.findViewById(R.id.ll_item_message);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        holder.tv_title.setText(dataSource.get(position).getTitle());
        holder.tv_intro.setText(dataSource.get(position).getBrief());
        holder.tv_time.setText(dataSource.get(position).getTimestamp());
        FrescoUtils.setImage(holder.sd_cover, dataSource.get(position).getPicture());
        //is_read : 1无红点 2有红点
        if (dataSource.get(position).getIs_read()==2){
            FrescoUtils.setImage(holder.sd_cicle, AppConfig.res+R.drawable.red_round);
        }else {
            FrescoUtils.setImage(holder.sd_cicle, "");
        }
        holder.ll_item_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("acurl", dataSource.get(position).getUrl());
                String user_id = (String) (SPUtils.get(context, "user_id", ""));
                String token = (String) SPUtils.get(context, "token", "");
                intent.putExtra("urlParameter", "token=" + token + "&" + "user_id=" + user_id);
                intent.setClass(context, AcH5PostActivity.class);
                context.startActivity(intent);
                dataSource.get(position).setIs_read(1);
                notifyDataSetChanged();
            }
        });


        if (dataSource == null) {
            return null;
        } else {
            return view;
        }

    }




    static class ViewHolder {
        TextView tv_title;
        TextView tv_intro;
        TextView tv_time;
        SimpleDraweeView sd_cover;
        SimpleDraweeView sd_cicle;
        LinearLayout ll_item_message;
    }
}
