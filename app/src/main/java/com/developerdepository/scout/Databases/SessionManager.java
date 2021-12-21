package com.developerdepository.scout.Databases;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    //Variables
    SharedPreferences usersSession;
    SharedPreferences.Editor editor;
    Context context;

    //Session Names
    public static final String SESSION_USERSESSION = "userLoginSession";
    public static final String SESSION_REMEMBERME = "rememberMe";

    //User Session Variables
    private static final String IS_LOGGED_IN = "IsLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_DATEOFBIRTH = "dateOfBirth";
    public static final String KEY_MOBILE = "mobile";

    //Remember Me Session Variables
    private static final String IS_REMEMBER_ME = "IsRememberMe";
    public static final String KEY_SESSIONMOBILE = "mobile";
    public static final String KEY_SESSIONPASSWORD = "password";

    //Constructor
    public SessionManager(Context _context, String sessionName) {
        context = _context;
        usersSession = context.getSharedPreferences(sessionName, Context.MODE_PRIVATE);
        editor = usersSession.edit();
    }


    //Users Login Session Functions

    public void createLoginSession(String name, String username, String email, String password, String gender, String dateOfBirth, String mobile) {
        editor.putBoolean(IS_LOGGED_IN, true);

        editor.putString(KEY_NAME, name);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_GENDER, gender);
        editor.putString(KEY_DATEOFBIRTH, dateOfBirth);
        editor.putString(KEY_MOBILE, mobile);

        editor.commit();
    }

    public HashMap<String, String> getUsersDetailFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put(KEY_NAME, usersSession.getString(KEY_NAME, null));
        userData.put(KEY_USERNAME, usersSession.getString(KEY_USERNAME, null));
        userData.put(KEY_EMAIL, usersSession.getString(KEY_EMAIL, null));
        userData.put(KEY_PASSWORD, usersSession.getString(KEY_PASSWORD, null));
        userData.put(KEY_GENDER, usersSession.getString(KEY_GENDER, null));
        userData.put(KEY_DATEOFBIRTH, usersSession.getString(KEY_DATEOFBIRTH, null));
        userData.put(KEY_MOBILE, usersSession.getString(KEY_MOBILE, null));

        return userData;
    }

    public boolean checkIfUserLoggedIn() {
        if(usersSession.getBoolean(IS_LOGGED_IN, false)) {
            return true;
        } else {
            return false;
        }
    }

    public void logoutUserFromSession() {
        editor.clear();
        editor.commit();
    }


    //Remember Me Session Functions

    public void createRememberMeSession(String mobile, String password) {
        editor.putBoolean(IS_REMEMBER_ME, true);

        editor.putString(KEY_SESSIONMOBILE, mobile);
        editor.putString(KEY_SESSIONPASSWORD, password);

        editor.commit();
    }

    public HashMap<String, String> getRememberMeDetailsFromSession() {
        HashMap<String, String> rememberMeData = new HashMap<String, String>();

        rememberMeData.put(KEY_SESSIONMOBILE, usersSession.getString(KEY_SESSIONMOBILE, null));
        rememberMeData.put(KEY_SESSIONPASSWORD, usersSession.getString(KEY_SESSIONPASSWORD, null));

        return rememberMeData;
    }

    public boolean checkRememberMe() {
        if(usersSession.getBoolean(IS_REMEMBER_ME, false)) {
            return true;
        } else {
            return false;
        }
    }
}
