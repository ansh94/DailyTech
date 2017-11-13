/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.anshdeep.dailytech.di.module;

import android.app.Application;
import android.content.Context;

import com.anshdeep.dailytech.BuildConfig;
import com.anshdeep.dailytech.data.AppDataManager;
import com.anshdeep.dailytech.data.DataManager;
import com.anshdeep.dailytech.data.db.AppDbHelper;
import com.anshdeep.dailytech.data.db.DbHelper;
import com.anshdeep.dailytech.data.network.ApiHelper;
import com.anshdeep.dailytech.data.network.AppApiHelper;
import com.anshdeep.dailytech.data.prefs.AppPreferencesHelper;
import com.anshdeep.dailytech.data.prefs.PreferencesHelper;
import com.anshdeep.dailytech.di.ApiInfo;
import com.anshdeep.dailytech.di.ApplicationContext;
import com.anshdeep.dailytech.di.DatabaseInfo;
import com.anshdeep.dailytech.util.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }


    @Provides
    @ApiInfo
    String provideApiKey() {
        return BuildConfig.NEWS_API_KEY;
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return Constants.DB_NAME;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }

    @Provides
    @Singleton
    DbHelper provideDbHelper(AppDbHelper appDbHelper) {
        return appDbHelper;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }

    @Provides
    @Singleton
    ApiHelper provideApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }



}
