package com.mm.medicalman.Bean;

/**
 * /**
 * Created by NiPing and AirrWang on 2017/10/25.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 */

public class UpPicSingleTool {
    public BeanData getData() {
        return data;
    }

    public void setData(BeanData data) {
        this.data = data;
    }

    private BeanData data;


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
    public class BeanData {
        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        String path;//一半图片地址，暂时无用

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        String url;//全称图片地址
    }
}
