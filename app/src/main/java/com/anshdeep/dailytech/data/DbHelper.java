package com.anshdeep.dailytech.data;

import com.anshdeep.dailytech.data.model.Article;

/**
 * Created by ANSHDEEP on 29-08-2017.
 */

interface DbHelper {

    void insertArticle(final Article article);

    void removeArticle(Article article);

    boolean isArticlePresent(String articleUrl);

    Article returnArticleByUrl(String url);

}
