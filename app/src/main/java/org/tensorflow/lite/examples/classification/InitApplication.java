package org.tensorflow.lite.examples.classification;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class InitApplication extends Application {
//    public static final String NIGHT_MODE = "NIGHT_MODE";
//    private boolean isNightModeEnabled = false;

    private static InitApplication singleton = null;

    public static InitApplication getInstance() {
        if(singleton == null){
            singleton = new InitApplication();
        }
        return singleton;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        singleton = this;
//        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        this.isNightModeEnabled = mPrefs.getBoolean(NIGHT_MODE, false);
    }

//    public void setIsNightModeEnabled(boolean isNightModeEnabled){
//        this.isNightModeEnabled = isNightModeEnabled;
//
//        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor editor = mPrefs.edit();
//        editor.putBoolean(NIGHT_MODE, isNightModeEnabled);
//        editor.apply();
//    }
//
//    public boolean isNightModeEnabled() {
//        return isNightModeEnabled;
//    }
}
