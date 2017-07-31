package com.test.upcoming.di.module

import android.content.Context

import com.test.upcoming.BuildConfig
import com.test.upcoming.R
import com.test.upcoming.networkcall.WebService

import java.util.concurrent.TimeUnit

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Aks4125 on 7/25/2017.
 */

@Module
class ApiModule {
    @Provides
    @Singleton
    fun apiService(context: Context): WebService {
        val mBaseUrl = context.getString(if (BuildConfig.DEBUG) R.string.local_url else R.string.live_url)

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        val okHttpClient = OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                //.addNetworkInterceptor(networkInterceptor)
                .build()

        return Retrofit.Builder().baseUrl(mBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build().create(WebService::class.java)

    }

}

