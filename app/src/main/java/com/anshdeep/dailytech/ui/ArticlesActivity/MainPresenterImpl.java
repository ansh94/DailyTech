package com.anshdeep.dailytech.ui.ArticlesActivity;

import android.content.Context;
import android.util.Log;

import com.anshdeep.dailytech.DailyTechApp;
import com.anshdeep.dailytech.api.model.NewsResponse;
import com.anshdeep.dailytech.network.NewsApiInterface;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by ANSHDEEP on 11-08-2017.
 */

public class MainPresenterImpl implements MainPresenter {

    private MainView view;

    @Inject
    NewsApiInterface newsApi;


    public MainPresenterImpl(Context context) {
        ((DailyTechApp) context).getAppComponent().inject(this);
    }

    @Override
    public void setView(MainView view) {
        this.view = view;
    }

    @Override
    public void getArticles(String source, String apiKey) {
        newsApi.getArticles(source, apiKey)
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        view.hideLoading();
                        view.swipeToRefresh(false);
                        if (response.body().getArticles().size() > 0)
                            view.showArticles(response.body().getArticles());
                        else
                            view.showErrorMessage("No news articles found");
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                        Log.e(TAG, "onError: ", t);
                        view.showErrorMessage("An error occurred\nPlease try again");
                        view.hideLoading();
                        view.swipeToRefresh(false);
                    }
                });
    }
}
