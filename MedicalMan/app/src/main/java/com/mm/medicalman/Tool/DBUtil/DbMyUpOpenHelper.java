package com.mm.medicalman.Tool.DBUtil;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mm.medicalman.LoginActivity;
import com.mm.medicalman.MmApplication;
import com.mm.medicalman.Tool.JpushTool;
import com.mm.medicalman.Tool.SPUtils;
import com.mm.medicalman.greendao.gen.DaoMaster;
import com.mm.medicalman.greendao.gen.SubjectDao;

import org.greenrobot.greendao.database.Database;

import static com.mm.medicalman.MainActivity.tokenErr;
import static com.mm.medicalman.Tool.DBUtil.DBTools.deleteDb;

/**
 * /**
 * Created by NiPing and AirrWang on 2017/11/15.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 */

public class DbMyUpOpenHelper extends DaoMaster.DevOpenHelper {

    public DbMyUpOpenHelper(Context context, String name) {
        super(context, name);
    }

    public DbMyUpOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db,oldVersion,newVersion);
        // 数据库版本升级后清除本地的同步关于账户表的本地记录
        SPUtils.remove(MmApplication.getInstance(),"isLoveNp");
        SPUtils.remove(MmApplication.getInstance(),"isLearnNp");
        SPUtils.remove(MmApplication.getInstance(),"mockResultsNp");
    }
}
