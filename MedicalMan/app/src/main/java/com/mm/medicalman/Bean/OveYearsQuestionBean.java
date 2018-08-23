package com.mm.medicalman.Bean;

import com.mm.medicalman.DBBean.Subject;

import java.util.List;

/**
 * /**
 * Created by NiPing and AirrWang on 2017/11/3.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 */

public class OveYearsQuestionBean {
    public void setData(OveYearsQuestionBeanData data) {
        this.data = data;
    }

    private OveYearsQuestionBeanData data;

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

    public OveYearsQuestionBeanData getData() {
        return data;
    }

    public class OveYearsQuestionBeanData {
        private String total;//num	数据总量

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getPer_page() {
            return per_page;
        }

        public void setPer_page(String per_page) {
            this.per_page = per_page;
        }

        public String getCurr_page() {
            return curr_page;
        }

        public void setCurr_page(String curr_page) {
            this.curr_page = curr_page;
        }

        public String getLast_page() {
            return last_page;
        }

        public void setLast_page(String last_page) {
            this.last_page = last_page;
        }

        public List<Subject> getList() {
            return list;
        }

        public void setList(List<Subject> list) {
            this.list = list;
        }

        private String per_page;//num	每页数量
        private String curr_page;//	num	当前页目
        private String last_page;//	num	最后一页
        private List<Subject> list;//arr	数据内容
    }

}
