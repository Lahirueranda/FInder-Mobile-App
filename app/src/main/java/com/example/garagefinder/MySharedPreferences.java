package com.example.garagefinder;

//imports libraries
import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {

    //declaring and initialized the variables
    private static final String PREF_NAME = "MyPref";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private SharedPreferences pref;
    private static final String KEY_NAME = "name";
    private static final String KEY_TYPE = "type";
    private SharedPreferences.Editor editor;
    private Context context;

    public MySharedPreferences(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        // Initialize SharedPreferences.Editor object
        editor = pref.edit();
    }

    public void setLoggedIn(boolean isLoggedIn) {
        // Store the login status in shared preferences
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    public boolean isLoggedIn() {
        // Retrieve the login status from shared preferences
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

    public void setUser(String user) {
        editor.putString(KEY_NAME, user);
        //apply the changes to shared preferences
        editor.apply();
    }

    public String getUser() {
        // Store the user name in shared preferences
        String name = pref.getString(KEY_NAME, "");
        return name;
    }

    public void setUserType(String userType) {
        //store the user type in shared preferences
        editor.putString(KEY_TYPE, userType);
        //apply the changes to shared preferences
        editor.apply();
    }

    public String getUserType() {
        //retrieve the user type from shared preferences,
        String userType = pref.getString(KEY_TYPE, "");
        return userType;
    }

    public void clear() {
        //clear all data in shared preferences
        editor.clear();
        //apply the changes to shared preferences
        editor.apply();
    }

}
