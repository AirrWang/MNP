package com.mm.medicalman.Bean;

import java.util.List;

/**
 * Created by NiPing and Airr Wang on 2017/11/2.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class GetyearsBean {

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
    private GetyearsData data;

    public GetyearsData getData() {
        return data;
    }

    public void setData(GetyearsData data) {
        this.data = data;
    }

    public class GetyearsData {
        private List<Detail> list;

        public List<Detail> getList() {
            return list;
        }

        public void setList(List<Detail> list) {
            this.list = list;
        }

        public class Detail {
            public String getPaper_id() {
                return paper_id;
            }

            public void setPaper_id(String paper_id) {
                this.paper_id = paper_id;
            }

            private String paper_id;
            private String name;


            public String getMinute() {
                return minute;
            }

            public void setMinute(String minute) {
                this.minute = minute;
            }

            private String minute;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
