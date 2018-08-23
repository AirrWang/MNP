package com.mm.medicalman.Bean;

import java.util.List;

/**
 * Created by NiPing and Airr Wang on 2017/10/26.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class NoticeBean {
    private String status;
    private String message;
    private String err_var;
    private Dextail data;

    public Dextail getData() {
        return data;
    }

    public void setData(Dextail data) {
        this.data = data;
    }

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

    public static class Dextail {
       private List<DetailBean> list;

        public List<DetailBean> getList() {
            return list;
        }

        public void setList(List<DetailBean> list) {
            this.list = list;
        }

        public static class DetailBean {
            private String title;
            private String create_time;
            private String h5_url;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getH5_url() {
                return h5_url;
            }

            public void setH5_url(String h5_url) {
                this.h5_url = h5_url;
            }
        }
    }
}
