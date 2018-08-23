package com.mm.medicalman.Bean;

import com.mm.medicalman.DBBean.MockResults;

import java.util.List;

/**
 * /**
 * Created by NiPing and AirrWang on 2017/11/2.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 */

public class MockResultsBean {
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

    public MockResultsBeanData getData() {
        return data;
    }

    public void setData(MockResultsBeanData data) {
        this.data = data;
    }

    private MockResultsBeanData data;

    public class MockResultsBeanData {
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

        public List<MockResults> getList() {
            return list;
        }

        public void setList(List<MockResults> list) {
            this.list = list;
        }

        private String per_page;//num	每页数量
        private String curr_page;//	num	当前页目
        private String last_page;//	num	最后一页
        private List<MockResults> list;//arr	数据内容


    }

//    public class MockResultsBeanDataList {
//        public String getTimestamp() {
//            return timestamp;
//        }
//
//        public void setTimestamp(String timestamp) {
//            this.timestamp = timestamp;
//        }
//
//        public String getMinute() {
//            return minute;
//        }
//
//        public void setMinute(String minute) {
//            this.minute = minute;
//        }
//
//        public String getPercent() {
//            return percent;
//        }
//
//        public void setPercent(String percent) {
//            this.percent = percent;
//        }
//
//        public Long getRecord_id() {
//            return record_id;
//        }
//
//        public void setRecord_id(Long record_id) {
//            this.record_id = record_id;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public void setType(String type) {
//            this.type = type;
//        }
//
//        private Long record_id;
//        private String type;
//        private String timestamp;//str	考试时间 格式 YYYY-MM-DD HH:MM:SS 24小时制
//        private String minute;//num	用时 单位:分钟
//        private String percent;//	num	正确率(0-100整数)
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        private String name;//考试名称
//    }
}
