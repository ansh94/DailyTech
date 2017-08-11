package com.anshdeep.dailytech.ui.ArticlesActivity;

import com.anshdeep.dailytech.api.model.Article;

import java.util.List;

/**
 * Created by ANSHDEEP on 11-08-2017.
 */

interface MainView {

    void showLoading();

    void hideLoading();

    void swipeToRefresh(boolean refreshingStatus);

    void showArticles(List<Article> articleList);

    void showErrorMessage(String error);

}
