package com.mm.medicalman.Mine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mm.medicalman.Bean.InformationBean;
import com.mm.medicalman.Bean.SetBean;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.FrescoUtils;
import com.mm.medicalman.Tool.MoudleUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mm.medicalman.Tool.MoudleUtils.textViewSetText;

/**
 * /**
 * Created by NiPing and AirrWang on 2017/10/24.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 */

class MineSetAdapter extends RecyclerView.Adapter<MineSetAdapter.ViewHolder> {
    private List<SetBean> list;
    private Context context;

    public MineSetAdapter(Context context) {
        super();
        this.context = context;
    }

    public void updateData(List<SetBean> data) {
        this.list = data;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mine_set, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // 绑定数据
        MoudleUtils.textViewSetText(holder.tv_title,list.get(position).getTitle());
        MoudleUtils.textViewSetText(holder.tv_value,list.get(position).getValue());
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tv_title;
        @BindView(R.id.tv_value)
        TextView tv_value;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
