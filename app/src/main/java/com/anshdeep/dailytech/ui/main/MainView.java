package com.anshdeep.dailytech.ui.main;

import com.anshdeep.dailytech.api.model.Article;
import com.anshdeep.dailytech.ui.base.MvpView;

import java.util.List;

/**
 * Created by ANSHDEEP on 11-08-2017.
 */

public interface MainView extends MvpView {

    void swipeToRefresh(boolean refreshingStatus);

    void showArticles(List<Article> articleList);

}
