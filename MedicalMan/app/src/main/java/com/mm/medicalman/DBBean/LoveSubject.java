package com.mm.medicalman.DBBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by NiPing and AirrWang on 2017/11/1.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 * 与账户有关，记录的账户收藏的题
 */
@Entity
public class LoveSubject {
    @Id
    private String q_id;//	num	题目id,唯一主键
    private String is_love;//1是2否
    private String is_love_up="2";//1是2否


    @Generated(hash = 712152451)
    public LoveSubject(String q_id, String is_love, String is_love_up) {
        this.q_id = q_id;
        this.is_love = is_love;
        this.is_love_up = is_love_up;
    }

    @Generated(hash = 736663161)
    public LoveSubject() {
    }


    public String getQ_id() {
        return this.q_id;
    }

    public void setQ_id(String q_id) {
        this.q_id = q_id;
    }

    public String getIs_love() {
        return this.is_love;
    }

    public void setIs_love(String is_love) {
        this.is_love = is_love;
    }

    public String getIs_love_up() {
        return this.is_love_up;
    }

    public void setIs_love_up(String is_love_up) {
        this.is_love_up = is_love_up;
    }
   
}
