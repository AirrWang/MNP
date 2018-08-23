package com.mm.medicalman.Tool;

import android.os.Environment;

/**
 * Created by NiPing and Airr Wang on 2017/10/23.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class AppConfig {
    public static String res = "res://com.mm.medicalman/";
    public static String file = "file://";
    public static String h5_home_eng_exam = RetrofitUtils.baseUrlH5 + "eng_exam/index";//设置运动权限
    public static String h5_home_adult_edu = RetrofitUtils.baseUrlH5 + "adult_edu/index";//设置运动权限
    public static String h5Protocol = RetrofitUtils.baseUrlH5 + "setting/agreement";//设置里的协议页面
}
