package com.anshdeep.dailytech.data;

import android.content.Context;

import com.anshdeep.dailytech.data.network.ApiHeader;
import com.anshdeep.dailytech.di.ApplicationContext;
import com.anshdeep.dailytech.data.db.DbHelper;
import com.anshdeep.dailytech.data.model.Article;
import com.anshdeep.dailytech.data.network.ApiHelper;
import com.anshdeep.dailytech.data.prefs.PreferencesHelper;
import com.anshdeep.dailytech.network.model.NewsResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * Created by ansh on 25/09/17.
 */

@Singleton
public class AppDataManager implements DataManager {

    private static final String TAG = "AppDataManager";

    private final Context mContext;
    private final DbHelper mDbHelper;
    private final PreferencesHelper mPreferencesHelper;
    private final ApiHelper mApiHelper;



    @Inject
    public AppDataManager(@ApplicationContext Context context,
                          DbHelper dbHelper,
                          PreferencesHelper preferencesHelper,
                          ApiHelper apiHelper) {
        mContext = context;
        mDbHelper = dbHelper;
        mPreferencesHelper = preferencesHelper;
        mApiHelper = apiHelper;
    }


    @Override
    public String getSubtitle() {
        return mPreferencesHelper.getSubtitle();
    }

    @Override
    public String getSourceName() {
        return mPreferencesHelper.getSourceName();
    }

    @Override
    public void setSubtitle(String subtitle) {
        mPreferencesHelper.setSubtitle(subtitle);
    }

    @Override
    public void setSourceName(String sourceName) {
        mPreferencesHelper.setSourceName(sourceName);
    }

    @Override
    public Observable<Long> insertArticle(Article article) {
        return mDbHelper.insertArticle(article);
    }

    @Override
    public Observable<Void> removeArticle(Article article) {
        return mDbHelper.removeArticle(article);
    }

    @Override
    public Observable<Boolean> isArticlePresent(String articleUrl) {
        return mDbHelper.isArticlePresent(articleUrl);
    }

    @Override
    public Observable<Article> returnArticleByUrl(String url) {
        return mDbHelper.returnArticleByUrl(url);
    }

    @Override
    public ApiHeader getApiHeader() {
        return mApiHelper.getApiHeader();
    }

    @Override
    public Observable<NewsResponse> getArticlesApiCall(String newsSource, String apiKey) {
        return mApiHelper.getArticlesApiCall(newsSource,apiKey);
    }
}
