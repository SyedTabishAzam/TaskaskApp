package com.example.tabish.taskask;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Taskask";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    private static final String IS_NEW = "IsNewUser";

    public static final String IS_SPRINT = "IsInSprint";

    public static final String IS_CUSTOMER = "IsCustomer";

    public static final String IS_RUNNER = "IsRunner";

    // User name (make variable public to access from outside)
    public static final String KEY_FULLNAME = "fullname";

    // User name (make variable public to access from outside)
    public static final String KEY_TASKID = "taskID";

    // User name (make variable public to access from outside)
    public static final String KEY_OTHERUSER = "otherUserId";

    // Email address (make variable public to access from outside)
    public static final String KEY_USERNAME = "username";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }

    /**
     * Create login session
     * */
    public void createLoginSession(String name, String email){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_FULLNAME, name);

        // Storing email in pref
        editor.putString(KEY_USERNAME, email);

        // commit changes
        editor.commit();
    }

    public void setNewUser(){
        // Storing login value as TRUE
        editor.putBoolean(IS_NEW, false);

        editor.commit();
    }

    public void createSprintSessionCustomer(String taskID, String userName){

        // Storing login value as TRUE
        editor.putBoolean(IS_SPRINT, true);

        // Storing name in pref
        editor.putString(KEY_TASKID, taskID);

        // Storing email in pref
        editor.putString(KEY_OTHERUSER, userName);
        editor.putBoolean(IS_CUSTOMER,true);
        editor.putBoolean(IS_RUNNER,false);
        // commit changes
        editor.commit();
        // After logout redirect user to Loing Activity



    }

    public void createSprintSessionRunner(){

        // Storing login value as TRUE
        editor.putBoolean(IS_SPRINT, true);
        editor.putBoolean(IS_CUSTOMER,false);
        editor.putBoolean(IS_RUNNER,true);
        // commit changes
        editor.commit();
        // After logout redirect user to Loing Activity


    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status

        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity

            Intent i = new Intent(_context, MainScreenActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);

        }


    }

    public void checkSprint()
    {
        // Check login status
        if(this.isInSprint()){
            // user is is sprint - redirect him to Sprint Activity
            if(isCustomerOrRunner().equals(IS_CUSTOMER))
            {


                Intent i = new Intent(_context, Sprint.class);
                // Closing all the Activities
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                // Staring Sprint Activity
                _context.startActivity(i);
            }
            else
            {
                Intent i = new Intent(_context, OnlySprint.class);
                // Closing all the Activities
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                // Staring Sprint Activity
                _context.startActivity(i);
            }
        }

    }





    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_FULLNAME, pref.getString(KEY_FULLNAME, null));

        // user email id
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));

        // return user
        return user;
    }

    public HashMap<String, String> getSprintDetail(){
        HashMap<String, String> sprint = new HashMap<String, String>();
        // On going task id
        sprint.put(KEY_TASKID, pref.getString(KEY_TASKID, null));

        // Accepted User Id
        sprint.put(KEY_OTHERUSER, pref.getString(KEY_OTHERUSER, null));

        // return user
        return sprint;
    }

    public boolean getNewUser()
    {
        boolean isNew= pref.getBoolean(IS_NEW,true);
        return isNew;

    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, MainScreenActivity.class);
        // Closing all the Activities
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }



    public void endSprint(){

        // Storing login value as TRUE
        editor.putBoolean(IS_SPRINT, false);
        editor.putBoolean(IS_RUNNER, false);
        editor.putBoolean(IS_CUSTOMER, false);
        // Storing name in pref
        editor.putString(KEY_TASKID, "");

        // Storing email in pref
        editor.putString(KEY_OTHERUSER,"");

        // commit changes
        editor.commit();
        // After logout redirect user to Loing Activity



    }

    public void ClearAll()
    {
        editor.clear();
        editor.commit();
        logoutUser();
        endSprint();
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public String isCustomerOrRunner() {if(true== pref.getBoolean(IS_CUSTOMER,false)) return IS_CUSTOMER; return IS_RUNNER;}
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
    public boolean isInSprint(){
        return pref.getBoolean(IS_SPRINT, false);
    }
}