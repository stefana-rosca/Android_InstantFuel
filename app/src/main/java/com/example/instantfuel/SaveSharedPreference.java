package com.example.instantfuel;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference
{
    static final String PREF_USER_NAME= "username";
    static final String PREF_USER_UID= "id";


    static android.content.SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName)
    {
        android.content.SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static void setUserUid(Context ctx, String userUID)
    {
        android.content.SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_UID, userUID);
        editor.commit();
    }

    public static String getUserUID(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_UID, "");
    }
}