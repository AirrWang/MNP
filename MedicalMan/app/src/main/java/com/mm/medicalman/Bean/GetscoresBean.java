package com.mm.medicalman.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by NiPing and Airr Wang on 2017/10/31.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class GetscoresBean implements Serializable{
    private String status;
    private String message;
    private LoginInfoBean data;
    private String err_var;

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

    public LoginInfoBean getData() {
        return data;
    }

    public void setData(LoginInfoBean data) {
        this.data = data;
    }

    public String getErr_var() {
        return err_var;
    }

    public void setErr_var(String err_var) {
        this.err_var = err_var;
    }

    public  class LoginInfoBean implements Serializable{
        private String name;
        private String student_id;
        private String classname;
        private String counselor;
        private String cotel;
        private String director;
        private String dirtel;
        private String grade;
        private String title;
        private List<ScoresBean> scores;
        private String letter_url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStudent_id() {
            return student_id;
        }

        public void setStudent_id(String student_id) {
            this.student_id = student_id;
        }

        public String getClassname() {
            return classname;
        }

        public void setClassname(String classname) {
            this.classname = classname;
        }

        public String getCounselor() {
            return counselor;
        }

        public void setCounselor(String counselor) {
            this.counselor = counselor;
        }

        public String getCotel() {
            return cotel;
        }

        public void setCotel(String cotel) {
            this.cotel = cotel;
        }

        public String getDirector() {
            return director;
        }

        public void setDirector(String director) {
            this.director = director;
        }

        public String getDirtel() {
            return dirtel;
        }

        public void setDirtel(String dirtel) {
            this.dirtel = dirtel;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<ScoresBean> getScores() {
            return scores;
        }

        public void setScores(List<ScoresBean> scores) {
            this.scores = scores;
        }

        public String getLetter_url() {
            return letter_url;
        }

        public void setLetter_url(String letter_url) {
            this.letter_url = letter_url;
        }

        public  class ScoresBean implements Serializable{
            private String subject;
            private String grade;


            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public String getGrade() {
                return grade;
            }

            public void setGrade(String grade) {
                this.grade = grade;
            }
        }
    }
}
