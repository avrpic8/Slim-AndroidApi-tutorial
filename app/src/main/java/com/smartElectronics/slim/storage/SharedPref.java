package com.smartElectronics.slim.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.smartElectronics.slim.network.model.User;

public class SharedPref {

    private static final String SHARED_PREF_NAME = "user";
    private static SharedPref mInstance;

    private Context context;

    private SharedPref(Context mContext){
        this.context = mContext;
    }

    public static synchronized SharedPref getInstance(Context context){

        if(mInstance == null)
            mInstance = new SharedPref(context);

        return mInstance;
    }

    public void saveUser(User user){

        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("id", user.getId());
        editor.putString("email", user.getEmail());
        editor.putString("name", user.getName());
        editor.putString("school", user.getSchool());

        editor.commit();
    }

    public void storeApiKey(String apiKey){

        SharedPreferences preferences = context.getSharedPreferences("API_KEY", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("api_key", apiKey);
        editor.commit();
    }
    public String getApiKey(){

        SharedPreferences preferences = context.getSharedPreferences("API_KEY", Context.MODE_PRIVATE);
        return preferences.getString("api_key", null);
    }

    public boolean isLoggedIn(){

        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        if (preferences.getInt("id", -1) != -1)
            return true;
        else return false;
    }

    public User getUser(){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return new User(
                preferences.getInt("id",-1),
                preferences.getString("email", null),
                preferences.getString("name", null),
                preferences.getString("school", null)
        );
    }

    public void clear(){

        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
