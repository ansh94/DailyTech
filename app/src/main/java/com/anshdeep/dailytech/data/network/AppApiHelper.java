package com.anshdeep.dailytech.data.network;

import com.anshdeep.dailytech.network.model.NewsResponse;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * Created by ansh on 20/10/17.
 */

@Singleton
public class AppApiHelper implements ApiHelper {

    private ApiHeader mApiHeader;

    @Inject
    public AppApiHelper(ApiHeader apiHeader) {
        mApiHeader = apiHeader;
    }

    @Override
    public ApiHeader getApiHeader() {
        return mApiHeader;
    }


    @Override
    public Observable<NewsResponse> getArticlesApiCall(String newsSource, String apiKey) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_ARTICLES)
                .addQueryParameter("source", newsSource)
                .addQueryParameter("apiKey", apiKey)
                .build()
                .getObjectObservable(NewsResponse.class);
    }
}
