package com.mm.medicalman.Home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mm.medicalman.AcH5Activity;
import com.mm.medicalman.Api.WfApi;
import com.mm.medicalman.Bean.BeanTool;
import com.mm.medicalman.Bean.GetscoresBean;
import com.mm.medicalman.R;
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
public class QuerryScoreActivity extends AppCompatActivity implements View.OnClickListener {

    private MyListView list_chengji;
    private ListAdapterQuerryScore listAdapterQuerryScore;
    private List<GetscoresBean.LoginInfoBean> listAll = new ArrayList<>();
    private EditText et_jianyi;
    private TextView tv_title;
    private TextView tv_name;
    private TextView tv_class;
    private TextView tv_xuehao;
    private TextView tv_grade;
    private TextView tv_counselor;
    private Button btn_submit;
    private TextView tv_counselor_num;
    private TextView tv_letter;
    private GetscoresBean.LoginInfoBean info;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_querry_score);
        initToolbar();
        initId();
        KeyBoardUtils.closeKeyboard(et_jianyi,this);
        Intent intent=getIntent();
        info = (GetscoresBean.LoginInfoBean) intent.getSerializableExtra("info");
        initUi(info);
        initAdapter(info);
    }

    private void initUi(GetscoresBean.LoginInfoBean info) {
            tv_title.setText(info.getTitle());
            tv_name.setText("姓名:"+info.getName());
            tv_class.setText("班级:"+info.getClassname());
            tv_xuehao.setText("学号:"+info.getStudent_id());
            tv_grade.setText(info.getGrade());
            tv_counselor.setText("辅导员: "+info.getCounselor()+"    联系电话: "+info.getCotel());
           tv_counselor_num.setText("教导主任: "+info.getDirector()+"    联系电话: "+info.getDirtel());
    }

    /**
     * listView绑定数据
     * @param info
     */
    private void initAdapter(GetscoresBean.LoginInfoBean info) {
        if (listAdapterQuerryScore == null) {
            listAdapterQuerryScore = new ListAdapterQuerryScore(this);
            listAdapterQuerryScore.setDataSource(info.getScores());
            list_chengji.setAdapter(listAdapterQuerryScore);
        } else {
            listAdapterQuerryScore.setDataSource(info.getScores());
            listAdapterQuerryScore.notifyDataSetChanged();
        }
    }

    private void initId() {
        tv_letter = (TextView) findViewById(R.id.tv_letter);
        tv_counselor_num = (TextView) findViewById(R.id.tv_counselor_num);
        tv_title = (TextView) findViewById(R.id.tv_title);
        et_jianyi = (EditText) findViewById(R.id.et_jianyi);
        list_chengji = (MyListView) findViewById(R.id.list_chengji);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_class = (TextView) findViewById(R.id.tv_class);
        tv_xuehao = (TextView) findViewById(R.id.tv_xuehao);
        tv_grade = (TextView) findViewById(R.id.tv_grade);
        tv_counselor = (TextView) findViewById(R.id.tv_counselor);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        tv_letter.setOnClickListener(this);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("成绩单");
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
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
            case R.id.tv_letter:
                Intent intent=new Intent();
                intent.putExtra("acurl",info.getLetter_url());
                intent.setClass(QuerryScoreActivity.this,AcH5Activity.class);
                startActivity(intent);
                break;
            case R.id.btn_submit:
            if (et_jianyi.getText().toString().trim().equals("")) {
                ToastUtils.showShort(QuerryScoreActivity.this, "建议不能为空");
            } else {
                submit();
            }
                break;
        }
    }

    /**
     * 提交接口
     */
    private void submit() {
        dialog = new ProgressDialog(this);
        MoudleUtils.dialogShow(dialog);
        String user_id = (String) SPUtils.get("user_id", "");
        String token = (String) SPUtils.get("token", "");
        String phone= (String) SPUtils.get("tel","");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);

        Call<BeanTool> callBack = restApi.advice(user_id,token,phone,info.getStudent_id(),et_jianyi.getText().toString().trim());

        callBack.enqueue(new Callback<BeanTool>() {
            @Override
            public void onResponse(Call<BeanTool> call, Response<BeanTool> response) {
                if (response.body()!=null){
                    if (response.body().getStatus().equals("0")){
                        ToastUtils.initLoginAgain(QuerryScoreActivity.this,response.body().getErr_var());
                        ToastUtils.showShort(QuerryScoreActivity.this, response.body().getMessage());
                    }else if (response.body().getStatus().equals("1")){
                        KeyBoardUtils.closeKeyboard(et_jianyi,QuerryScoreActivity.this);
                        finish();
                    }
                }
                MoudleUtils.dialogDismiss(dialog);
            }
            @Override
            public void onFailure(Call<BeanTool> call, Throwable t) {
                MoudleUtils.toChekWifi(QuerryScoreActivity.this);
                MoudleUtils.dialogDismiss(dialog);
            }
        });
    }
}
