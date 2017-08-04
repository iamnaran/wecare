package com.naran.wecare.Models;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "mysharedpref12";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_EMAIL = "useremail";
    private static final String KEY_USER_ID = "userid";

    private static final String SHARED_PREF_NAME_ORG = "myorgSharedPref";
    private static final String KEY_ORG_USERNAME = "orgUsername";
    private static final String KEY_ORG_EMAIL = "orgEmail";
    private static final String KEY_ORG_ID = "orgID";


    private SharedPrefManager(Context context) {
        mCtx = context;

    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }


    public boolean userLogin(int id, String username, String email) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_ID, id);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USERNAME, username);

        editor.apply();

        return true;
    }



    public boolean userOrganizationLogin(int id, String username, String email) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME_ORG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ORG_ID, id);
        editor.putString(KEY_ORG_EMAIL, email);
        editor.putString(KEY_ORG_USERNAME, username);
        editor.apply();
        return true;
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_USERNAME, null) != null) {
            return true;
        }
        return false;
    }
    public boolean isOrgLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME_ORG, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_ORG_USERNAME, null) != null) {
            return true;
        }

        return false;
    }

    public boolean logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }
    public boolean orgLogout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME_ORG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }


    public String getUsername() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    public String getUserEmail() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }

    public String getOrgUsername(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME_ORG, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ORG_USERNAME, null);

    }
    public String getOrgEmail(){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME_ORG, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ORG_EMAIL, null);

    }




}