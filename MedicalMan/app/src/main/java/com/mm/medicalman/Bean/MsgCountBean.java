package com.mm.medicalman.Bean;

/**
 * Created by NiPing and Airr Wang on 2017/11/8.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class MsgCountBean {
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

    private MsgCountDetail data;

    public MsgCountDetail getData() {
        return data;
    }

    public void setData(MsgCountDetail data) {
        this.data = data;
    }

    public class MsgCountDetail {
        private int count;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
