package com.mm.medicalman.Bean;

import java.util.List;

/**
 * Created by NiPing and Airr Wang on 2017/10/26.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class PickdataListBean {
    private String status;
    private String message;
    private String err_var;
    private ListBrean data;

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

    public ListBrean getData() {
        return data;
    }

    public void setData(ListBrean data) {
        this.data = data;
    }

    public static class ListBrean {
       private List<ListDetail> list;

        public List<ListDetail> getList() {
            return list;
        }

        public void setList(List<ListDetail> list) {
            this.list = list;
        }

        public static class ListDetail {
            private String name;
            private int id;
            private List<FacultyDetail> faculty;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public List<FacultyDetail> getFaculty() {
                return faculty;
            }

            public void setFaculty(List<FacultyDetail> faculty) {
                this.faculty = faculty;
            }

            public static class FacultyDetail {
                private String name;
                private int id;
                private List<GrageDetail> grade;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public List<GrageDetail> getGrade() {
                    return grade;
                }

                public void setGrade(List<GrageDetail> grade) {
                    this.grade = grade;
                }

                public static class GrageDetail {
                    private String name;
                    private int id;
                    private List<ClasDetail> clas;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public List<ClasDetail> getClas() {
                        return clas;
                    }

                    public void setClas(List<ClasDetail> clas) {
                        this.clas = clas;
                    }

                    public static class ClasDetail {
                        private String name;
                        private int id;

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
                        }

                        public int getId() {
                            return id;
                        }

                        public void setId(int id) {
                            this.id = id;
                        }
                    }
                }
            }
        }
    }


}
