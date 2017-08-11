package com.anshdeep.dailytech.dagger;

import android.content.Context;

import com.anshdeep.dailytech.ui.ArticlesActivity.MainPresenter;
import com.anshdeep.dailytech.ui.ArticlesActivity.MainPresenterImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ANSHDEEP on 11-08-2017.
 */

@Module
public class PresenterModule {

    @Provides
    @Singleton
    MainPresenter provideMainPresenter(Context context) {
        return new MainPresenterImpl(context);
    }
}
