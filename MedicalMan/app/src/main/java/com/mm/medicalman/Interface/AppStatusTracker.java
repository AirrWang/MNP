package com.mm.medicalman.Interface;

import android.app.Activity;
import android.os.Bundle;

import com.mm.medicalman.MmApplication;

/**
 * /**
 * Created by NiPing and AirrWang on 2017/11/22.
 * Copyright © 2017/11/22 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 * 判断应用是否处于前台活跃状态
 */

public class AppStatusTracker implements MmApplication.ActivityLifecycleCallbacks {

    private static final long MAX_INTERVAL = 5 * 60 * 1000;
    private static AppStatusTracker tracker;
    private MmApplication application;
    private boolean isForground;
    private int activeCount;
    private long timestamp;


    private AppStatusTracker(MmApplication application) {
        this.application = application;
        application.registerActivityLifecycleCallbacks(this);
    }

    public static void init(MmApplication application) {
        tracker = new AppStatusTracker(application);
    }

    public static AppStatusTracker getInstance() {
        return tracker;
    }


    public boolean isForground() {
        return isForground;
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

        if (activeCount == 0) {
            timestamp = System.currentTimeMillis();
        }
        activeCount++;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        isForground = true;
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

        activeCount--;
        if (activeCount == 0) {
            isForground = false;
            timestamp = System.currentTimeMillis() - timestamp;
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
