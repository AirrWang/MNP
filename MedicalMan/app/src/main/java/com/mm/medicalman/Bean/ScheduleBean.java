package com.mm.medicalman.Bean;

/**
 * Created by NiPing and Airr Wang on 2017/10/27.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class ScheduleBean {
    private String status;
    private String message;
    private String err_var;
    private ScheduleData data;

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

    public ScheduleData getData() {
        return data;
    }

    public void setData(ScheduleData data) {
        this.data = data;
    }

    public static class ScheduleData {
        private String schedule;

        public String getSchedule() {
            return schedule;
        }

        public void setSchedule(String schedule) {
            this.schedule = schedule;
        }
    }
}
