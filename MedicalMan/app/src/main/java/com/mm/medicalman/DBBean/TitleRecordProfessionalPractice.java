package com.mm.medicalman.DBBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by NiPing and AirrWang on 2017/11/1.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 * 与账户有关：专业实务做题情况记录1
 */
@Entity
public class TitleRecordProfessionalPractice {
    private String c_id;//num	章节id：默认为无意义-1
    @Id
    private String q_id;//num	post	是	题目id,以英文逗号分隔拼接为字符串,例如: 1
    private String s1;
    private String s2;
    private String s3;
    private String s4;
    private String s5;
    private String is_right;//1对2错0未做
    @Generated(hash = 410542948)
    public TitleRecordProfessionalPractice(String c_id, String q_id, String s1,
            String s2, String s3, String s4, String s5, String is_right) {
        this.c_id = c_id;
        this.q_id = q_id;
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
        this.s4 = s4;
        this.s5 = s5;
        this.is_right = is_right;
    }
    @Generated(hash = 1193299615)
    public TitleRecordProfessionalPractice() {
    }
    public String getC_id() {
        return this.c_id;
    }
    public void setC_id(String c_id) {
        this.c_id = c_id;
    }
    public String getQ_id() {
        return this.q_id;
    }
    public void setQ_id(String q_id) {
        this.q_id = q_id;
    }
    public String getS1() {
        return this.s1;
    }
    public void setS1(String s1) {
        this.s1 = s1;
    }
    public String getS2() {
        return this.s2;
    }
    public void setS2(String s2) {
        this.s2 = s2;
    }
    public String getS3() {
        return this.s3;
    }
    public void setS3(String s3) {
        this.s3 = s3;
    }
    public String getS4() {
        return this.s4;
    }
    public void setS4(String s4) {
        this.s4 = s4;
    }
    public String getS5() {
        return this.s5;
    }
    public void setS5(String s5) {
        this.s5 = s5;
    }
    public String getIs_right() {
        return this.is_right;
    }
    public void setIs_right(String is_right) {
        this.is_right = is_right;
    }
}
