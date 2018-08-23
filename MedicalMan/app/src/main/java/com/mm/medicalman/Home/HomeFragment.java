package com.mm.medicalman.Home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mm.medicalman.AcH5Activity;
import com.mm.medicalman.AcH5PostActivity;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.AppConfig;
import com.mm.medicalman.Tool.SPUtils;
import com.mm.medicalman.Tool.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mm.medicalman.MainActivity.mainUpAgain;

/**
 * Created by NiPing and Airr Wang on 2017/10/23.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class HomeFragment extends Fragment {
    private View view;
    @BindView(R.id.ll_eng_exam)
    LinearLayout ll_eng_exam;
    @BindView(R.id.adult_edu)
    LinearLayout ll_adult_edu;
    @BindView(R.id.ll_home_nurse)
    LinearLayout ll_home_nurse;
    private AlertDialog alertDialog = null;

    public HomeFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void H5(String url) {
        Intent intent = new Intent();
        intent.putExtra("acurl", url);
        String user_id = (String) (SPUtils.get(getActivity(), "user_id", ""));
        String token = (String) SPUtils.get(getActivity(), "token", "");
        intent.putExtra("urlParameter", "token=" + token + "&" + "user_id=" + user_id);
        intent.setClass(getActivity(), AcH5PostActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_eng_exam)
    void EngExam() {
        H5(AppConfig.h5_home_eng_exam);
    }

    @OnClick(R.id.adult_edu)
    void AdultEdu() {
        H5(AppConfig.h5_home_adult_edu);
    }

    @OnClick(R.id.ll_home_nurse)
    void NurseExam() {
        String mockResults = (String) SPUtils.get("mockResultsNp", "0");
        if (mockResults.equals("0")) {
            initAgainToBu(getActivity());
            if (alertDialog != null) {
                if (!alertDialog.isShowing()) {
                    alertDialog.show(); // 显示
                }
            }
        } else {
            startActivity(new Intent().setClass(getActivity(), HomeNurseExamActivity.class));
        }
    }


    private void initAgainToBu(Context context) {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(context).setTitle("")
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
            alertDialog.setCancelable(true);
            alertDialog.setCanceledOnTouchOutside(true);
        }
    }

    @OnClick(R.id.ll_campus)
    void ToCampus() {
        startActivity(new Intent().setClass(getActivity(), CampusActivity.class));
    }

}
