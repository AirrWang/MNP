package com.mm.medicalman.Bean;

import java.util.List;

/**
 * /**
 * Created by NiPing and AirrWang on 2017/10/24.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 */

public class InformationBean {
    public BeanRegisterData getData() {
        return data;
    }

    public void setData(BeanRegisterData data) {
        this.data = data;
    }

    private BeanRegisterData data;


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
    public class BeanRegisterData {
        public List<BeanRegisterDataList> getList() {
            return list;
        }

        public void setList(List<BeanRegisterDataList> list) {
            this.list = list;
        }

        List<BeanRegisterDataList> list;

        public class BeanRegisterDataList {
            String title;//str	标题
            String cover;//	str	封面

            public String getApp_num() {
                return app_num;
            }

            public void setApp_num(String app_num) {
                this.app_num = app_num;
            }

            String app_num;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public String getLength() {
                return length;
            }

            public void setLength(String length) {
                this.length = length;
            }

            public String getH5_url() {
                return h5_url;
            }

            public void setH5_url(String h5_url) {
                this.h5_url = h5_url;
            }

            String author;//	str	发布人
            String type;//str	类型（1：文章;2：课程）
            String label;//	str	标签（无则为空字符串）
            String length;//	num	课时（type=1时为0）
            String h5_url;//	str	h5地址
            private String article_id;

            public String getArticle_id() {
                return article_id;
            }

            public void setArticle_id(String article_id) {
                this.article_id = article_id;
            }
        }
    }


}
