package com.mm.medicalman.Tool;


import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by NiPing and Airr Wang
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class RetrofitUtils {
    /**
     * 开发和上线时要分别进行调整
     */

    private static String baseUrl = "http://www.cimtn.com/api/v1/";//baseUrl
//    private static String baseUrl = "http://test.cimtn.com/api/v1/";//baseUrl,开发：接口前缀

    public static String baseUrlH5 = "http://www.cimtn.com/api/v1/";//baseUrlH5Vn 带版本区别的h5网址前缀
//    public static String baseUrlH5 = "http://test.cimtn.com/api/v1/";//baseUrlH5Vn 带版本区别的h5网址前缀

    public static String tags = "apns_production_true";//正式
//    public static String tags = "apns_production_false";//测试

    public static Retrofit retrofit;//单例retrofit对象

    /**
     * 初始化http
     *
     * @return
     */
    public static Retrofit init() {
        //初始化
        if (retrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            setCookies(builder);
            retrofit = new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())//配置gson解析json
                    .client(builder.build())
                    .build();
        }
        return retrofit;
    }

    private static void setCookies(OkHttpClient.Builder builder) {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        builder.cookieJar(new JavaNetCookieJar(cookieManager));
    }

}
