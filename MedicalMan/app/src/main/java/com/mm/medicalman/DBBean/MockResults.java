package com.mm.medicalman.DBBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * /**
 * Created by NiPing and AirrWang on 2017/11/1.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 * 与账户有关：模拟考试历史表
 * <p>
 * record_id	num	主键
 * type	num	考试类型,目前2个值,模拟考试:1,历史试卷考试:2
 * timestamp	str	考试时间 格式 YYYY.MM.DD
 * minute	num	用时 单位:分钟
 * percent	num	正确率(0-100整数)
 */
@Entity
public class MockResults {
    @Id(autoincrement = true)
    private Long record_id;//num	主键
    private String type;//考试类型,目前2个值,模拟考试:1,历史试卷考试:2
    private String timestamp;//	str	post	是	考试时间 格式 YYYY-MM-DD HH:MM:SS 24小时制
    private String minute;//	num	post	是	用时 单位:分钟
    private Long percent;//num	post	是	正确率（0-100）
    private String name;//考试名称
    private String is_up="2";//1是2否


    @Generated(hash = 1255495705)
    public MockResults(Long record_id, String type, String timestamp, String minute,
            Long percent, String name, String is_up) {
        this.record_id = record_id;
        this.type = type;
        this.timestamp = timestamp;
        this.minute = minute;
        this.percent = percent;
        this.name = name;
        this.is_up = is_up;
    }

    @Generated(hash = 1322168706)
    public MockResults() {
    }









    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMinute() {
        return this.minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }


    
    public String getType() {
        return this.type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public String getName() {
        return this.name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public Long getRecord_id() {
        return this.record_id;
    }

    public void setRecord_id(Long record_id) {
        this.record_id = record_id;
    }

    public String getIs_up() {
        return this.is_up;
    }

    public void setIs_up(String is_up) {
        this.is_up = is_up;
    }

    public long getPercent() {
        return this.percent;
    }

    public void setPercent(long percent) {
        this.percent = percent;
    }

    public void setPercent(Long percent) {
        this.percent = percent;
    }

}
