package com.mm.medicalman.Bean;

/**
 * /**
 * Created by NiPing and AirrWang on 2017/10/30.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 */

public class DbUpBean {
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

    public BeanData getData() {
        return data;
    }

    public void setData(BeanData data) {
        this.data = data;
    }

    private BeanData data;
    /**
     * "quest_up":"1",
     * "quest_new":"39",
     * "class_up":"2",
     * "class_new":"39"
     */
    public class BeanData {
        public String getQuest_up() {
            return quest_up;
        }

        public void setQuest_up(String quest_up) {
            this.quest_up = quest_up;
        }

        public String getQuest_new() {
            return quest_new;
        }

        public void setQuest_new(String quest_new) {
            this.quest_new = quest_new;
        }

        public String getClass_up() {
            return class_up;
        }

        public void setClass_up(String class_up) {
            this.class_up = class_up;
        }

        public String getClass_new() {
            return class_new;
        }

        public void setClass_new(String class_new) {
            this.class_new = class_new;
        }

        String quest_up;
        String quest_new;
        String class_up;
        String class_new;
    }

}
