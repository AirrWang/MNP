package com.mm.medicalman.Bean;

/**
 * /**
 * Created by NiPing and AirrWang on 2017/11/1.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 */

public class MockRuleBean {
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

    public MockRuleBeanData getData() {
        return data;
    }

    public void setData(MockRuleBeanData data) {
        this.data = data;
    }

    private MockRuleBeanData data;

    public class MockRuleBeanData {
        public String getMinute() {
            return minute;
        }

        public void setMinute(String minute) {
            this.minute = minute;
        }

        public String getQ_num() {
            return q_num;
        }

        public void setQ_num(String q_num) {
            this.q_num = q_num;
        }

        public Qdifficulty getQ_difficulty() {
            return q_difficulty;
        }

        public void setQ_difficulty(Qdifficulty q_difficulty) {
            this.q_difficulty = q_difficulty;
        }

        public Qclass getQ_class() {
            return q_class;
        }

        public void setQ_class(Qclass q_class) {
            this.q_class = q_class;
        }

        public String getRule_score() {
            return rule_score;
        }

        public void setRule_score(String rule_score) {
            this.rule_score = rule_score;
        }

        public String getRule_question() {
            return rule_question;
        }

        public void setRule_question(String rule_question) {
            this.rule_question = rule_question;
        }

        String minute;//num	考试时长,单位分钟
        String q_num;//str	总题数
        Qdifficulty q_difficulty;//	arr	各难度题目数量分配规则
        Qclass q_class;//arr	各章节题目数量分配规则
        String rule_score;//str	出题规则
        String rule_question;//str	计分规则
    }

    public class Qdifficulty {
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        /**
         * type	str	难度类别 , 用英文逗号分隔 , 与num一一对应
         * num	num	题目数量 , 用英文逗号分隔 , 与type一一对应
         */
        String type;//
        String num;//
    }

    public class Qclass {
        public String getC_id() {
            return c_id;
        }

        public void setC_id(String c_id) {
            this.c_id = c_id;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        /**
         * 参数	类型	说明
         * c_id	str	章节c_id , 用英文逗号分隔 , 与num一一对应
         * num	num	题目数量 , 用英文逗号分隔 , 与c_id一一对应
         */
        String c_id;//
        String num;//
    }
}
