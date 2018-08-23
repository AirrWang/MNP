package com.mm.medicalman.Info;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mm.medicalman.Bean.InformationBean;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.FrescoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mm.medicalman.Tool.MoudleUtils.textViewSetText;

/**
 * /**
 * Created by NiPing and AirrWang on 2017/10/24.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 */

class InfoFragmentAdapter extends RecyclerView.Adapter<InfoFragmentAdapter.ViewHolder> {
    private List<InformationBean.BeanRegisterData.BeanRegisterDataList> list;
    private Context context;

    public InfoFragmentAdapter(Context context) {
        super();
        this.context = context;
    }

    public void updateData(List<InformationBean.BeanRegisterData.BeanRegisterDataList> data) {
        this.list = data;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_infomation, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // 绑定数据
        FrescoUtils.setImage(holder.sd_pic, list.get(position).getCover());
        textViewSetText(holder.tv_author, list.get(position).getAuthor());
        if (list.get(position).getType().equals("1")) {
            textViewSetText(holder.tv_num, "");
            textViewSetText(holder.tv_haved_people, "");
        } else if (list.get(position).getType().equals("2")) {
            textViewSetText(holder.tv_haved_people, "已报名" + list.get(position).getApp_num() + "人");
            textViewSetText(holder.tv_num, "共" + list.get(position).getLength() + "课时");
        }
        textViewSetText(holder.tv_title, list.get(position).getTitle());
        //1：无；2:热门；3：重点
        if (list.get(position).getLabel().equals("1")) {
            holder.tv_type.setBackgroundColor(context.getResources().getColor(R.color.mm_ziti_bai));
            textViewSetText(holder.tv_type, "");
        } else if (list.get(position).getLabel().equals("2")) {
            holder.tv_type.setBackgroundColor(context.getResources().getColor(R.color.c_ca9a3f));
            textViewSetText(holder.tv_type, "热门");
        } else if (list.get(position).getLabel().equals("3")) {
            holder.tv_type.setBackgroundColor(context.getResources().getColor(R.color.c_d1685b));
            textViewSetText(holder.tv_type, "重点");
        }
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.tv_type)
        TextView tv_type;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
