package com.mm.medicalman.Tool.DBUtil;

import com.mm.medicalman.DBBean.TopicRecord;
import com.mm.medicalman.greendao.gen.TopicRecordDao;

import static com.mm.medicalman.Home.DoExerciseActivity.upIsTrueInterfaceDoEx;
import static com.mm.medicalman.Home.MineLoveExamActivity.upIsTrueInterfaceMineLove;

/**
 * /**
 * Created by NiPing and AirrWang on 2017/10/31.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 */

public class DBTools {
    public static void deleteDb() {
        DbCore.getDaoSession().getCourseNumDao().deleteAll();
        DbCore.getDaoSession().getLoveSubjectDao().deleteAll();
        DbCore.getDaoSession().getTopicRecordDao().deleteAll();
        DbCore.getDaoSession().getMockResultsDao().deleteAll();
        //所有的答题记录表清空
        DbCore.getDaoSession().getTitleRecordExaminationDao().deleteAll();
        DbCore.getDaoSession().getTitleRecordLoveDao().deleteAll();
        DbCore.getDaoSession().getTitleRecordPracticeCompetenceDao().deleteAll();
        DbCore.getDaoSession().getTitleRecordProfessionalPracticeDao().deleteAll();
    }

    /**
     * isRifhtOrErr：1时对加一，2时错加一
     * 此时插入对加一还是错加一
     *
     * @param isRifhtOrErr
     */
    public static void rightErrAllNum(final String q_id, final String isRifhtOrErr) {
        new android.os.Handler().post(new Runnable() {
            @Override
            public void run() {
                TopicRecordDao dao = DbCore.getDaoSession().getTopicRecordDao();
                TopicRecord topicRecord = dao.queryBuilder().where(
                        TopicRecordDao.Properties.Q_id.eq(q_id)).build().unique();
                if (topicRecord != null) {//1对，2错
                    if (isRifhtOrErr.equals("1")) {
                        int n = Integer.parseInt(topicRecord.getRight()) + 1;
                        topicRecord.setRight(n + "");
                        topicRecord.setError("0");
                    } else if (isRifhtOrErr.equals("2")) {
                        int n = Integer.parseInt(topicRecord.getError()) + 1;
                        topicRecord.setError(n + "");
                        topicRecord.setRight("0");
                    }
                } else {//1对，2错
                    if (isRifhtOrErr.equals("1")) {
                        topicRecord = new TopicRecord(q_id, "1", "0", "2", "2");
                    } else if (isRifhtOrErr.equals("2")) {
                        topicRecord = new TopicRecord(q_id, "0", "1", "2", "2");
                    }
                }
                dao.insertOrReplaceInTx(topicRecord);
                dao.detachAll();
                if (upIsTrueInterfaceMineLove != null) {
                    upIsTrueInterfaceMineLove.upIsTrue();
                }
                if (upIsTrueInterfaceDoEx != null) {
                    upIsTrueInterfaceDoEx.upIsTrue();
                }
            }
        });
    }

    /**
     * isRifhtOrErr：1时对加一，2时错加一
     * 此时插入对加一还是错加一
     * <p>
     * is_remove;//是否移除：1移除，2未移除
     */
    public static void rightErrAllNumRemove(String q_id) {
        TopicRecordDao dao = DbCore.getDaoSession().getTopicRecordDao();
        TopicRecord topicRecord = dao.queryBuilder().where(
                TopicRecordDao.Properties.Q_id.eq(q_id)).build().unique();
        if (topicRecord != null) {
            topicRecord.setIs_remove("1");
        }
        dao.insertOrReplaceInTx(topicRecord);
        dao.detachAll();
    }
}
