package com.anshdeep.dailytech.data.db;

import com.anshdeep.dailytech.data.model.Article;
import com.anshdeep.dailytech.data.model.ArticleDao;
import com.anshdeep.dailytech.data.model.DaoMaster;
import com.anshdeep.dailytech.data.model.DaoSession;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * Created by ANSHDEEP on 29-08-2017.
 */

@Singleton
public class AppDbHelper implements DbHelper {

    private final DaoSession mDaoSession;

    @Inject
    public AppDbHelper(DbOpenHelper dbOpenHelper) {
        mDaoSession = new DaoMaster(dbOpenHelper.getWritableDb()).newSession();
    }

    @Override
    public Observable<Long> insertArticle(final Article article) {
        return Observable.fromCallable(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return mDaoSession.getArticleDao().insert(article);

            }
        });
    }


    @Override
    public Observable<Void> removeArticle(final Article article) {
        return Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                mDaoSession.getArticleDao().delete(article);
                return null;

            }
        });
    }

    @Override
    public Observable<Boolean> isArticlePresent(final String articleUrl) {

        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                List<Article> articles = mDaoSession.getArticleDao().queryBuilder()
                        .where(ArticleDao.Properties.Url.eq(articleUrl))
                        .list();
                boolean isListEmpty = articles.isEmpty();
                return !isListEmpty;
            }
        });

    }

    @Override
    public Observable<Article> returnArticleByUrl(final String url) {
        return Observable.fromCallable(new Callable<Article>() {
            @Override
            public Article call() throws Exception {
                List<Article> articles = mDaoSession.getArticleDao().queryBuilder()
                        .where(ArticleDao.Properties.Url.eq(url))
                        .list();

                if (articles.isEmpty()) {
                    return null;
                } else {
                    return articles.get(0);
                }
            }
        });
    }
}
