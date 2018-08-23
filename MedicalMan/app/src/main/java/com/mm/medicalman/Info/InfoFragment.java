package com.mm.medicalman.Info;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.mm.medicalman.AcH5Activity;
import com.mm.medicalman.Api.NpApi;
import com.mm.medicalman.Bean.InformationBean;
import com.mm.medicalman.MainActivity;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.Tool.RecyclerViewClickListener;
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
 * Created by NiPing and Airr Wang on 2017/10/23.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class InfoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private View view;
    @BindView(R.id.rv_information)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView.LayoutManager mLayoutManager;
    private int page = 1;
    private boolean up = false;
    private InfoFragmentAdapter mAdapter;
    private List<InformationBean.BeanRegisterData.BeanRegisterDataList> listAll = new ArrayList<>();

    public InfoFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_info, container, false);
        ButterKnife.bind(this, view);
        initAdapter(listAll);
        // 将myTouchListener注册到分发列表
        toListen();
        init();
        initData();
        initSet();
        return view;
    }

    private void toListen() {
        MainActivity.MyTouchListener myTouchListener = new MainActivity.MyTouchListener() {
            @Override
            public void onTouchEvent(MotionEvent event) {
                // 处理手势事件
                String action = "";
                //在触发时回去到起始坐标
                float x = event.getX();
                float y = event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //将按下时的坐标存储
                        downX = x;
                        downY = y;
                        break;
                    case MotionEvent.ACTION_UP:
                        //获取到距离差
                        float dx = x - downX;
                        float dy = y - downY;
                        //防止是按下也判断
                        if (Math.abs(dx) > 8 && Math.abs(dy) > 8) {
                            //通过距离差判断方向
                            int orientation = getOrientation(dx, dy);
                            switch (orientation) {
                                case 'r':
                                    action = "右";
                                    break;
                                case 'l':
                                    action = "左";
                                    break;
                                case 't':
                                    action = "上";
                                    up = true;
                                    break;
                                case 'b':
                                    action = "下";
                                    up = false;
                                    break;
                            }
//                            Toast.makeText(getActivity(), "向" + action + "滑动", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        };
        ((MainActivity) this.getActivity()).registerMyTouchListener(myTouchListener);
    }

    private void init() {
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);//线性横向布局
        // 设置布局管理器
        recyclerView.setLayoutManager(mLayoutManager);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayout.setBackgroundColor(Color.TRANSPARENT);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout.setOnRefreshListener(this);
        MoudleUtils.initRefrushTrue(swipeRefreshLayout);
    }


    private void initSet() {
        // 判断滚动到底部
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        if (up) {
                            if (isSlideToBottom(recyclerView)) {
                                initData();
                            }
                        }
                        break;
                }
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerViewClickListener(getActivity(), recyclerView,
                new RecyclerViewClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (listAll==null||listAll.size()==0)return;
                        Intent intent = new Intent();
                        intent.putExtra("acurl", listAll.get(position).getH5_url());
                        intent.putExtra("article_id",listAll.get(position).getArticle_id());
                        intent.setClass(getActivity(), AcH5InfoActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                    }
                }));
    }

    private float downX;    //按下时 的X坐标
    private float downY;    //按下时 的Y坐标

    /**
     * 根据距离差判断 滑动方向
     *
     * @param dx X轴的距离差
     * @param dy Y轴的距离差
     * @return 滑动的方向
     */
    private int getOrientation(float dx, float dy) {
        if (Math.abs(dx) > Math.abs(dy)) {
            //X轴移动
            return dx > 0 ? 'r' : 'l';
        } else {
            //Y轴移动
            return dy > 0 ? 'b' : 't';
        }
    }

    private boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange()) {
            return true;
        } else {
            return false;
        }
    }

    private void initData() {
        String user_id = (String) SPUtils.get("user_id", "");
        String token = (String) SPUtils.get("token", "");
        NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
        Call<InformationBean> callBack = restApi.articleList(token, user_id, page);

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
                    listAll.addAll(bean.getData().getList());
                    initAdapter(listAll);
                    page++;
                } else {
                    ToastUtils.initLoginAgain(getActivity(), bean.getErr_var());
                    ToastUtils.showShort(getActivity(), bean.getMessage());
                }
            }

            @Override
            public void onFailure(Call<InformationBean> call, Throwable t) {
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayout);
                MoudleUtils.toChekWifi(getActivity());
            }
        });
    }

    private void initAdapter(List<InformationBean.BeanRegisterData.BeanRegisterDataList> data) {
        if (mAdapter == null) {
            mAdapter = new InfoFragmentAdapter(getActivity());
            mAdapter.updateData(data);
            recyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.updateData(data);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        initData();
    }
}
