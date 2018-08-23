package com.mm.medicalman.DBBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * /**
 * Created by NiPing and AirrWang on 2017/10/30.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 * 公共：记录当前的本地的下载的服务器数据库版本
 */
@Entity
public class DBcurrv {
    @Id
    private String num;
    private String quest_v;
    private String class_v;
    @Generated(hash = 876135001)
    public DBcurrv(String num, String quest_v, String class_v) {
        this.num = num;
        this.quest_v = quest_v;
        this.class_v = class_v;
    }
    @Generated(hash = 827815521)
    public DBcurrv() {
    }
    public String getNum() {
        return this.num;
    }
    public void setNum(String num) {
        this.num = num;
    }
    public String getQuest_v() {
        return this.quest_v;
    }
    public void setQuest_v(String quest_v) {
        this.quest_v = quest_v;
    }
    public String getClass_v() {
        return this.class_v;
    }
    public void setClass_v(String class_v) {
        this.class_v = class_v;
    }
}
