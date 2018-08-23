package com.mm.medicalman.Mine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mm.medicalman.Api.WfApi;
import com.mm.medicalman.Bean.InformationBean;
import com.mm.medicalman.Info.AcH5InfoActivity;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.Tool.RetrofitUtils;
import com.mm.medicalman.Tool.SPUtils;
import com.mm.medicalman.Tool.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang on 2017/11/16.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class CollectActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private int page=1;

    @BindView(R.id.toolbar_title)
    TextView textView;
    @BindView(R.id.lv_collect)
    ListView lv_collect;
    @BindView(R.id.srl_collect)
    SwipeRefreshLayout swipeRefreshLayout;
    private View footView;
    private List<InformationBean.BeanRegisterData.BeanRegisterDataList> listAll = new ArrayList<>();
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        ButterKnife.bind(this);
        init();
        MoudleUtils.initRefrushTrue(swipeRefreshLayout);
        initToolbar();
        dialog = new ProgressDialog(this);
        MoudleUtils.dialogShow(dialog);
    }

    @Override
    protected void onResume() {
        super.onResume();
        page=1;
        initData();
    }

    private void init() {
        footView = LayoutInflater.from(this).inflate(R.layout.next_foot, null);
        lv_collect.addFooterView(footView);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayout.setBackgroundColor(Color.TRANSPARENT);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout.setOnRefreshListener(this);
        MoudleUtils.initRefrushTrue(swipeRefreshLayout);
        initSet();
    }
    private void initSet() {
        lv_collect.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (lv_collect.getLastVisiblePosition() == (lv_collect.getCount() - 1)) {
                            if (footView.getVisibility() != View.VISIBLE) {
                                footView.setVisibility(View.VISIBLE);
                                if (page > 1) {
                                    initData();
                                }
                            }
                        }
                        //判断滚动到顶部
                        if (lv_collect.getFirstVisiblePosition() == 0) {

                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    private void initData() {

        String user_id = (String) SPUtils.get("user_id", "");
        String token = (String) SPUtils.get("token", "");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        Call<InformationBean> callBack = restApi.collectList(user_id,token, page);

        callBack.enqueue(new Callback<InformationBean>() {

            @Override
            public void onResponse(Call<InformationBean> call, Response<InformationBean> response) {
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayout);
                InformationBean bean = response.body();
                if (bean == null) {
                    return;
                }
                if (bean.getStatus().equals("1")) {
                    // 设置adapter
                    if (page == 1) {
                        listAll.clear();
                    }
                    if (bean.getData().getList().size()==0){
                        ToastUtils.showShort(CollectActivity.this,"暂无收藏");
                    }
                    listAll.addAll(bean.getData().getList());
                    initAdapter(listAll);
                    initTo();
                    page++;
                } else {
                    ToastUtils.initLoginAgain(CollectActivity.this, bean.getErr_var());
                    ToastUtils.showShort(CollectActivity.this, bean.getMessage());
                }
                MoudleUtils.dialogDismiss(dialog);
                showFoot();
            }

            @Override
            public void onFailure(Call<InformationBean> call, Throwable t) {
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayout);
                MoudleUtils.toChekWifi(CollectActivity.this);
                MoudleUtils.dialogDismiss(dialog);
                showFoot();
            }
        });
    }

    private void initTo() {
        lv_collect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listAll==null||listAll.size()==0)return;
                Intent intent = new Intent();
                intent.putExtra("acurl", listAll.get(position).getH5_url());
                intent.putExtra("article_id",listAll.get(position).getArticle_id());
                intent.setClass(CollectActivity.this, AcH5InfoActivity.class);
                startActivity(intent);
            }
        });
    }

    private ListAdapterMineCollection adapter;
    private void initAdapter(List<InformationBean.BeanRegisterData.BeanRegisterDataList> listAll) {
        if (adapter == null) {
            adapter = new ListAdapterMineCollection(this);
            adapter.setDataSource(listAll);
            lv_collect.setAdapter(adapter);
        } else {
            adapter.setDataSource(listAll);
            adapter.notifyDataSetChanged();
        }
    }
    /**
     * 页面标题
     */
    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView.setText("收藏");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }
    /**
     * 页面返回键返回操作
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showFoot() {
        if (footView.getVisibility() == View.VISIBLE) {
            footView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onRefresh() {
        page=1;
        initData();
    }
}
