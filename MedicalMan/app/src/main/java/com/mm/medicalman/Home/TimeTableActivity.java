package com.mm.medicalman.Home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mm.medicalman.AcH5Activity;
import com.mm.medicalman.Api.WfApi;
import com.mm.medicalman.Bean.NoticeBean;
import com.mm.medicalman.Bean.PickdataListBean;
import com.mm.medicalman.Bean.ScheduleBean;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.FrescoUtils;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.Tool.RetrofitUtils;
import com.mm.medicalman.Tool.SPUtils;
import com.mm.medicalman.Tool.ToastUtils;
import com.mm.medicalman.Tool.WheelView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang on 2017/10/26.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class TimeTableActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog dialog;
    private int schoolPos=1;
    private int clasPos=1;
    private int gradePos=1;
    private int finalPos=1;
    private TextView tv_choose_school;
    private TextView tv_choose_faculty;
    private TextView tv_choose_clas;
    private TextView tv_choose_grade;
    private List<String> schoolList= new ArrayList<>();;
    private List<String> facultyList= new ArrayList<>();;
    private List<String> gradeList= new ArrayList<>();;
    private List<String> clasList= new ArrayList<>();;
    private PickdataListBean bean;
    private ListView lv_message;
    private View footView;
    private int page;
    private List<NoticeBean.Dextail.DetailBean> listAll=new ArrayList<>();
    private TextView tv_1;
    private SimpleDraweeView sd_timetable;
    private TextView tv_2;
    private String picUrl="";
    private TextView tv_3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        footView = LayoutInflater.from(this).inflate(
                R.layout.next_foot, null);
        dialog = new ProgressDialog(this);
        page = 1;
        initId();
        initToolbar();
        initGetData();
    }
    /**
     * 页面标题
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbar_title= (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("课表查询");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void initId() {
        tv_choose_school = (TextView) findViewById(R.id.tv_choose_school);
        LinearLayout ll_choose_school= (LinearLayout) findViewById(R.id.ll_choose_school);
        ll_choose_school.setOnClickListener(this);
        tv_choose_faculty = (TextView) findViewById(R.id.tv_choose_faculty);
        LinearLayout ll_choose_faculty= (LinearLayout) findViewById(R.id.ll_choose_faculty);
        ll_choose_faculty.setOnClickListener(this);
        tv_choose_grade = (TextView) findViewById(R.id.tv_choose_grade);
        LinearLayout ll_choose_grade= (LinearLayout) findViewById(R.id.ll_choose_grade);
        ll_choose_grade.setOnClickListener(this);
        tv_choose_clas = (TextView) findViewById(R.id.tv_choose_clas);
        LinearLayout ll_choose_clas= (LinearLayout) findViewById(R.id.ll_choose_clas);
        ll_choose_clas.setOnClickListener(this);

        lv_message = (ListView) findViewById(R.id.lv_message);
        lv_message.addFooterView(footView);

        tv_1 = (TextView) findViewById(R.id.tv_1);
        MoudleUtils.viewGone(tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_3 = (TextView) findViewById(R.id.tv_3);

        Button btn_get_schedule= (Button) findViewById(R.id.btn_get_schedule);
        btn_get_schedule.setOnClickListener(this);
        sd_timetable = (SimpleDraweeView) findViewById(R.id.sd_timetable);
        sd_timetable.setOnClickListener(this);
    }

    private void initGetData() {
        MoudleUtils.dialogShow(dialog);
        String user_id = (String) SPUtils.get("user_id", "");
        String token = (String) SPUtils.get("token", "");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        Call<PickdataListBean> callBack = restApi.getList( user_id,token);
        callBack.enqueue(new Callback<PickdataListBean>() {
            @Override
            public void onResponse(Call<PickdataListBean> call, Response<PickdataListBean> response) {
                bean = response.body();
                if (bean == null) {
                    MoudleUtils.dialogDismiss(dialog);
                    ToastUtils.showShort(TimeTableActivity.this, "Server Error");
                    finish();
                    return;
                }
                switch (bean.getStatus()) {
                    case "1":
                        initTaskSuccess(bean);
                        break;
                    case "0":
                        ToastUtils.initLoginAgain(TimeTableActivity.this, bean.getErr_var());
                        ToastUtils.showShort(TimeTableActivity.this, bean.getMessage());
                        MoudleUtils.dialogDismiss(dialog);
                        finish();
                        break;
                }
            }

            @Override
            public void onFailure(Call<PickdataListBean> call, Throwable t) {
                MoudleUtils.dialogDismiss(dialog);
                MoudleUtils.toChekWifi(TimeTableActivity.this);
                finish();
            }
        });

    }

    private void initTaskSuccess(PickdataListBean bean) {

        for (int i=0;i<bean.getData().getList().size();i++){
            schoolList.add(bean.getData().getList().get(i).getName());
        }

        MoudleUtils.dialogDismiss(dialog);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_choose_school: //选择院校
               toShow(schoolList,"院校",1,tv_choose_school);

                break;
            case R.id.ll_choose_faculty://选择系别
                if (tv_choose_school.getText().equals("院校")){
                    ToastUtils.showShort(this,"请先选择院校");
                    break;
                }
                toShow(facultyList,"系别",2,tv_choose_faculty);

                break;
            case R.id.ll_choose_grade://选择年级
                if (tv_choose_school.getText().equals("请院校")){
                    ToastUtils.showShort(this,"请先选择院校");
                    break;
                }else {
                    if (tv_choose_faculty.getText().equals("系别")){
                        ToastUtils.showShort(this,"请先选择系别");
                        break;
                    }
                }
                toShow(gradeList,"年级",3,tv_choose_grade);

                break;
            case R.id.ll_choose_clas://选择班级
                if (tv_choose_school.getText().equals("院校")){
                    ToastUtils.showShort(this,"请先选择院校");
                    break;
                }else {
                    if (tv_choose_faculty.getText().equals("系别")){
                        ToastUtils.showShort(this,"请先选择系别");
                        break;
                    }else {
                        if (tv_choose_grade.getText().equals("年级")){
                            ToastUtils.showShort(this,"请先选择年级");
                            break;
                        }
                    }
                }
                toShow(clasList,"班级",4,tv_choose_clas);
                break;
            case R.id.btn_get_schedule:
                if (tv_choose_school.getText().equals("院校")){
                    ToastUtils.showShort(this,"请先选择院校");
                    break;
                }else {
                    if (tv_choose_faculty.getText().equals("系别")){
                        ToastUtils.showShort(this,"请先选择系别");
                        break;
                    }else {
                        if (tv_choose_grade.getText().equals("年级")){
                            ToastUtils.showShort(this,"请先选择年级");
                            break;
                        }else {
                            if (tv_choose_clas.getText().equals("班级")){
                                ToastUtils.showShort(this,"请先选择班级");
                                break;
                            }
                        }
                    }
                }
                toGetSchedule();
                break;
            case R.id.sd_timetable://点击查看课表

                    Intent intent = new Intent();
                    intent.putExtra("touxiang", picUrl);
                    intent.setClass(TimeTableActivity.this, CircleIcBigActivity.class);
                    startActivity(intent);

                break;
            default:
                break;
        }
    }

    private void toGetSchedule() {
        dialog=new ProgressDialog(this);
        MoudleUtils.dialogShow(dialog);
        String user_id = (String) SPUtils.get("user_id", "");
        String token = (String) SPUtils.get("token", "");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int a =bean.getData().getList().get(schoolPos).getFaculty().get(gradePos).getGrade().get(clasPos).getClas().get(finalPos).getId();
        Call<ScheduleBean> callBack = restApi.getSchedule(user_id,token,a);
        callBack.enqueue(new Callback<ScheduleBean>() {
            @Override
            public void onResponse(Call<ScheduleBean> call, Response<ScheduleBean> response) {

                if (response.body() == null) {
                    return;
                }

                switch (response.body().getStatus()) {
                    case "1":
                        if (response.body().getData().getSchedule()!=null&&!response.body().getData().getSchedule().equals("")) {
                            picUrl = response.body().getData().getSchedule();
                            MoudleUtils.viewShow(sd_timetable);
                            MoudleUtils.viewShow(tv_2);
                            FrescoUtils.setImage(sd_timetable,picUrl);
                        }else {
                            ToastUtils.showShort(TimeTableActivity.this, response.body().getMessage());
                            MoudleUtils.viewGone(sd_timetable);
                            MoudleUtils.viewGone(tv_2);
                            FrescoUtils.setImage(sd_timetable,"");
                        }
                        MoudleUtils.dialogDismiss(dialog);
                        break;
                    case "0":
                        ToastUtils.initLoginAgain(TimeTableActivity.this, response.body().getErr_var());
                        ToastUtils.showShort(TimeTableActivity.this, response.body().getMessage());
                        MoudleUtils.dialogDismiss(dialog);
                        break;
                }

            }

            @Override
            public void onFailure(Call<ScheduleBean> call, Throwable t) {
                MoudleUtils.toChekWifi(TimeTableActivity.this);
                MoudleUtils.dialogDismiss(dialog);
            }
        });

    }


    private void toShow(List<String> list, String str, final int type, final TextView tv) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(TimeTableActivity.this);
        View outerView = LayoutInflater.from(this).inflate(R.layout.wheel_view, null);
        final WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
        final Button buttonSure = (Button) outerView.findViewById(R.id.sure);
        wv.setOffset(1);
        wv.setItems(list);
        wv.setSeletion(0);
        alertDialog.setView(outerView).create();
        final AlertDialog alert = alertDialog.create();
        alert.setTitle(str);
        buttonSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type==1){
                    schoolPos=wv.getSeletedIndex();
                    page=1;
                    initTask();
                    tv_choose_faculty.setText("系别");
                    tv_choose_grade.setText("年级");
                    tv_choose_clas.setText("班级");
                    facultyList.clear();
                    for (int i = 0; i<bean.getData().getList().get(schoolPos).getFaculty().size(); i++){
                        facultyList.add(bean.getData().getList().get(schoolPos).getFaculty().get(i).getName());
                    }
                }else if (type==2){
                    gradePos=wv.getSeletedIndex();
                    tv_choose_grade.setText("年级");
                    tv_choose_clas.setText("班级");
                    gradeList.clear();
                    for (int i = 0; i<bean.getData().getList().get(schoolPos).getFaculty().get(gradePos).getGrade().size(); i++){
                        gradeList.add(bean.getData().getList().get(schoolPos).getFaculty().get(gradePos).getGrade().get(i).getName());
                    }
                }else if (type==3){
                    clasPos=wv.getSeletedIndex();
                    tv_choose_clas.setText("班级");
                    clasList.clear();
                    for (int i = 0; i<bean.getData().getList().get(schoolPos).getFaculty().get(gradePos).getGrade().get(clasPos).getClas().size(); i++){
                        clasList.add(bean.getData().getList().get(schoolPos).getFaculty().get(gradePos).getGrade().get(clasPos).getClas().get(i).getName());
                    }
                }else if (type==4){
                    finalPos=wv.getSeletedIndex();
                }else {}
                tv.setText(wv.getSeletedItem());
                alert.dismiss();
            }
        });
        alert.setCanceledOnTouchOutside(true);
        alert.setCancelable(true);
        alert.show();
    }
    //获取通知列表
    private void initTask() {
        String user_id = (String) SPUtils.get("user_id", "");
        String token = (String) SPUtils.get("token", "");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        Call<NoticeBean> callBack = restApi.getNotice(user_id,token,page,bean.getData().getList().get(schoolPos).getId());
        callBack.enqueue(new Callback<NoticeBean>() {
            @Override
            public void onResponse(Call<NoticeBean> call, Response<NoticeBean> response) {

                if (response.body() == null) {
                    return;
                }
                switch (response.body().getStatus()) {
                    case "1":
                        List<NoticeBean.Dextail.DetailBean> list = new ArrayList<>();
                        list = response.body().getData().getList();
                        if (page==1){
                            listAll.clear();
                        }
                        listAll.addAll(list);

                        initAdapter();
                        initSetItemClick(listAll);
                        initSet();
                        page++;
                        break;
                    case "0":
                        ToastUtils.initLoginAgain(TimeTableActivity.this, bean.getErr_var());
                        ToastUtils.showShort(TimeTableActivity.this, bean.getMessage());
                        break;
                }
                showFoot();
            }

            @Override
            public void onFailure(Call<NoticeBean> call, Throwable t) {
                MoudleUtils.toChekWifi(TimeTableActivity.this);
                showFoot();
            }
        });
    }
    private void showFoot() {
        if (footView.getVisibility() == View.VISIBLE) {
            footView.setVisibility(View.INVISIBLE);
        }
    }
    private void initSetItemClick(final List<NoticeBean.Dextail.DetailBean> list) {
        lv_message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.putExtra("acurl",list.get(position).getH5_url());
                intent.setClass(TimeTableActivity.this, AcH5Activity.class);
                startActivity(intent);
            }
        });
    }

    private ListAdapterTimeTable adapter;
    private void initAdapter() {
        if (adapter == null) {
            adapter = new ListAdapterTimeTable(this);
            adapter.setList(listAll);
            lv_message.setAdapter(adapter);
        } else {
            adapter.setList(listAll);
            adapter.notifyDataSetChanged();
        }
        MoudleUtils.viewShow(tv_1);
        MoudleUtils.viewShow(tv_3);

    }
    private void initSet() {
        lv_message.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (lv_message.getLastVisiblePosition() == (lv_message.getCount() - 1)) {
                            if (footView.getVisibility() != View.VISIBLE) {
                                footView.setVisibility(View.VISIBLE);
                                if (page > 1) {
                                    initTask();
                                }
                            }
                        }
                        //判断滚动到顶部
                        if (lv_message.getFirstVisiblePosition() == 0) {

                        }
                        break;
                }
            }
            private boolean flag;
            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem + visibleItemCount == totalItemCount && !flag) {
                    flag = true;
                } else {
                    flag = false;
                }
            }
        });
    }


}
