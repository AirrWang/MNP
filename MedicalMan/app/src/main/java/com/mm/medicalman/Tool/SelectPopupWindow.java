package com.mm.medicalman.Tool;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mm.medicalman.DBBean.Subject;
import com.mm.medicalman.Home.RecycleBottomAdapter;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.Kyloading.KyLoadingView;

import java.util.List;

/**
 * Created by NiPing and Airr Wang on 2017/11/3.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class SelectPopupWindow extends PopupWindow {


    private Context context;
    private View mMenuView;
    private RecyclerView rv_bottom;

    // type 3我的收藏， 4我的错题， 7随机练习,  专业实务:1, 实践能力:2， 8模拟考试错题解析
    public SelectPopupWindow(Context context, View.OnClickListener itemsOnClick, int p, int size, boolean a, String serial, String name, List<Subject>
            list, String type, int wrong, int right, ViewPager vp) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.alert_dialog, null);

        initView(itemsOnClick, a, p, size, serial, name, list, type, right, wrong, vp);

        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(80555555);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

    private void initView(View.OnClickListener itemsOnClick, boolean a, int p, int size,
                          String serial, String name, List<Subject> list, String type, int right, int wrong, ViewPager vp) {
        LinearLayout ll_delet = (LinearLayout) mMenuView.findViewById(R.id.ll_delet);
        LinearLayout ll_love = (LinearLayout) mMenuView.findViewById(R.id.ll_love);
        LinearLayout ll_delet_is = (LinearLayout) mMenuView.findViewById(R.id.ll_delet_is);
        LinearLayout pop_layout = (LinearLayout) mMenuView.findViewById(R.id.pop_layout);
        if (type.equals("8")) {
            MoudleUtils.viewInvisibily(pop_layout);
        } else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) (ScreenUtils.getScreenWidth()), KyLoadingView.dip2px(context, 45));
            pop_layout.setLayoutParams(layoutParams);
            MoudleUtils.viewShow(pop_layout);
        }
        if (type.equals("4")) {
            MoudleUtils.viewGone(ll_love);
            MoudleUtils.viewShow(ll_delet_is);
            TextView textViewIsRemove = (TextView) mMenuView.findViewById(R.id.tv_delet_is);
            if (a) {
                MoudleUtils.textViewSetText(textViewIsRemove, "已移除");
            } else {
                MoudleUtils.textViewSetText(textViewIsRemove, "移除");
            }
        } else {
            SimpleDraweeView sd_love = (SimpleDraweeView) mMenuView.findViewById(R.id.sd_love);
            if (a) {
                FrescoUtils.setImage(sd_love, AppConfig.res + R.drawable.collect_selected);
            } else {
                FrescoUtils.setImage(sd_love, AppConfig.res + R.drawable.collect_unselected);
            }
            MoudleUtils.viewGone(ll_delet_is);
            MoudleUtils.viewShow(ll_love);
        }
        //设置按钮监听
        ll_delet.setOnClickListener(itemsOnClick);


        TextView tv_postion = (TextView) mMenuView.findViewById(R.id.tv_postion);
        tv_postion.setText(p + 1 + "");
        TextView tv_total = (TextView) mMenuView.findViewById(R.id.tv_total);
        tv_total.setText("/" + size);
        TextView tv_1 = (TextView) mMenuView.findViewById(R.id.tv_1);
        if (!type.equals("7") && !type.equals("8")) {
            tv_1.setText(serial);
        }
        TextView tv_2 = (TextView) mMenuView.findViewById(R.id.tv_2);
        if (!type.equals("7") && !type.equals("8")) {
            tv_2.setText(name);
        }
        TextView tv_wrong = (TextView) mMenuView.findViewById(R.id.tv_wrong);
        tv_wrong.setText(wrong + "");
        TextView tv_right = (TextView) mMenuView.findViewById(R.id.tv_right);
        tv_right.setText(right + "");


        rv_bottom = (RecyclerView) mMenuView.findViewById(R.id.rv_bottom);
        GridLayoutManager mLayoutManager = new GridLayoutManager(context, 6);
        rv_bottom.setLayoutManager(mLayoutManager);
        RecycleBottomAdapter mAdapter = new RecycleBottomAdapter(context, type, vp, this);
        mAdapter.updateData(list);
        rv_bottom.setAdapter(mAdapter);
    }

}
