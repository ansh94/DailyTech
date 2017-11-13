package com.anshdeep.dailytech.data.network;

import com.anshdeep.dailytech.network.model.NewsResponse;

import io.reactivex.Observable;

/**
 * Created by ansh on 20/10/17.
 */

public interface ApiHelper {

    ApiHeader getApiHeader();

    Observable<NewsResponse> getArticlesApiCall(String newsSource,String apiKey);
}
