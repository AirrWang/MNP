package com.mm.medicalman.Bean;

import java.io.Serializable;

/**
 * /**
 * Created by NiPing and AirrWang on 2017/11/1.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 */

public class MockRuleMyBean implements Serializable {

    private String id;
    private String minute;//	num	考试时长,单位分钟
    private String q_num;//str	总题数
    //    q_difficulty	arr	各难度题目数量分配规则
    //    q_class	arr	各章节题目数量分配规则
    private String rule_score;//	str	出题规则
    private String rule_question;//	str	计分规则
    private String q_difficulty_type;//	str	难度类别 , 用英文逗号分隔 , 与num一一对应
    private String q_difficulty_num;//num	题目数量 , 用英文逗号分隔 , 与type一一对应
    private String q_class_c_id;//str	章节c_id , 用英文逗号分隔 , 与num一一对应
    private String q_class_num;//num	题目数量 , 用英文逗号分隔 , 与c_id一一对应

    public MockRuleMyBean(String id, String minute,String q_num, String rule_score, String rule_question,
                          String q_difficulty_type, String q_difficulty_num, String q_class_c_id,
                          String q_class_num) {
        this.id = id;
        this.minute = minute;
        this.q_num = q_num;
        this.rule_score = rule_score;
        this.rule_question = rule_question;
        this.q_difficulty_type = q_difficulty_type;
        this.q_difficulty_num = q_difficulty_num;
        this.q_class_c_id = q_class_c_id;
        this.q_class_num = q_class_num;
    }

    public MockRuleMyBean() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMinute() {
        return this.minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getQ_num() {
        return this.q_num;
    }

    public void setQ_num(String q_num) {
        this.q_num = q_num;
    }

    public String getRule_score() {
        return this.rule_score;
    }

    public void setRule_score(String rule_score) {
        this.rule_score = rule_score;
    }

    public String getRule_question() {
        return this.rule_question;
    }

    public void setRule_question(String rule_question) {
        this.rule_question = rule_question;
    }

    public String getQ_difficulty_type() {
        return this.q_difficulty_type;
    }

    public void setQ_difficulty_type(String q_difficulty_type) {
        this.q_difficulty_type = q_difficulty_type;
    }

    public String getQ_difficulty_num() {
        return this.q_difficulty_num;
    }

    public void setQ_difficulty_num(String q_difficulty_num) {
        this.q_difficulty_num = q_difficulty_num;
    }

    public String getQ_class_c_id() {
        return this.q_class_c_id;
    }

    public void setQ_class_c_id(String q_class_c_id) {
        this.q_class_c_id = q_class_c_id;
    }

    public String getQ_class_num() {
        return this.q_class_num;
    }

    public void setQ_class_num(String q_class_num) {
        this.q_class_num = q_class_num;
    }
}
