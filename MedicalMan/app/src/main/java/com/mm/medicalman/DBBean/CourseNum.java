package com.mm.medicalman.DBBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * /**
 * Created by NiPing and AirrWang on 2017/10/31.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 * 与账户有关，章节练习时的正在做的题号记录
 */
@Entity
public class CourseNum {

    private String c_id;//	num	章节id
    private String ability;//	num	题目方向分类,2个值,专业实务:1,实践能力:2
    private String q_id;//num 题目id
    private String c_vp_num;//num 题号(vp:position)
    @Id
    private String type;//4个值,专业实务:1,实践能力:2，收藏：3，错题分析：4
    public CourseNum() {
    }

    @Generated(hash = 1586332182)
    public CourseNum(String c_id, String ability, String q_id, String c_vp_num,
            String type) {
        this.c_id = c_id;
        this.ability = ability;
        this.q_id = q_id;
        this.c_vp_num = c_vp_num;
        this.type = type;
    }

    public String getC_id() {
        return this.c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getAbility() {
        return this.ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public String getQ_id() {
        return this.q_id;
    }

    public void setQ_id(String q_id) {
        this.q_id = q_id;
    }

    public String getC_vp_num() {
        return this.c_vp_num;
    }

    public void setC_vp_num(String c_vp_num) {
        this.c_vp_num = c_vp_num;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
