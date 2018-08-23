package com.mm.medicalman.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class GetNewVersionBean {

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


    public GetNewVersionInfoBean getData() {
        return data;
    }

    public void setData(GetNewVersionInfoBean data) {
        this.data = data;
    }

    private GetNewVersionInfoBean data;

    public static class GetNewVersionInfoBean {
        private String v_num;//num	版本编号
        private String v_name;//str	版本名称

        public String getV_num() {
            return v_num;
        }

        public void setV_num(String v_num) {
            this.v_num = v_num;
        }

        public String getV_name() {
            return v_name;
        }

        public void setV_name(String v_name) {
            this.v_name = v_name;
        }

        public String getDown_url() {
            return down_url;
        }

        public void setDown_url(String down_url) {
            this.down_url = down_url;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getIs_must() {
            return is_must;
        }

        public void setIs_must(String is_must) {
            this.is_must = is_must;
        }

        private String down_url;//url	下载地址
        private String content;//str	更新内容
        private String is_must;//num	强制更新,2个值,是:1,否:2

        //"v_num":"10",
        // "v_name":"v1.00",

    }
}
