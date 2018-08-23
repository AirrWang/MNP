package com.mm.medicalman.Bean;

import java.util.List;

/**
 * Created by NiPing and Airr Wang on 2017/11/21.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class MySchoolBean {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

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

    private String err_var;

    private MySchoolDetailBean data;

    public MySchoolDetailBean getData() {
        return data;
    }

    public void setData(MySchoolDetailBean data) {
        this.data = data;
    }

    public class MySchoolDetailBean {
        private String schedule;
        private ScoreslistBean scoreslist;
        private List<NoticelistBean> noticelist;

        public String getSchedule() {
            return schedule;
        }

        public void setSchedule(String schedule) {
            this.schedule = schedule;
        }

        public ScoreslistBean getScoreslist() {
            return scoreslist;
        }

        public void setScoreslist(ScoreslistBean scoreslist) {
            this.scoreslist = scoreslist;
        }

        public List<NoticelistBean> getNoticelist() {
            return noticelist;
        }

        public void setNoticelist(List<NoticelistBean> noticelist) {
            this.noticelist = noticelist;
        }

        public class ScoreslistBean {
            private String name;
            private String student_id;
            private String classname;
            private String title;
            private List<ScoresDetBean> scores;

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

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public List<ScoresDetBean> getScores() {
                return scores;
            }

            public void setScores(List<ScoresDetBean> scores) {
                this.scores = scores;
            }

            public class ScoresDetBean {
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

        public class NoticelistBean {
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
