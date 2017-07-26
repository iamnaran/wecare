package com.naran.wecare.Welcome;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by NaRan on 5/10/17.
 */

public class PrefManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_FIRST_TOKEN = "IsFirstToken";

    public PrefManager(Context context) {
        this.context = context;
        pref = this.context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = pref.edit();

    }
    public void setIsFirstTimeLaunch(boolean isFirstTimeLaunch){
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTimeLaunch);
        editor.commit();
    }
    public boolean isFirstTimeLaunch(){
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH,true);
    }

    public boolean isFirstToken(){

        return pref.getBoolean(IS_FIRST_TOKEN,true);
    }

}
