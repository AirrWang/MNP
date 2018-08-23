package com.mm.medicalman.Bean;

import com.mm.medicalman.DBBean.Course;

import java.util.List;

/**
 * /**
 * Created by NiPing and AirrWang on 2017/10/31.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 */

public class ClassBean {
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

    public ClassBeanData getData() {
        return data;
    }

    public void setData(ClassBeanData data) {
        this.data = data;
    }

    private ClassBeanData data;
    public class ClassBeanData{
        public List<Course> getList() {
            return list;
        }

        public void setList(List<Course> list) {
            this.list = list;
        }

        private List<Course>list;

    }


}
