package com.anshdeep.dailytech.network;

import com.anshdeep.dailytech.api.model.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ANSHDEEP on 18-05-2017.
 */

public interface NewsApiInterface {

    // https://newsapi.org/v1/articles?source=time&apiKey=<key>
    @GET("articles")
    Call<NewsResponse> getArticles(@Query("source") String newsSource, @Query("apiKey") String apiKey);
}
