package com.anshdeep.dailytech;

import android.app.Application;

import com.anshdeep.dailytech.dagger.AppComponent;
import com.anshdeep.dailytech.dagger.AppModule;
import com.anshdeep.dailytech.dagger.DaggerAppComponent;

/**
 * Created by ANSHDEEP on 11-08-2017.
 */

public class DailyTechApp extends Application {

    private AppComponent appComponent;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    protected AppComponent initDagger(DailyTechApp application) {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(application))
                .build();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // This initializes the appComponent field when the application first starts up.
        appComponent = initDagger(this);
    }

}
