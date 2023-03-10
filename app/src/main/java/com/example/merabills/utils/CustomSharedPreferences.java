package com.example.merabills.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.merabills.R;

/**
 * Created by Jeetesh Surana.
 */
public class CustomSharedPreferences {
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    public CustomSharedPreferences(Activity activity) {
        pref = activity.getSharedPreferences(activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = pref.edit();
    }

   public void setBooleanValue(String key,Boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean getBooleanValue(String key) {
        return  pref.getBoolean(key, false);
    }
}
