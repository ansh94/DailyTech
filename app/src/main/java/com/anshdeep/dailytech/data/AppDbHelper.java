package com.anshdeep.dailytech.data;

import com.anshdeep.dailytech.data.model.Article;
import com.anshdeep.dailytech.data.model.ArticleDao;
import com.anshdeep.dailytech.data.model.DaoMaster;
import com.anshdeep.dailytech.data.model.DaoSession;

import java.util.List;

/**
 * Created by ANSHDEEP on 29-08-2017.
 */

public class AppDbHelper implements DbHelper {

    private final DaoSession mDaoSession;

    public AppDbHelper(DbOpenHelper dbOpenHelper) {
        mDaoSession = new DaoMaster(dbOpenHelper.getWritableDb()).newSession();
    }

    @Override
    public void insertArticle(Article article) {
        mDaoSession.getArticleDao().insert(article);
    }

    @Override
    public void removeArticle(Article article) {
        mDaoSession.getArticleDao().delete(article);
    }

    @Override
    public boolean isArticlePresent(String articleUrl) {
        List<Article> articles = mDaoSession.getArticleDao().queryBuilder()
                .where(ArticleDao.Properties.Url.eq(articleUrl))
                .list();

        boolean isListEmpty = articles.isEmpty();
        if (isListEmpty) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Article returnArticleByUrl(String url) {
        List<Article> articles = mDaoSession.getArticleDao().queryBuilder()
                .where(ArticleDao.Properties.Url.eq(url))
                .list();

        if (articles.isEmpty()) {
            return null;
        } else {
            return articles.get(0);
        }
    }
}
