package com.anshdeep.dailytech.dagger.module;

import com.anshdeep.dailytech.network.NewsApiInterface;
import com.anshdeep.dailytech.util.Constants;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ANSHDEEP on 11-08-2017.
 */

@Module
public class NetworkModule {

    private static final String NAME_BASE_URL = "NAME_BASE_URL";

    // You are injecting a String object, and since String is such a common type to use in an Android app,
    // you’ve taken advantage of the @Named annotation to specify a specific string to be provided.
    // This same technique can be used for your own types if you need multiple variations injected.
    @Provides
    @Named(NAME_BASE_URL)
    String provideBaseUrlString() {
        return Constants.BASE_URL;
    }

    @Provides
    @Singleton
    Converter.Factory provideGsonConverter() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Converter.Factory converter, @Named(NAME_BASE_URL) String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converter)
                .build();
    }


    /*
    This tells Dagger to construct a dependency graph, so that when an object asks for a UsdaApi object to be injected,
    Dagger will first provide a Retrofit object to use in provideUsdaApi(Retrofit retrofit).
    Then, Dagger will continue walking up the graph to find a converter and baseUrl for
    provideRetrofit(Converter.Factory converter, @Named(NAME_BASE_URL) String baseUrl).
     */
    @Provides
    @Singleton
    NewsApiInterface provideNewsApi(Retrofit retrofit) {
        return retrofit.create(NewsApiInterface.class);
    }

}
