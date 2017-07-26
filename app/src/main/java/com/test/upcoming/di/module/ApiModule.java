package com.test.upcoming.di.module;

import android.content.Context;

import com.test.upcoming.BuildConfig;
import com.test.upcoming.R;
import com.test.upcoming.networkcall.WebService;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Aks4125 on 7/25/2017.
 */

@Module
public class ApiModule {
    @Provides
    @Singleton
    public WebService apiService(Context context) {
        String mBaseUrl = context.getString(BuildConfig.DEBUG ? R.string.local_url : R.string.live_url);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                //.addNetworkInterceptor(networkInterceptor)
                .build();

        return new Retrofit.Builder().baseUrl(mBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build().create(WebService.class);

    }

}

