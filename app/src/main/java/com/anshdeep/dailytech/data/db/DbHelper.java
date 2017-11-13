package com.anshdeep.dailytech.data.db;

import com.anshdeep.dailytech.data.model.Article;

import io.reactivex.Observable;

/**
 * Created by ANSHDEEP on 29-08-2017.
 */

public interface DbHelper {

    Observable<Long> insertArticle(final Article article);

    Observable<Void> removeArticle(Article article);

    Observable<Boolean> isArticlePresent(String articleUrl);

    Observable<Article> returnArticleByUrl(String url);


}
