package com.mm.medicalman.DBBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.util.List;

/**
 * Created by NiPing and AirrWang on 2017/11/1.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 * 与账户有关：做每到题的对错次数记录，章节练习时的正在做的题号记录
 */
@Entity
public class TopicRecord {
    @Id
    private String q_id;//num	post	是	题目id,以英文逗号分隔拼接为字符串,例如: 1
    private String right;//	num	post	是	做对次数,以英文逗号分隔拼接为字符串,例如: 1,默认0
    private String error;//	num	post	是	做错次数,以英文逗号分隔拼接为字符串,例如: 1,默认0
    private String is_remove="2";//1是2否
    private String is_up="2";//1是2否



    @Generated(hash = 783736853)
    public TopicRecord(String q_id, String right, String error, String is_remove,
            String is_up) {
        this.q_id = q_id;
        this.right = right;
        this.error = error;
        this.is_remove = is_remove;
        this.is_up = is_up;
    }

    @Generated(hash = 51353328)
    public TopicRecord() {
    }



    public String getQ_id() {
        return this.q_id;
    }

    public void setQ_id(String q_id) {
        this.q_id = q_id;
    }

    public String getRight() {
        return this.right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getIs_remove() {
        return this.is_remove;
    }

    public void setIs_remove(String is_remove) {
        this.is_remove = is_remove;
    }

    public String getIs_up() {
        return this.is_up;
    }

    public void setIs_up(String is_up) {
        this.is_up = is_up;
    }
}
