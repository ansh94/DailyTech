package com.anshdeep.dailytech.ui.main;

import android.content.Context;
import android.util.Log;

import com.anshdeep.dailytech.DailyTechApp;
import com.anshdeep.dailytech.dagger.ActivityContext;
import com.anshdeep.dailytech.data.model.Article;
import com.anshdeep.dailytech.data.prefs.AppPreferencesHelper;
import com.anshdeep.dailytech.network.NewsApiInterface;
import com.anshdeep.dailytech.network.model.NewsResponse;
import com.anshdeep.dailytech.ui.base.BasePresenter;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ANSHDEEP on 11-08-2017.
 */

public class MainPresenter extends BasePresenter<MainView> {


    private static final String TAG = "MainPresenter";

    @Inject
    NewsApiInterface newsApi;

    @Inject
    AppPreferencesHelper prefHelper;

    @Inject
    public MainPresenter(@ActivityContext Context context) {
        super(context);
        DailyTechApp.get(context).getComponent().inject(this);
    }

    @Override
    public void onAttach(MainView mvpView) {
        super.onAttach(mvpView);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void getArticles(String apiKey) {
        String source = prefHelper.getSourceName();
        newsApi.getArticles(source, apiKey)
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        getMvpView().hideLoading();
                        getMvpView().swipeToRefresh(false);
                        if (response.body().getArticles().size() > 0)
                            getMvpView().showArticles(response.body().getArticles());
                        else
                            getMvpView().showMessage("No news articles found");
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                        Log.e(TAG, "onError: ", t);
                        getMvpView().onError(null);
                        getMvpView().hideLoading();
                        getMvpView().swipeToRefresh(false);
                    }
                });
    }

    public void updateSharedPrefs(String subtitle, String sourceName) {
        prefHelper.setSubtitle(subtitle);
        prefHelper.setSourceName(sourceName);
    }

    public String getSubtitle() {
        return prefHelper.getSubtitle();
    }

    public void onMenuActionFavoriteClick() {
        getMvpView().openFavoriteArticlesActivity();
    }

    public void onArticleClick(Article article) {
        getMvpView().openArticleDetailActivity(article);
    }
}
