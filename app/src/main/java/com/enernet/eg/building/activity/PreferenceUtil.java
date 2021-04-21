package com.enernet.eg.building.activity;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil{

    public static void setPreferences(Context context, String key, int value) {
        SharedPreferences pref = context.getSharedPreferences("pref", context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getPreferences(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        pref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        return pref.getInt(key, 0);
    }
}


