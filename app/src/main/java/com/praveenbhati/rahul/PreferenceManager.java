package com.praveenbhati.rahul;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Bhati on 2/19/2016.
 */
public class PreferenceManager {
    private Context context;
    private String TAG = PreferenceManager.class.getSimpleName();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "Rahulapp";
    private static final String KEY_IS_LOGIN = "is_login";

    public PreferenceManager(Context context) {
        this.context = context;
        pref = this.context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = pref.edit();
    }


    public void setKeyIsLogin(){
        editor.putBoolean(KEY_IS_LOGIN,true);
        editor.apply();
    }

    public boolean getIsLogin(){
        return pref.getBoolean(KEY_IS_LOGIN,false);
    }


    public void clear() {
        editor.clear();
        editor.commit();
    }
}
