package com.filemanagementapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref  {

    SharedPreferences sharedPreferences;
    public SharedPref(Context context){
        sharedPreferences = context.getSharedPreferences("info", Context.MODE_PRIVATE);
    }

    public void edit(String create){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("create",create);
        editor.apply();
    }

    public String getCreate(){
        return sharedPreferences.getString("create","");
    }
}
