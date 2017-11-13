package com.anshdeep.dailytech.ui.main;

import com.androidnetworking.error.ANError;
import com.anshdeep.dailytech.data.DataManager;
import com.anshdeep.dailytech.data.model.Article;
import com.anshdeep.dailytech.network.model.NewsResponse;
import com.anshdeep.dailytech.ui.base.BasePresenter;
import com.anshdeep.dailytech.util.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by ANSHDEEP on 11-08-2017.
 */


public class MainPresenter extends BasePresenter<MainView> {


    private static final String TAG = "MainPresenter";

//    @Inject
//    NewsApiInterface newsApi;

//    @Inject
//    AppPreferencesHelper prefHelper;

//    @Inject
//    public MainPresenter(@ActivityContext Context context) {
//        super(context);
//        DailyTechApp.get(context).getComponent().inject(this);
//    }

    @Inject
    public MainPresenter(DataManager dataManager, SchedulerProvider schedulerProvider,
                         CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
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
        String source = getDataManager().getSourceName();
        getMvpView().showLoading();

        getCompositeDisposable().add(getDataManager()
                .getArticlesApiCall(source, apiKey)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<NewsResponse>() {
                    @Override
                    public void accept(NewsResponse response) throws Exception {

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().swipeToRefresh(false);
                        if (response.getArticles().size() > 0)
                            getMvpView().showArticles(response.getArticles());
                        else
                            getMvpView().showMessage("No news articles found");

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        // handle the api error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));

//        newsApi.getArticles(source, apiKey)
//                .enqueue(new Callback<NewsResponse>() {
//                    @Override
//                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
//                        getMvpView().hideLoading();
//                        getMvpView().swipeToRefresh(false);
//                        if (response.body().getArticles().size() > 0)
//                            getMvpView().showArticles(response.body().getArticles());
//                        else
//                            getMvpView().showMessage("No news articles found");
//                    }
//
//                    @Override
//                    public void onFailure(Call<NewsResponse> call, Throwable t) {
//                        Log.e(TAG, "onError: ", t);
//                        getMvpView().onError(null);
//                        getMvpView().hideLoading();
//                        getMvpView().swipeToRefresh(false);
//                    }
//                });


    }

    public void updateSharedPrefs(String subtitle, String sourceName) {
        getDataManager().setSubtitle(subtitle);
        getDataManager().setSourceName(sourceName);
    }

    public String getSubtitle() {
        return getDataManager().getSubtitle();
    }

    public void onMenuActionFavoriteClick() {
        getMvpView().openFavoriteArticlesActivity();
    }

    public void onArticleClick(Article article) {
        getMvpView().openArticleDetailActivity(article);
    }
}
