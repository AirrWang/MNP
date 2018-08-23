package com.mm.medicalman.Mine;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mm.medicalman.AcH5Activity;
import com.mm.medicalman.Api.NpApi;
import com.mm.medicalman.Bean.BeanTool;
import com.mm.medicalman.Bean.SetBean;
import com.mm.medicalman.LoginActivity;
import com.mm.medicalman.MainActivity;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.AppConfig;
import com.mm.medicalman.Tool.DataCleanManager;
import com.mm.medicalman.Tool.JpushTool;
import com.mm.medicalman.Tool.Kyloading.KyLoadingBuilder;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.Tool.RecyclerViewClickListener;
import com.mm.medicalman.Tool.RetrofitUtils;
import com.mm.medicalman.Tool.SPUtils;
import com.mm.medicalman.Tool.ToastUtils;
import com.mm.medicalman.Tool.UpVersionNameAppTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mm.medicalman.MainActivity.tokenErr;
import static com.mm.medicalman.Tool.DBUtil.DBTools.deleteDb;

/**
 * Created by NiPing and Airr Wang on 2017/10/23.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class MineSetActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    private MineSetAdapter mAdapter;
    @BindView(R.id.rv_set)
    RecyclerView recyclerView;
    @BindView(R.id.btn_out)
    Button btn_out;
    private List<SetBean> list = new ArrayList<>();
    private String[] ITEMS;
    private RecyclerView.LayoutManager mLayoutManager;
    private AlertDialog alertDialog = null;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_set);
        ButterKnife.bind(this);
        initToolbar();
        init();
        initData();
        initAdapter(list);
        initSet();
    }

    @OnClick(R.id.btn_out)
    void Out() {
        initOut(this);
        if (alertDialog != null) {
            if (!alertDialog.isShowing()) {
                alertDialog.show(); // 显示
            }
        }
    }

    private void initOut(final Context context) {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(context).setTitle("")
                    .setMessage("确定退出？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            initDataOut();
                        }

                    }).setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create(); // 创建对话框
            alertDialog.setCancelable(true);
            alertDialog.setCanceledOnTouchOutside(true);
        }
    }

    private void initDataOut() {
        MoudleUtils.dialogShow(progressDialog);
        String user_id = (String) SPUtils.get("user_id", "");
        String token = (String) SPUtils.get("token", "");
        NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
        Call<BeanTool> callBack = restApi.out(token, user_id);
        callBack.enqueue(new Callback<BeanTool>() {
            @Override
            public void onResponse(Call<BeanTool> call, Response<BeanTool> response) {
                BeanTool bean = response.body();
                if (bean == null) {
                    MoudleUtils.dialogDismiss(progressDialog);
                    ToastUtils.showShort(MineSetActivity.this, "Server Error");
                    return;
                }
                switch (bean.getStatus()) {
                    case "1":
                    case "0":
                        initTaskOutData(MineSetActivity.this);
                        break;
                }
                MoudleUtils.dialogDismiss(progressDialog);
            }

            @Override
            public void onFailure(Call<BeanTool> call, Throwable t) {
                MoudleUtils.toChekWifi(MineSetActivity.this);
                MoudleUtils.dialogDismiss(progressDialog);
            }
        });
    }

    private void initTaskOutData(Context context) {
        JpushTool.mySetAlias("");
        deleteDb();
        SPUtils.clear(context);
        if (tokenErr != null) {
            tokenErr.toFinishMain(true);
        }
        finish();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    private void initSet() {
        recyclerView.addOnItemTouchListener(new RecyclerViewClickListener(this, recyclerView,
                new RecyclerViewClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        switch (position) {
                            case 0:
                                startActivity(new Intent().setClass(MineSetActivity.this, FeedbackActivity.class));
                                break;
                            case 1:
                                initToClearCache();
                                break;
                            case 2:
                                KyLoadingBuilder kyLoadingBuilderUp = new KyLoadingBuilder(MineSetActivity.this);
                                UpVersionNameAppTool.initTaskUpdata(kyLoadingBuilderUp, MineSetActivity.this, "set");
                                break;
                            case 3:
                                startActivity(new Intent().setClass(MineSetActivity.this, AboutActivity.class));
                                break;
                            case 4:
                                Intent intent = new Intent();
                                intent.putExtra("acurl", AppConfig.h5Protocol);
                                intent.setClass(MineSetActivity.this, AcH5Activity.class);
                                startActivity(intent);
                                break;
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                    }
                }));
    }

    private void initToClearCache() {
        final KyLoadingBuilder kyLoadingBuilder = new KyLoadingBuilder(MineSetActivity.this);
        MoudleUtils.kyloadingShow(kyLoadingBuilder, "正在清除缓存...", false, false);
        DataCleanManager.cleanApplicationData(MineSetActivity.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                list.get(1).setValue("");
                initAdapter(list);
            }
        }, 1000);
    }


    private void init() {
        progressDialog = new ProgressDialog(this);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);//线性横向布局
        // 设置布局管理器
        recyclerView.setLayoutManager(mLayoutManager);
    }

    private void initData() {
        ITEMS = getResources().getStringArray(R.array.list_data_setting);
        for (String ITEM : ITEMS) {
            SetBean setBean = new SetBean();
            setBean.setTitle(ITEM);
            setBean.setValue("");
            list.add(setBean);
        }
        list.get(1).setValue(DataCleanManager.getCacheSize(this.getExternalCacheDir()));
    }

    private void initAdapter(List<SetBean> data) {
        if (mAdapter == null) {
            mAdapter = new MineSetAdapter(this);
            mAdapter.updateData(data);
            recyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.updateData(data);
            mAdapter.notifyDataSetChanged();
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

    /**
     * 页面标题
     */
    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar_title.setText("设置");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }
}
