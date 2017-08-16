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

package com.anshdeep.dailytech.dagger.component;

import android.app.Application;
import android.content.Context;

import com.anshdeep.dailytech.dagger.ApplicationContext;
import com.anshdeep.dailytech.dagger.module.ApplicationModule;
import com.anshdeep.dailytech.dagger.module.NetworkModule;
import com.anshdeep.dailytech.ui.main.MainPresenter;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {

    @ApplicationContext
    Context context();

    // inject network module in Main Presenter
    void inject(MainPresenter target);


    Application application();


}