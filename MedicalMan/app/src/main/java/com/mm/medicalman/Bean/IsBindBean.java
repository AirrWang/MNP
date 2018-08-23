package com.mm.medicalman.Bean;

/**
 * Created by NiPing and Airr Wang on 2017/11/20.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class IsBindBean {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

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

    private String err_var;

    private IsBindDetailBean data;

    public IsBindDetailBean getData() {
        return data;
    }

    public void setData(IsBindDetailBean data) {
        this.data = data;
    }

    public class IsBindDetailBean {
        private int isbind;
        private String phone;

        public int getIsbind() {
            return isbind;
        }

        public void setIsbind(int isbind) {
            this.isbind = isbind;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
