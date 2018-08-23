package com.mm.medicalman.Bean;

/**
 * /**
 * Created by NiPing and AirrWang on 2017/11/1.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 */

public class CollectBean {
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

    public CollectBeanData getData() {
        return data;
    }

    public void setData(CollectBeanData data) {
        this.data = data;
    }

    private CollectBeanData data;

    public class CollectBeanData {


        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        private String sql;
    }
}
