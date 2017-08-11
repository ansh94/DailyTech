package com.anshdeep.dailytech.ui.ArticlesActivity;

/**
 * Created by ANSHDEEP on 11-08-2017.
 */

public interface MainPresenter {

    void setView(MainView view);

    void getArticles(String source, String apiKey);
}
