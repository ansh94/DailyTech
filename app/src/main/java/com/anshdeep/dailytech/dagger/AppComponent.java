package com.anshdeep.dailytech.dagger;

import com.anshdeep.dailytech.ui.ArticlesActivity.MainActivity;
import com.anshdeep.dailytech.ui.ArticlesActivity.MainPresenterImpl;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ANSHDEEP on 11-08-2017.
 */

// You’ve told Dagger that AppComponent is a singleton component interface for the app.
// The @Component annotation takes a list of modules as an input, and you’ve added AppModule to the list.
@Singleton
@Component(modules = {AppModule.class, PresenterModule.class, NetworkModule.class})
public interface AppComponent {

    void inject(MainActivity target);

    void inject(MainPresenterImpl targer);

}
