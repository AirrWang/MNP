package com.mm.medicalman.Tool;

import com.mm.medicalman.MmApplication;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * /**
 * Created by NiPing and AirrWang on 2017/10/31.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 */

public class JpushTool {
    /**
     * 设置极光推送别名
     */
    public static void mySetAlias(String alias) {
        if (alias != null && !alias.equals("0") && MmApplication.getInstance() != null) {
            try {
                int aliasInt = 1;
                Set<String> set = new HashSet<>();
                set.add(RetrofitUtils.tags);
                if (alias == null || alias.equals("")) {
                    JPushInterface.deleteAlias(MmApplication.getInstance(), aliasInt);
                    JPushInterface.deleteTags(MmApplication.getInstance(), aliasInt, set);
                } else {
                    JPushInterface.setAlias(MmApplication.getInstance(), aliasInt, alias);
                    JPushInterface.setTags(MmApplication.getInstance(), aliasInt, set);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
