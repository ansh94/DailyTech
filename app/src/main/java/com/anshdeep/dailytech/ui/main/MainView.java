package com.anshdeep.dailytech.ui.main;

import com.anshdeep.dailytech.data.model.Article;
import com.anshdeep.dailytech.ui.base.MvpView;

import java.util.List;

/**
 * Created by ANSHDEEP on 11-08-2017.
 */

interface MainView extends MvpView {

    void swipeToRefresh(boolean refreshingStatus);

    void showArticles(List<Article> articleList);

    void updateSubtitle(String subtitle);

    void openFavoriteArticlesActivity();

    void openArticleDetailActivity(Article article);

}
