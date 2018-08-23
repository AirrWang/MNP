package com.mm.medicalman.Api;

import com.mm.medicalman.Bean.BeanRegister;
import com.mm.medicalman.Bean.BeanTool;
import com.mm.medicalman.Bean.ClassBean;
import com.mm.medicalman.Bean.CollectBean;
import com.mm.medicalman.Bean.DbUpBean;
import com.mm.medicalman.Bean.GetNewVersionBean;
import com.mm.medicalman.Bean.InformationBean;
import com.mm.medicalman.Bean.LearnBean;
import com.mm.medicalman.Bean.MockResultsBean;
import com.mm.medicalman.Bean.MockRuleBean;
import com.mm.medicalman.Bean.OveYearsQuestionBean;
import com.mm.medicalman.Bean.QuestBean;
import com.mm.medicalman.Bean.SchoollistBean;
import com.mm.medicalman.Bean.UpPicSingleTool;
import com.mm.medicalman.Bean.UserInfoBean;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * /**
 * Created by NiPing and AirrWang on 2017/10/23.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 */

public interface NpApi {
    //获取注册验证码
    //验证码类型,注册:1,找回密码:2
    @FormUrlEncoded
    @POST("general/sms_send")
    Call<BeanTool> getCode(@Field("phone") String phone, @Field("type") String type);

    //检验验证码是否正确
    @FormUrlEncoded
    @POST("general/sms_check")
    Call<BeanTool> getCodeIsTure(@Field("phone") String phone, @Field("type") String type, @Field("code") String code);

    //检验验证码是否正确
    @FormUrlEncoded
    @POST("account/register")
    Call<BeanRegister> register(@Field("phone") String phone, @Field("password") String password, @Field("code") String code); //检验验证码是否正确

    @FormUrlEncoded
    @POST("account/find_pwd")
    Call<BeanTool> findPwd(@Field("phone") String phone, @Field("new_pwd") String new_pwd, @Field("code") String code);

    @FormUrlEncoded
    @POST("article/article_list")
    Call<InformationBean> articleList(@Field("token") String token, @Field("user_id") String user_id, @Field("page") int page);

    @FormUrlEncoded
    @POST("user_center/user_info")
    Call<UserInfoBean> userInfo(@Field("token") String token, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("account/logout")
    Call<BeanTool> out(@Field("token") String token, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("user_center/edit")
    Call<BeanTool> edit(@Field("token") String token, @Field("user_id") String user_id,
                        @Field("nickname") String nickname, @Field("sex") String sex,
                        @Field("pic") String pic,
                        @Field("slogan") String slogan);

    @FormUrlEncoded
    @POST("setting/feedback")
    Call<BeanTool> feedback(@Field("token") String token, @Field("user_id") String user_id, @Field("content") String content);

    @FormUrlEncoded
    @POST("exam_db/check_update")
    Call<DbUpBean> dbCheckUpdate(@Field("token") String token, @Field("user_id") String user_id,
                                 @Field("quest_v") String quest_v, @Field("class_v") String class_v);

    /**
     * curr_v	num	post	是	当前版本,此参数值为0时全量下载
     * target_v	num	post	是	目标版本
     * page
     *
     * @param token
     * @param user_id
     * @param
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("exam_db/quest_down")
    Call<QuestBean> questDown(@Field("token") String token, @Field("user_id") String user_id,
                              @Field("curr_v") String curr_v, @Field("target_v") String target_v
            , @Field("page") long page);

    @FormUrlEncoded
    @POST("exam_db/class_down")
    Call<ClassBean> classDown(@Field("token") String token, @Field("user_id") String user_id,
                              @Field("curr_v") String curr_v, @Field("target_v") String target_v);

    @FormUrlEncoded
    @POST("exam_db/collect_down")
    Call<CollectBean> collectDown(@Field("token") String token, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("exam_db/learn_down")
    Call<LearnBean> learnDown(@Field("token") String token, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("exam_db/mock_results_down")
    Call<MockResultsBean> mockResultsDown(@Field("token") String token, @Field("user_id") String user_id, @Field("page") long page);

    @FormUrlEncoded
    @POST("exam_db/mock_rule")
    Call<MockRuleBean> mockRule(@Field("token") String token, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("exam_db/over_years_question")
    Call<OveYearsQuestionBean> OveYearsQuestion(@Field("token") String token, @Field("user_id") String user_id, @Field("page") long page, @Field("id") String id);

    @FormUrlEncoded
    @POST("exam_db/learn_up")
    Call<BeanTool> learnUp(@Field("token") String token, @Field("user_id") String user_id, @Field("q_id") String q_id, @Field("right") String right, @Field("error") String error);

    @FormUrlEncoded
    @POST("exam_db/collect_up")
    Call<BeanTool> collectUp(@Field("token") String token, @Field("user_id") String user_id, @Field("q_id") String q_id, @Field("type") String type);

    @FormUrlEncoded
    @POST("exam_db/mock_results_up")
    Call<BeanTool> mockResultsUp(@Field("token") String token, @Field("user_id") String user_id, @Field("list") String list);

    @FormUrlEncoded
    @POST("school/schoollist")
    Call<SchoollistBean> schoollist(@Field("token") String token, @Field("user_id") String user_id);

    @GET("setting/check_update")
    Call<GetNewVersionBean> update();

    @Multipart
    @POST("general/up_pic_single")
    Call<UpPicSingleTool> uploadMemberIcon(@PartMap Map<String, RequestBody> requestBodys);

}
