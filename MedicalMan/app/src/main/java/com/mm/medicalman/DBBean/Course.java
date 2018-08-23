package com.mm.medicalman.DBBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * /**
 * Created by NiPing and AirrWang on 2017/10/31.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 * 公共题库章节
 */
@Entity
public class Course {
    @Id
    private String c_id;//	num	章节id
    private long c_num;//	num	序号
    private String serial;//	str	第几章
    private String name;//	str	章节名称
    private String deleted;//	num	是否已删除,是:1, 否:2 ; 若已删除 请删除本地数据库此条数据

    @Generated(hash = 2010618106)
    public Course(String c_id, long c_num, String serial, String name,
            String deleted) {
        this.c_id = c_id;
        this.c_num = c_num;
        this.serial = serial;
        this.name = name;
        this.deleted = deleted;
    }
    @Generated(hash = 1355838961)
    public Course() {
    }

    public String getC_id() {
        return this.c_id;
    }
    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getSerial() {
        return this.serial;
    }
    public void setSerial(String serial) {
        this.serial = serial;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDeleted() {
        return this.deleted;
    }
    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
    public long getC_num() {
        return this.c_num;
    }
    public void setC_num(long c_num) {
        this.c_num = c_num;
    }
}
