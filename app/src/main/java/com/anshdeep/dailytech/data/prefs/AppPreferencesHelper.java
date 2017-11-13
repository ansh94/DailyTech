package com.anshdeep.dailytech.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.anshdeep.dailytech.di.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by ANSHDEEP on 17-08-2017.
 */

@Singleton
public class AppPreferencesHelper implements PreferencesHelper {

    private static final String PREF_KEY_SUBTITLE = "PREF_KEY_SUBTITLE";
    private static final String PREF_KEY_URL_SOURCE_NAME = "PREF_KEY_URL_SOURCE_NAME";

    private final SharedPreferences mPrefs;

    @Inject
    public AppPreferencesHelper(@ApplicationContext Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public String getSubtitle() {
        return mPrefs.getString(PREF_KEY_SUBTITLE,null);
    }

    @Override
    public String getSourceName() {
        return mPrefs.getString(PREF_KEY_URL_SOURCE_NAME,null);
    }

    @Override
    public void setSubtitle(String subtitle) {
        mPrefs.edit().putString(PREF_KEY_SUBTITLE, subtitle).apply();
    }

    @Override
    public void setSourceName(String sourceName) {
        mPrefs.edit().putString(PREF_KEY_URL_SOURCE_NAME, sourceName).apply();
    }
}
