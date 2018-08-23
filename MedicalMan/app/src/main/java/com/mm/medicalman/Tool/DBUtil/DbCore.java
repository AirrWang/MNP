
package com.mm.medicalman.Tool.DBUtil;

import android.content.Context;


import com.mm.medicalman.greendao.gen.DaoMaster;
import com.mm.medicalman.greendao.gen.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;
/**
 * Created by NiPing and Airr Wang on 2017/10/23.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */

public class DbCore {
    private static final String DEFAULT_DB_NAME = "mm.db";
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    private static Context mContext;
    private static String DB_NAME;

    public static void init(Context context) {
        init(context, DEFAULT_DB_NAME);
    }

    public static void init(Context context, String dbName) {
        if (context == null) {
            throw new IllegalArgumentException("context can't be null");
        }
        mContext = context.getApplicationContext();
        DB_NAME = dbName;
    }

    public static DaoMaster getDaoMaster() {
        if (daoMaster == null) {
            //此处不可用 DaoMaster.DevOpenHelper, 那是开发辅助类，我们要自定义一个，方便升级
            DaoMaster.OpenHelper helper = new DbMyUpOpenHelper(mContext, DB_NAME);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    public static DaoSession getDaoSession() {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    public static void enableQueryBuilderLog() {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

//    UserDao dao= DbCore.getDaoSession().getUserDao();

//    name = et_Name.getText().toString().trim();
//    age = et_age.getText().toString();
//    sex = et_sex.getText().toString().trim();
//    salary = et_Salary.getText().toString().trim();
//    User insertData = new User(null, name, age, sex, salary);
//    dao.insert(insertData);

//    dao.insert();
//    dao.deleteByKey();
//    dao.update();
//    List<User> userList = dao.queryBuilder()
//            .where(UserDao.Properties.Id.eq(999))
//            .orderAsc(UserDao.Properties.Id)
//            .limit(5)
//            .build().list();
}
