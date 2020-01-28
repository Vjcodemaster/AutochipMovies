package app_utility;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesClass {
    SharedPreferences sharedPreferences;
    private Context _context;
    private static final String APP_PREFERENCES = "BEOC_PREFERENCES";
    private static final int PRIVATE_MODE = 0;

    private SharedPreferences.Editor editor;

    private static final String IS_LOGGED_IN = "IS_LOGGED_IN";
    private static final String USER_NAME = "USER_NAME";
    private static final String MOBILE_NUMBER = "MOBILE_NUMBER";
    private static final String EMAIL_ID = "EMAIL_ID";
    private static final String PASSWORD = "PASSWORD";
    private static final String CONTACT_ID = "CONTACT_ID";
    private static final String IS_ONE_ACCOUNT_PRESENT = "IS_ONE_ACCOUNT_PRESENT";

    public SharedPreferencesClass(Context context) {
        this._context = context;

        sharedPreferences = _context.getSharedPreferences(APP_PREFERENCES, PRIVATE_MODE);
        editor = sharedPreferences.edit();
        //editor.apply();
    }

    public void setUserLogStatus(boolean bValue){
        editor.putBoolean(IS_LOGGED_IN, bValue);
        editor.apply();
    }

    public boolean getUserLogStatus(){
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public void setTotalAccountStatus(boolean bValue){
        editor.putBoolean(IS_ONE_ACCOUNT_PRESENT, bValue);
        editor.apply();
    }

    public boolean getTotalAccountStatus(){
        return sharedPreferences.getBoolean(IS_ONE_ACCOUNT_PRESENT, false);
    }

    public void setUserInfo(String sName, String sEmail, String sMobile, String sPassword){
        editor.putString(USER_NAME, sName);
        editor.putString(EMAIL_ID, sEmail);
        editor.putString(MOBILE_NUMBER, sMobile);
        editor.putString(PASSWORD, sPassword);
        editor.apply();
    }

    public String getPassword(){
        return sharedPreferences.getString(PASSWORD, "");
    }


}
