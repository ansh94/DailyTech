package com.anshdeep.dailytech;

import android.app.Application;
import android.content.Context;

import com.anshdeep.dailytech.dagger.component.ApplicationComponent;
import com.anshdeep.dailytech.dagger.component.DaggerApplicationComponent;
import com.anshdeep.dailytech.dagger.module.ApplicationModule;

/**
 * Created by ANSHDEEP on 11-08-2017.
 */

public class DailyTechApp extends Application {

    private ApplicationComponent mApplicationComponent;

    private static DailyTechApp instance;

    public static DailyTechApp get(Context context) {
        return (DailyTechApp) context.getApplicationContext();
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        mApplicationComponent = DaggerApplicationComponent.builder()
//                .applicationModule(new ApplicationModule(this)).build();

//        mApplicationComponent.inject(this);


    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }

}
