package com.mm.medicalman.DBBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * /**
 * Created by NiPing and AirrWang on 2017/10/30.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 * 公共题库所有题
 */
@Entity
public class Subject {
    @Id
    private String q_id;//	num	题目id,唯一主键
    private String c_id;//	num	章节(与章节表关联)
    private long q_num;//	num	题目序号
    private String question;//	str	题目描述
    private String answer;//	str	答案,目前5个值,A:1,B:2,C:3,D:4,E:5
    private String analysis;//	str	答案解析
    private String option1;//	str	答案1
    private String option2;//	str	答案2
    private String option3;//	str	答案3
    private String option4;//	str	答案4
    private String option5;//	str	答案5
    private String deleted;//	num	是否已删除,是:1, 否:2 ; 若已删除 请删除本地数据库此条数据
    private String ability;//	num	题目方向分类,2个值,专业实务:1,实践能力:2
    private String difficulty;//	num	题目难度,容易:1,中等:2,困难:3
    private String point;//str 考点
    @Generated(hash = 36858658)
    public Subject(String q_id, String c_id, long q_num, String question,
            String answer, String analysis, String option1, String option2,
            String option3, String option4, String option5, String deleted,
            String ability, String difficulty, String point) {
        this.q_id = q_id;
        this.c_id = c_id;
        this.q_num = q_num;
        this.question = question;
        this.answer = answer;
        this.analysis = analysis;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.option5 = option5;
        this.deleted = deleted;
        this.ability = ability;
        this.difficulty = difficulty;
        this.point = point;
    }
    @Generated(hash = 1617906264)
    public Subject() {
    }
    public String getQ_id() {
        return this.q_id;
    }
    public void setQ_id(String q_id) {
        this.q_id = q_id;
    }
    public String getC_id() {
        return this.c_id;
    }
    public void setC_id(String c_id) {
        this.c_id = c_id;
    }
    public long getQ_num() {
        return this.q_num;
    }
    public void setQ_num(long q_num) {
        this.q_num = q_num;
    }
    public String getQuestion() {
        return this.question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public String getAnswer() {
        return this.answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public String getAnalysis() {
        return this.analysis;
    }
    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }
    public String getOption1() {
        return this.option1;
    }
    public void setOption1(String option1) {
        this.option1 = option1;
    }
    public String getOption2() {
        return this.option2;
    }
    public void setOption2(String option2) {
        this.option2 = option2;
    }
    public String getOption3() {
        return this.option3;
    }
    public void setOption3(String option3) {
        this.option3 = option3;
    }
    public String getOption4() {
        return this.option4;
    }
    public void setOption4(String option4) {
        this.option4 = option4;
    }
    public String getOption5() {
        return this.option5;
    }
    public void setOption5(String option5) {
        this.option5 = option5;
    }
    public String getDeleted() {
        return this.deleted;
    }
    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
    public String getAbility() {
        return this.ability;
    }
    public void setAbility(String ability) {
        this.ability = ability;
    }
    public String getDifficulty() {
        return this.difficulty;
    }
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    public String getPoint() {
        return this.point;
    }
    public void setPoint(String point) {
        this.point = point;
    }
}
