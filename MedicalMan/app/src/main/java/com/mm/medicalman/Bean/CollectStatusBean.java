package com.mm.medicalman.Bean;

/**
 * Created by NiPing and Airr Wang on 2017/11/17.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class CollectStatusBean {


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

    private CollectStatusDetail data;

    public CollectStatusDetail getData() {
        return data;
    }

    public void setData(CollectStatusDetail data) {
        this.data = data;
    }

    public class CollectStatusDetail {

        private int status;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
