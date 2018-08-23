package com.mm.medicalman.Bean;

import java.util.List;

/**
 * /**
 * Created by NiPing and AirrWang on 2017/11/17.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 */

public class SchoollistBean {
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

    public SchoollistBeanData getData() {
        return data;
    }

    public void setData(SchoollistBeanData data) {
        this.data = data;
    }

    private SchoollistBeanData data;

    public class SchoollistBeanData {
        List<String> list;

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }
    }
}
