package com.mm.medicalman.Mine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mm.medicalman.AcH5Activity;
import com.mm.medicalman.Api.WfApi;
import com.mm.medicalman.Bean.BeanTool;
import com.mm.medicalman.Bean.GetscoresBean;
import com.mm.medicalman.Bean.MySchoolBean;
import com.mm.medicalman.Bean.NoticeBean;
import com.mm.medicalman.Home.CircleIcBigActivity;
import com.mm.medicalman.Home.ListAdapterQuerryScore;
import com.mm.medicalman.Home.TimeTableActivity;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.AppConfig;
import com.mm.medicalman.Tool.FrescoUtils;
import com.mm.medicalman.Tool.KeyBoardUtils;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.Tool.MyListView;
import com.mm.medicalman.Tool.RetrofitUtils;
import com.mm.medicalman.Tool.SPUtils;
import com.mm.medicalman.Tool.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang on 2017/10/23.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class BindSchoolInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private MyListView list_chengji;
    private ListAdapterSchoolTop listAdapterQuerryScore;
    private TextView tv_title;
    private TextView tv_name;
    private TextView tv_class;
    private TextView tv_xuehao;
    private MyListView lv_message;
    private SimpleDraweeView sd_timetable;
    private ProgressDialog dialog;
    private String picUrl;
    private TextView tv_null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_info);
        initToolbar();
        initId();
        initData();

    }

    private void initData() {
        dialog=new ProgressDialog(this);
        MoudleUtils.dialogShow(dialog);
        String user_id = (String) SPUtils.get("user_id", "");
        String token = (String) SPUtils.get("token", "");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        Call<MySchoolBean> callBack = restApi.myschool(user_id,token);
        callBack.enqueue(new Callback<MySchoolBean>() {
            @Override
            public void onResponse(Call<MySchoolBean> call, Response<MySchoolBean> response) {

                if (response.body() == null) {
                    finish();
                    return;
                }
                switch (response.body().getStatus()) {
                    case "1":
                        if (response.body().getData()==null)return;
                        picUrl = response.body().getData().getSchedule();
                        initUi(response.body().getData().getScoreslist());
                        initAdapterTop(response.body().getData().getScoreslist().getScores());
                        initAdapter(response.body().getData().getNoticelist());
                        initSetItemClick(response.body().getData().getNoticelist());
                        MoudleUtils.dialogDismiss(dialog);
                        break;
                    case "0":
                        ToastUtils.initLoginAgain(BindSchoolInfoActivity.this, response.body().getErr_var());
                        ToastUtils.showShort(BindSchoolInfoActivity.this, response.body().getMessage());
                        MoudleUtils.dialogDismiss(dialog);
                        break;
                }
            }

            @Override
            public void onFailure(Call<MySchoolBean> call, Throwable t) {
                MoudleUtils.toChekWifi(BindSchoolInfoActivity.this);
                MoudleUtils.dialogDismiss(dialog);
            }
        });

    }

    private ListAdapterSchoolBottom adapter;
    private void initAdapter(List<MySchoolBean.MySchoolDetailBean.NoticelistBean> noticelist) {
        if (noticelist.size()==0){
            MoudleUtils.viewGone(lv_message);
            MoudleUtils.viewShow(tv_null);
        }
            adapter = new ListAdapterSchoolBottom(this);
            adapter.setList(noticelist);
            lv_message.setAdapter(adapter);
    }

    private void initUi(MySchoolBean.MySchoolDetailBean.ScoreslistBean scoreslist) {
        tv_title.setText(scoreslist.getTitle());
        tv_name.setText("姓名:"+scoreslist.getName());
        tv_class.setText("班级:"+scoreslist.getClassname());
        tv_xuehao.setText("学号:"+scoreslist.getStudent_id());
        FrescoUtils.setImage(sd_timetable, picUrl);
    }

    /**
     * listView绑定数据
     *
     * @param scores
     */
    private void initAdapterTop(List<MySchoolBean.MySchoolDetailBean.ScoreslistBean.ScoresDetBean> scores) {


        if (scores.size()==0){
            MySchoolBean.MySchoolDetailBean.ScoreslistBean.ScoresDetBean bean = new MySchoolBean().new MySchoolDetailBean().new ScoreslistBean().new ScoresDetBean();
            bean.setGrade("暂无");
            bean.setSubject("暂无");
            scores.add(bean);
        }
            listAdapterQuerryScore = new ListAdapterSchoolTop(this);
            listAdapterQuerryScore.setDataSource(scores);
            list_chengji.setAdapter(listAdapterQuerryScore);
    }

    private void initId() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        list_chengji = (MyListView) findViewById(R.id.list_chengji);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_class = (TextView) findViewById(R.id.tv_class);
        tv_xuehao = (TextView) findViewById(R.id.tv_xuehao);
        sd_timetable = (SimpleDraweeView) findViewById(R.id.sd_timetable);
        sd_timetable.setOnClickListener(this);
        lv_message = (MyListView) findViewById(R.id.lv_message);
        tv_null = (TextView) findViewById(R.id.tv_null);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("校园通");
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }

    private void initSetItemClick(final List<MySchoolBean.MySchoolDetailBean.NoticelistBean> noticelist) {
        if (noticelist.size()==0)return;

        lv_message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.putExtra("acurl",noticelist.get(position).getH5_url());
                intent.setClass(BindSchoolInfoActivity.this, AcH5Activity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sd_timetable://点击查看课表
                if (picUrl.equals(""))break;
                Intent intent = new Intent();
                intent.putExtra("touxiang", picUrl);
                intent.setClass(BindSchoolInfoActivity.this, CircleIcBigActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

}
