package com.anshdeep.dailytech;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interceptors.HttpLoggingInterceptor.Level;
import com.anshdeep.dailytech.di.component.ApplicationComponent;
import com.anshdeep.dailytech.di.component.DaggerApplicationComponent;
import com.anshdeep.dailytech.di.module.ApplicationModule;
import com.anshdeep.dailytech.data.DataManager;

import javax.inject.Inject;

/**
 * Created by ANSHDEEP on 11-08-2017.
 */

public class DailyTechApp extends Application {

    @Inject
    DataManager mDataManager;

    private ApplicationComponent mApplicationComponent;


    @Override
    public void onCreate() {
        super.onCreate();
//        instance = this;

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();

        mApplicationComponent.inject(this);

        AndroidNetworking.initialize(getApplicationContext());
        if (BuildConfig.DEBUG) {
            AndroidNetworking.enableLogging(Level.BODY);
        }


    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }

}
