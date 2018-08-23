package com.mm.medicalman.Mine;

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

import com.mm.medicalman.AcH5PostActivity;
import com.mm.medicalman.Api.WfApi;
import com.mm.medicalman.Bean.ArticleListBean;
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
 * Created by NiPing and Airr Wang on 2017/11/7.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class MineMessageActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.lv_message)
    ListView lv_message;
    @BindView(R.id.srl_message)
    SwipeRefreshLayout swipeRefreshLayout;

    private int page = 1;
    private List<ArticleListBean.ArticleListDetail.DetailBeanArtList> listAll=new ArrayList<>();
    private View footView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        init();
        initToolbar();
        initData();
    }

    private void init() {
        footView = LayoutInflater.from(this).inflate(R.layout.next_foot, null);
        lv_message.addFooterView(footView);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayout.setBackgroundColor(Color.TRANSPARENT);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout.setOnRefreshListener(this);
        MoudleUtils.initRefrushTrue(swipeRefreshLayout);
        initSet();

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
                                    initData();
                                }
                            }
                        }
                        //判断滚动到顶部
                        if (lv_message.getFirstVisiblePosition() == 0) {

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
        Call<ArticleListBean> callBack = restApi.articleList(user_id,token, page);

        callBack.enqueue(new Callback<ArticleListBean>() {

            @Override
            public void onResponse(Call<ArticleListBean> call, Response<ArticleListBean> response) {
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayout);
                ArticleListBean bean = response.body();
                if (bean == null) {
                    return;
                }
                if (bean.getStatus().equals("1")) {
                    // 设置adapter
                    if (page == 1) {
                        listAll.clear();
                    }
                    listAll.addAll(bean.getData().getList());
                    initAdapter(listAll);
                    page++;
                } else {
                    ToastUtils.initLoginAgain(MineMessageActivity.this, bean.getErr_var());
                    ToastUtils.showShort(MineMessageActivity.this, bean.getMessage());
                }
                showFoot();
            }

            @Override
            public void onFailure(Call<ArticleListBean> call, Throwable t) {
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayout);
                MoudleUtils.toChekWifi(MineMessageActivity.this);
                showFoot();
            }
        });

    }
    private void showFoot() {
        if (footView.getVisibility() == View.VISIBLE) {
            footView.setVisibility(View.INVISIBLE);
        }
    }
    private ListAdapterMineMessage adapter;
    private void initAdapter(List<ArticleListBean.ArticleListDetail.DetailBeanArtList> listAll) {
        if (adapter == null) {
            adapter = new ListAdapterMineMessage(this);
            adapter.setDataSource(listAll);
            lv_message.setAdapter(adapter);
        } else {
            adapter.setDataSource(listAll);
            adapter.notifyDataSetChanged();
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
        toolbar_title.setText("消息");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }

    @Override
    public void onRefresh() {
        page = 1;
        initData();
    }
}
