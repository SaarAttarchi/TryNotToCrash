package com.example.TryNotToCrash;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferences {

    private static final String SP_FILE = "SP_FILE";



    public static void putString(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(key, defValue);
    }
}
