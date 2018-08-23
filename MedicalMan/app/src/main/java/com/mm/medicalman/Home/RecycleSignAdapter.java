package com.mm.medicalman.Home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mm.medicalman.Bean.SigninBean;
import com.mm.medicalman.DBBean.Subject;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.AppConfig;
import com.mm.medicalman.Tool.FrescoUtils;
import com.mm.medicalman.Tool.MoudleUtils;

import java.util.List;

/**
 * Created by NiPing and Airr Wang on 2017/8/22.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class RecycleSignAdapter extends RecyclerView.Adapter<RecycleSignAdapter.ViewHolder> {
    private List<SigninBean.SiginInfo.DetailInfo> list=null;
    private Context context;
    private int type;
    /**
     * Item拖拽滑动帮助
     */

    public RecycleSignAdapter(Context context) {
        super();
        this.context = context;
    }

    public void updateData(List<SigninBean.SiginInfo.DetailInfo> data) {
        this.list = data;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_sign, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // 绑定数据
        if (position==0){
            MoudleUtils.viewGone(holder.view);
        }
        if (position==list.size()-1){
            MoudleUtils.viewGone(holder.view_2);
        }
        if (list.get(position).getIn_status().equals("0")){
            MoudleUtils.viewGone(holder.rl_1);
            FrescoUtils.setImage(holder.simpleDraweeView2, AppConfig.res+R.drawable.gray_oval);
        }else if (list.get(position).getIn_status().equals("1")){
            MoudleUtils.viewShow(holder.rl_1);
            FrescoUtils.setImage(holder.sd_1, AppConfig.res+R.drawable.sing_in_normal);
            FrescoUtils.setImage(holder.simpleDraweeView2, AppConfig.res+R.drawable.sign_in);
        }else if (list.get(position).getIn_status().equals("2")){
            MoudleUtils.viewShow(holder.rl_1);
            FrescoUtils.setImage(holder.sd_1, AppConfig.res+R.drawable.sing_in_be_late);
            FrescoUtils.setImage(holder.simpleDraweeView2, AppConfig.res+R.drawable.sign_in);
        }
        if (list.get(position).getOut_status().equals("0")){
            MoudleUtils.viewGone(holder.rl_2);
            FrescoUtils.setImage(holder.simpleDraweeView, AppConfig.res+R.drawable.gray_oval);

        }else if (list.get(position).getOut_status().equals("1")){
            MoudleUtils.viewShow(holder.rl_2);
            FrescoUtils.setImage(holder.sd, AppConfig.res+R.drawable.sing_in_normal);
            FrescoUtils.setImage(holder.simpleDraweeView, AppConfig.res+R.drawable.sign_in);
        }else if (list.get(position).getOut_status().equals("2")){
            MoudleUtils.viewShow(holder.rl_2);
            FrescoUtils.setImage(holder.sd, AppConfig.res+R.drawable.sing_in_leave_early);
            FrescoUtils.setImage(holder.simpleDraweeView, AppConfig.res+R.drawable.sign_in);
        }
        holder.textView7.setText(list.get(position).getStart_time());
        holder.textView1.setText(list.get(position).getEnd_time());
        holder.textView17.setText(list.get(position).getIn_time()+"  签到");
        holder.text.setText(list.get(position).getOut_time()+"  签退");
        holder.tv_1.setText(list.get(position).getIn_adress());
        holder.tv_2.setText(list.get(position).getOut_adress());

        if (type==1){
            holder.textView2.setText("下课");
            holder.textView16.setText("上课");
        }else {
            holder.textView2.setText("下班");
            holder.textView16.setText("上班");
        }

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView simpleDraweeView2;
        SimpleDraweeView sd_1;
        TextView textView7;
        TextView textView16;
        TextView textView17;
        RelativeLayout rl_1;
        View view;
        View view_2;
        TextView tv_1;

        SimpleDraweeView simpleDraweeView;
        SimpleDraweeView sd;
        TextView textView1;
        TextView textView2;
        TextView text;
        RelativeLayout rl_2;
        TextView tv_2;


        public ViewHolder(View itemView) {
            super(itemView);
            view=itemView.findViewById(R.id.view);
            view_2=itemView.findViewById(R.id.view_2);
            simpleDraweeView2= (SimpleDraweeView) itemView.findViewById(R.id.simpleDraweeView2);
            textView7 = (TextView) itemView.findViewById(R.id.textView7);
            textView16= (TextView) itemView.findViewById(R.id.textView16);
            text= (TextView) itemView.findViewById(R.id.text);
            rl_1= (RelativeLayout) itemView.findViewById(R.id.rl_1);
            sd_1= (SimpleDraweeView) itemView.findViewById(R.id.sd_1);
            tv_2= (TextView) itemView.findViewById(R.id.tv_2);

            simpleDraweeView= (SimpleDraweeView) itemView.findViewById(R.id.simpleDraweeView);
            textView1 = (TextView) itemView.findViewById(R.id.textView1);
            textView2= (TextView) itemView.findViewById(R.id.textView2);
            textView17= (TextView) itemView.findViewById(R.id.textView17);
            rl_2= (RelativeLayout) itemView.findViewById(R.id.rl_2);
            sd= (SimpleDraweeView) itemView.findViewById(R.id.sd);
            tv_1= (TextView) itemView.findViewById(R.id.tv_1);
        }

    }
}
