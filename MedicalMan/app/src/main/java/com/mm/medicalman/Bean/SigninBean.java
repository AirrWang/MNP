package com.mm.medicalman.Bean;

import java.util.List;

/**
 * Created by NiPing and Airr Wang on 2017/11/6.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class SigninBean {

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

    private SiginInfo data;

    public SiginInfo getData() {
        return data;
    }

    public void setData(SiginInfo data) {
        this.data = data;
    }

    public static class SiginInfo {
        private List<DetailInfo> list;
        private String date;
        private int total;
        private int type;
        private int complete;

        public List<DetailInfo> getList() {
            return list;
        }

        public void setList(List<DetailInfo> list) {
            this.list = list;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getComplete() {
            return complete;
        }

        public void setComplete(int complete) {
            this.complete = complete;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public static class DetailInfo {
            private int att_id;
            private String start_time;
            private String end_time;
            private String in_time;
            private String out_time;
            private String in_status;
            private String out_status;
            private String in_adress;
            private String out_adress;
            private String lat;
            private String lon;
            private int distance;
            private int status;

            public int getAtt_id() {
                return att_id;
            }

            public void setAtt_id(int att_id) {
                this.att_id = att_id;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public String getIn_time() {
                return in_time;
            }

            public void setIn_time(String in_time) {
                this.in_time = in_time;
            }

            public String getOut_time() {
                return out_time;
            }

            public void setOut_time(String out_time) {
                this.out_time = out_time;
            }

            public String getIn_status() {
                return in_status;
            }

            public void setIn_status(String in_status) {
                this.in_status = in_status;
            }

            public String getOut_status() {
                return out_status;
            }

            public void setOut_status(String out_status) {
                this.out_status = out_status;
            }

            public String getIn_adress() {
                return in_adress;
            }

            public void setIn_adress(String in_adress) {
                this.in_adress = in_adress;
            }

            public String getOut_adress() {
                return out_adress;
            }

            public void setOut_adress(String out_adress) {
                this.out_adress = out_adress;
            }

            public int getDistance() {
                return distance;
            }

            public void setDistance(int distance) {
                this.distance = distance;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public String getLon() {
                return lon;
            }

            public void setLon(String lon) {
                this.lon = lon;
            }
        }
    }
}
