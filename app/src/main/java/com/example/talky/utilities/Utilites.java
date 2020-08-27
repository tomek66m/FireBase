package com.example.talky.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.talky.DatabaseClasses.User;
import com.example.talky.RegisterActivity;

public class Utilites {

    public static void SaveUserToSharedPreferences(User pUser, SharedPreferences pRegisterActivitySharedPref)
    {
        SharedPreferences.Editor editor = pRegisterActivitySharedPref.edit();
        editor.putString("User_ID", pUser._ID);
        editor.putString("User_Email", pUser._Email);
        editor.putString("User_Name", pUser._Name);
        editor.putString("User_Surname", pUser._Surname);
        editor.commit();
    }

    public static String GetCurrentUserID(Activity currentActivity)
    {
        Context context = currentActivity;
        SharedPreferences pref = context.getSharedPreferences("User_ID", 0);
         return pref.getString("User_ID", null);
    }
}
