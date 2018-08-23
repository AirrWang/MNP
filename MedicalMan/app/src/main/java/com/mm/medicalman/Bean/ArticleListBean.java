package com.mm.medicalman.Bean;

import java.util.List;

/**
 * Created by NiPing and Airr Wang on 2017/11/7.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class ArticleListBean {
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

    private ArticleListDetail data;

    public ArticleListDetail getData() {
        return data;
    }

    public void setData(ArticleListDetail data) {
        this.data = data;
    }

    public class ArticleListDetail {

        private List<DetailBeanArtList> list;

        public List<DetailBeanArtList> getList() {
            return list;
        }

        public void setList(List<DetailBeanArtList> list) {
            this.list = list;
        }


        public class DetailBeanArtList {
            private String title;
            private String brief;
            private String picture;
            private String timestamp;
            private String url;
            private int type;
            private int is_read;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getBrief() {
                return brief;
            }

            public void setBrief(String brief) {
                this.brief = brief;
            }

            public String getPicture() {
                return picture;
            }

            public void setPicture(String picture) {
                this.picture = picture;
            }

            public String getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(String timestamp) {
                this.timestamp = timestamp;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getIs_read() {
                return is_read;
            }

            public void setIs_read(int is_read) {
                this.is_read = is_read;
            }
        }
    }
}
