package com.mm.medicalman.Mine;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mm.medicalman.Api.NpApi;
import com.mm.medicalman.Api.WfApi;
import com.mm.medicalman.Bean.BeanTool;
import com.mm.medicalman.Bean.IsBindBean;
import com.mm.medicalman.Bean.MsgCountBean;
import com.mm.medicalman.Bean.UserInfoBean;
import com.mm.medicalman.Interface.UserInforInterface;
import com.mm.medicalman.LoginActivity;
import com.mm.medicalman.MainActivity;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.AppConfig;
import com.mm.medicalman.Tool.FrescoUtils;
import com.mm.medicalman.Tool.JpushTool;
import com.mm.medicalman.Tool.Kyloading.KyLoadingBuilder;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.Tool.RetrofitUtils;
import com.mm.medicalman.Tool.SPUtils;
import com.mm.medicalman.Tool.SavaDataLocalUtils;
import com.mm.medicalman.Tool.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mm.medicalman.MainActivity.mainUpAgain;
import static com.mm.medicalman.Tool.DBUtil.DBTools.deleteDb;

/**
 * Created by NiPing and Airr Wang on 2017/10/23.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class MineFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private View view;
    @BindView(R.id.sd_pic)
    SimpleDraweeView sd_pic;
    @BindView(R.id.sd_pic_small)
    SimpleDraweeView sd_pic_small;
    @BindView(R.id.textView3)
    TextView tv_nick_name;
    @BindView(R.id.tv_slogan)
    TextView tv_slogan;
    @BindView(R.id.sd_pic_sex)
    SimpleDraweeView sd_pic_sex;
    @BindView(R.id.ll_set)
    LinearLayout ll_set;
    @BindView(R.id.ll_message)
    LinearLayout ll_message;
    @BindView(R.id.tv_msg_count)
    TextView tv_msg_count;
    @BindView(R.id.srl_mine)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.ll_record)
    LinearLayout ll_record;
    @BindView(R.id.ll_collect)
    LinearLayout ll_collect;
    @BindView(R.id.ll_bind)
    LinearLayout ll_bind;

    private AlertDialog alertDialogAgain = null;
    public static UserInforInterface userInforInterface;

    public MineFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
        userInforInterface=null;
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayout.setBackgroundColor(Color.TRANSPARENT);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout.setOnRefreshListener(this);
        MoudleUtils.initRefrushTrue(swipeRefreshLayout);
        initPeopleInfor();
        initData();
        initGetMsg();
        return view;
    }

    @OnClick(R.id.ll_record)
    void ToRecord() {
        String mockResults = (String) SPUtils.get("mockResultsNp", "0");
        if (mockResults.equals("0")) {
            initAgainToBu(getActivity());
            if (alertDialogAgain != null) {
                if (!alertDialogAgain.isShowing()) {
                    alertDialogAgain.show(); // 显示
                }
            }
        } else {
            startActivity(new Intent().setClass(getActivity(), ExamHisActivity.class));
        }
    }

    @OnClick(R.id.ll_bind)
    void toBind(){
        //TODO
        isBind();
    }

    private void isBind() {
        final KyLoadingBuilder kyLoadingBuilder=new KyLoadingBuilder(getActivity());
        MoudleUtils.kyloadingShow(kyLoadingBuilder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        String user_id = (String) SPUtils.get("user_id", "");
        String token = (String) SPUtils.get("token", "");
        Call<IsBindBean> callBack = restApi.isbind(user_id,token);

        callBack.enqueue(new Callback<IsBindBean>() {
            @Override
            public void onResponse(Call<IsBindBean> call, Response<IsBindBean> response) {
                if (response.body()!=null){
                    if (response.body().getStatus().equals("0")){
                        ToastUtils.initLoginAgain(getActivity(),response.body().getErr_var());
                        ToastUtils.showShort(getActivity(), response.body().getMessage());
                    }else if (response.body().getStatus().equals("1")){
                        IsBindBean.IsBindDetailBean bean=response.body().getData();
                        if (bean==null) return;
                        if (bean.getIsbind()==1) {
                            startActivity(new Intent().setClass(getActivity(), BindSchoolInfoActivity.class));
                        }else {
                            startActivity(new Intent().putExtra("phone",bean.getPhone()).setClass(getActivity(), BindActivity.class));
                        }
                    }
                }
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
            }
            @Override
            public void onFailure(Call<IsBindBean> call, Throwable t) {
                MoudleUtils.toChekWifi(getActivity());
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
            }
        });

    }

    @OnClick(R.id.ll_collect)
    void ToCollect(){
        startActivity(new Intent().setClass(getActivity(), CollectActivity.class));
    }

    private void initAgainToBu(Context context) {
        if (alertDialogAgain == null) {
            alertDialogAgain = new AlertDialog.Builder(context).setTitle("")
                    .setMessage("此版本第一次同步题库失败，\n请联网后重新同步?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            if (mainUpAgain != null) {
                                mainUpAgain.upAgain();
                            } else {
                                ToastUtils.showShort(getActivity(), "Server Error");
                            }
                        }

                    }).setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create(); // 创建对话框
            alertDialogAgain.setCancelable(true);
            alertDialogAgain.setCanceledOnTouchOutside(true);
        }
    }

    private void initGetMsg() {

        String user_id = (String) SPUtils.get("user_id", "");
        String token = (String) SPUtils.get("token", "");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        Call<MsgCountBean> callBack = restApi.msgCount(user_id, token);
        callBack.enqueue(new Callback<MsgCountBean>() {
            @Override
            public void onResponse(Call<MsgCountBean> call, Response<MsgCountBean> response) {
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayout);
                MsgCountBean bean = response.body();
                if (bean == null) {
                    return;
                }
                switch (bean.getStatus()) {
                    case "1":
                        if (bean.getData().getCount() > 99) {
                            MoudleUtils.viewShow(tv_msg_count);
                            tv_msg_count.setText("99+");
                        } else {
                            if (bean.getData().getCount() == 0) {
                                MoudleUtils.viewGone(tv_msg_count);
                            } else {
                                MoudleUtils.viewShow(tv_msg_count);
                                tv_msg_count.setText(bean.getData().getCount() + "");
                            }
                        }

                        break;
                    case "0":
                        ToastUtils.initLoginAgain(getActivity(), bean.getErr_var());
                        ToastUtils.showShort(getActivity(), bean.getMessage());
                        break;
                }
            }

            @Override
            public void onFailure(Call<MsgCountBean> call, Throwable t) {
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayout);
            }
        });
    }

    @OnClick(R.id.ll_set)
    void set() {
        startActivity(new Intent().setClass(getActivity(), MineSetActivity.class));
    }

    @OnClick(R.id.ll_message)
    void message() {
        MoudleUtils.viewGone(tv_msg_count);
        startActivity(new Intent().setClass(getActivity(), MineMessageActivity.class));
    }


    @OnClick(R.id.sd_pic)
    void InformationEdite() {
        if (userInforInterface != null) {
            Intent intent = new Intent(getActivity(), InformationEditeActivity.class);
            startActivity(intent);
        }
    }


    private void initData() {
        String user_id = (String) SPUtils.get("user_id", "");
        String token = (String) SPUtils.get("token", "");
        NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
        Call<UserInfoBean> callBack = restApi.userInfo(token, user_id);
        callBack.enqueue(new Callback<UserInfoBean>() {
            @Override
            public void onResponse(Call<UserInfoBean> call, Response<UserInfoBean> response) {
                UserInfoBean bean = response.body();
                if (bean == null) {
                    return;
                }
                switch (bean.getStatus()) {
                    case "1":
                        initTaskSuccess(bean);
                        initEditInfoAccess();
                        break;
                    case "0":
                        ToastUtils.initLoginAgain(getActivity(), bean.getErr_var());
                        ToastUtils.showShort(getActivity(), bean.getMessage());
                        break;
                }
            }

            @Override
            public void onFailure(Call<UserInfoBean> call, Throwable t) {
                initPeopleInfor();
                initEditInfoAccess();
            }
        });
    }




    private void initTaskSuccess(UserInfoBean bean) {
        UserInfoBean.BeanData beanData = bean.getData();
        if (beanData != null) {
            SavaDataLocalUtils.saveDataString(getActivity(), "informationPicNp", beanData.getPic());
            SavaDataLocalUtils.saveDataString(getActivity(), "informationNicknameNp", beanData.getNickname());
            SavaDataLocalUtils.saveDataString(getActivity(), "informationSloganNp", beanData.getSlogan());
            SavaDataLocalUtils.saveDataString(getActivity(), "informationSexNp", beanData.getSex());
            initPeopleInfor();
        }
    }

    private void initPeopleInfor() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                inigSetData(SPUtils.get("informationPicNp", "") + "",
                        SPUtils.get("informationNicknameNp", "") + "",
                        SPUtils.get("informationSloganNp", "") + "",
                        SPUtils.get("informationSexNp", "") + "");
            }
        });
    }

    private void inigSetData(String pic, String nickname, String slogan, String sex) {
        intiToSdPic(pic);
        FrescoUtils.setImage(sd_pic_small, pic);
        MoudleUtils.textViewSetText(tv_nick_name, nickname);
        MoudleUtils.textViewSetText(tv_slogan, slogan);
        switch (sex) {//性别,:1男2女3未定义
            case "3":
                MoudleUtils.viewGone(sd_pic_sex);
                FrescoUtils.setImage(sd_pic_sex, "");
                break;
            case "2":
                FrescoUtils.setImage(sd_pic_sex, AppConfig.res + R.drawable.personal_women);
                MoudleUtils.viewShow(sd_pic_sex);
                break;
            case "1":
                FrescoUtils.setImage(sd_pic_sex, AppConfig.res + R.drawable.personal_man);
                MoudleUtils.viewShow(sd_pic_sex);
                break;
        }
    }

    private void initEditInfoAccess() {
        if (userInforInterface == null) {
            userInforInterface = new UserInforInterface() {
                @Override
                public void userInfor() {
                    initPeopleInfor();
                }
            };
        }
    }

    private void intiToSdPic(final String pic) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    FrescoUtils.downLoadImage(Uri.parse(pic), sd_pic, getActivity());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        initGetMsg();
    }
}
