package com.mm.medicalman.Bean;

/**
 * /**
 * Created by NiPing and AirrWang on 2017/10/24.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 */

public class BeanRegister {
    public BeanRegisterData getData() {
        return data;
    }

    public void setData(BeanRegisterData data) {
        this.data = data;
    }

    private BeanRegisterData data;


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

    public class BeanRegisterData {
        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        String user_id;
        String token;
    }
}
