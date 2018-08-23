package com.mm.medicalman.Api;

import com.mm.medicalman.Bean.ArticleListBean;
import com.mm.medicalman.Bean.BeanTool;
import com.mm.medicalman.Bean.CollectStatusBean;
import com.mm.medicalman.Bean.GetCodeBean;
import com.mm.medicalman.Bean.GetscoresBean;
import com.mm.medicalman.Bean.GetyearsBean;
import com.mm.medicalman.Bean.InformationBean;
import com.mm.medicalman.Bean.IsBindBean;
import com.mm.medicalman.Bean.LoginBean;
import com.mm.medicalman.Bean.MsgCountBean;
import com.mm.medicalman.Bean.MySchoolBean;
import com.mm.medicalman.Bean.NoticeBean;
import com.mm.medicalman.Bean.PickdataListBean;
import com.mm.medicalman.Bean.ScheduleBean;
import com.mm.medicalman.Bean.SigninBean;
import com.mm.medicalman.Bean.SigninHelpBean;
import com.mm.medicalman.Bean.UnionLoginBean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by NiPing and Airr Wang on 2017/10/23.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public interface WfApi {
    //登录
    @FormUrlEncoded
    @POST("account/login")
    Call<LoginBean>login(@Field("phone") String phone, @Field("password") String password);

    //登录
    @FormUrlEncoded
    @POST("account/union_bind")
    Call<LoginBean>unionBind(@Field("openid") String openid, @Field("access_token") String access_token,@Field("type") String type, @Field("unionid") String unionid,
                             @Field("phone") String phone, @Field("password") String password);

    //微信code获取的接口。
    @GET("oauth2/access_token?")
    Call<GetCodeBean> getCode(@Query("appid") String appid, @Query("secret") String secret, @Query("code") String code, @Query("grant_type") String grant_type);

    //课表联动
    @FormUrlEncoded
    @POST("school/pickdata_list")
    Call<PickdataListBean>getList(@Field("user_id") String user_id, @Field("token") String token);

    //获取学校通知
    @FormUrlEncoded
    @POST("school/notice_list")
    Call<NoticeBean>getNotice(@Field("user_id") String user_id, @Field("token") String token,@Field("page") int page, @Field("school_id") int school_id);

    //获取课程表
    @FormUrlEncoded
    @POST("school/schedule")
    Call<ScheduleBean>getSchedule(@Field("user_id") String user_id, @Field("token") String token, @Field("clas_id") int clas_id);

    //三方登录
    @FormUrlEncoded
    @POST("account/union_login")
    Call<UnionLoginBean>unionLogin(@Field("openid") String openid, @Field("access_token") String access_token, @Field("type") String type, @Field("unionid") String unionid);

    //三方注册
    @FormUrlEncoded
    @POST("account/union_register")
    Call<UnionLoginBean>unionRegister(@Field("openid") String openid, @Field("access_token") String access_token, @Field("type") String type, @Field("unionid") String unionid,
                                      @Field("phone") String phone, @Field("password") String password, @Field("code") String code);

    //成绩查询
    @FormUrlEncoded
    @POST("school/getscores")
    Call<GetscoresBean> getscores(@Field("user_id") String user_id, @Field("token") String token,@Field("phone") String phone, @Field("code") String code, @Field("idcard") String idcard);

    //提交建议
    @FormUrlEncoded
    @POST("school/advice")
    Call<BeanTool> advice(@Field("user_id") String user_id, @Field("token") String token, @Field("phone") String phone, @Field("student_id") String student_id, @Field("content") String content);

    //获取历年试题
    @FormUrlEncoded
    @POST("exam_db/over_years_paper")
    Call<GetyearsBean> getOverYears(@Field("user_id") String user_id, @Field("token") String token);

    //获取签到信息
    @FormUrlEncoded
    @POST("school/signin_info")
    Call<SigninBean> signin(@Field("user_id") String user_id, @Field("token") String token);

    //获取签到信息
    @FormUrlEncoded
    @POST("school/signin")
    Call<BeanTool> toSignin(@Field("user_id") String user_id, @Field("token") String token,@Field("att_id") int att_id, @Field("type") int type,@Field("adress") String adress);

    //获取签到信息
    @FormUrlEncoded
    @POST("school/signin_help")
    Call<SigninHelpBean> signinHelp(@Field("user_id") String user_id, @Field("token") String token);

    //获得消息列表
    @FormUrlEncoded
    @POST("user_center/msg_list")
    Call<ArticleListBean> articleList(@Field("user_id") String user_id, @Field("token") String token, @Field("page") int page);

    //未读消息数量
    @FormUrlEncoded
    @POST("user_center/msg_count")
    Call<MsgCountBean> msgCount(@Field("user_id") String user_id, @Field("token") String token);

    //我的收藏列表
    @FormUrlEncoded
    @POST("article/collect_list")
    Call<InformationBean> collectList(@Field("user_id") String user_id, @Field("token") String token, @Field("page") int page);

    //收藏状态查询
    @FormUrlEncoded
    @POST("article/collect_status")
    Call<CollectStatusBean> collectStatus(@Field("user_id") String user_id, @Field("token") String token, @Field("article_id") int article_id);

    //收藏
    @FormUrlEncoded
    @POST("article/collect")
    Call<BeanTool> collect(@Field("user_id") String user_id, @Field("token") String token, @Field("article_id") int article_id, @Field("type") String type);

    //是否绑定状态
    @FormUrlEncoded
    @POST("school/isbind")
    Call<IsBindBean> isbind(@Field("user_id") String user_id, @Field("token") String token);

    //我的校园通
    @FormUrlEncoded
    @POST("school/myschool")
    Call<MySchoolBean> myschool(@Field("user_id") String user_id, @Field("token") String token);

    //绑定
    @FormUrlEncoded
    @POST("school/schoolbind")
    Call<BeanTool> bind(@Field("user_id") String user_id, @Field("token") String token, @Field("phone") String phone, @Field("code") String code, @Field("idcard") String idcard, @Field("name") String name);
}
