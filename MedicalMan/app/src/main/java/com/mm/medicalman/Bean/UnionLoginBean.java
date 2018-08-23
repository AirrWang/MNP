package com.mm.medicalman.Bean;

/**
 * Created by NiPing and Airr Wang on 2017/10/30.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class UnionLoginBean {
    private String status;
    private String message;
    private String err_var;
    private UnionLoginData data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErr_var() {
        return err_var;
    }

    public void setErr_var(String err_var) {
        this.err_var = err_var;
    }

    public UnionLoginData getData() {
        return data;
    }

    public void setData(UnionLoginData data) {
        this.data = data;
    }

    public static class UnionLoginData {
        private String user_id;
        private String token;
        private String is_registered;

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

        public String getIs_registered() {
            return is_registered;
        }

        public void setIs_registered(String is_registered) {
            this.is_registered = is_registered;
        }
    }
}
