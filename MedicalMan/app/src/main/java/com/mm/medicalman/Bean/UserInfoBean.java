package com.mm.medicalman.Bean;

/**
 * /**
 * Created by NiPing and AirrWang on 2017/10/25.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 */

public class UserInfoBean {
    public BeanData getData() {
        return data;
    }

    public void setData(BeanData data) {
        this.data = data;
    }

    private BeanData data;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status;
    public String getErr_var() {
        return err_var;
    }

    public void setErr_var(String err_var) {
        this.err_var = err_var;
    }

    String err_var;
    public class BeanData {
        String phone;//str	手机号码

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getSlogan() {
            return slogan;
        }

        public void setSlogan(String slogan) {
            this.slogan = slogan;
        }

        String nickname;//	str	昵称
        String sex;//str	性别,2个值:1男2女3未定义
        String pic;//url	头像地址
        String slogan;//str	个人签名
    }
}
