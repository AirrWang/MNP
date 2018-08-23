package com.mm.medicalman.Tool;

import android.content.Context;
import android.content.SharedPreferences;

import com.mm.medicalman.MmApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by NiPing and Airr Wang
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class SPUtils {

    public static final String FILE_NAME = "share_data";


    public static void put(Context context, String key, Object object) {
        SharedPreferences sp = MmApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
        return;
    }
    public static void saveArray(String key,List<String> list) {
        SharedPreferences sp = MmApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sp.edit();
        editor.putInt("Status_size",list.size());

        for(int i=0;i<list.size();i++) {
            editor.remove(key + i);
            editor.putString(key + i, list.get(i));
        }
        SharedPreferencesCompat.apply(editor);
    }
    public static List<String> loadArray(String key,List<String> list) {

        SharedPreferences mSharedPreference1 = MmApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        list.clear();
        int size = mSharedPreference1.getInt("Status_size", 0);
        for(int i=0;i<size;i++) {
            list.add(mSharedPreference1.getString(key + i, null));
        }
        return list;
    }

    public static Object get(Context context, String key, Object defaultObject) {
        try {
            SharedPreferences sp = MmApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

            if (defaultObject instanceof String) {
                return sp.getString(key, (String) defaultObject);
            } else if (defaultObject instanceof Integer) {
                return sp.getInt(key, (Integer) defaultObject);
            } else if (defaultObject instanceof Boolean) {
                return sp.getBoolean(key, (Boolean) defaultObject);
            } else if (defaultObject instanceof Float) {
                return sp.getFloat(key, (Float) defaultObject);
            } else if (defaultObject instanceof Long) {
                return sp.getLong(key, (Long) defaultObject);
            }
        } catch (Exception e) {
            if (key.equals("user_id")) {
                return 0;
            } else if (key.equals("token")) {
                return "";
            }
        }
        return null;
    }

    public static Object get(String key, Object defaultObject) {
        try {
            SharedPreferences sp = MmApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

            if (defaultObject instanceof String) {
                return sp.getString(key, (String) defaultObject);
            } else if (defaultObject instanceof Integer) {
                return sp.getInt(key, (Integer) defaultObject);
            } else if (defaultObject instanceof Boolean) {
                return sp.getBoolean(key, (Boolean) defaultObject);
            } else if (defaultObject instanceof Float) {
                return sp.getFloat(key, (Float) defaultObject);
            } else if (defaultObject instanceof Long) {
                return sp.getLong(key, (Long) defaultObject);
            }
        } catch (Exception e) {
            if (key.equals("user_id")) {
                return 0;
            } else if (key.equals("token")) {
                return "";
            }
        }
        return null;
    }


    public static void remove(Context context, String key) {
        SharedPreferences sp = MmApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }


    public static void clear(Context context) {
        SharedPreferences sp = MmApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }


    public static boolean contains(Context context, String key) {
        SharedPreferences sp = MmApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.contains(key);
    }


    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = MmApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getAll();
    }


    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }
            return null;
        }


        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }
}

