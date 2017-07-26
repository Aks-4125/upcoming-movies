package com.test.upcoming.di.module;

import android.content.Context;

import com.test.upcoming.App;
import com.test.upcoming.networkcall.NetworkCheck;
import com.test.upcoming.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aks4125 on 7/25/2017.
 */


@Module
public class ApplicationModule {
    private App mApp;

    public ApplicationModule(App app) {
        mApp = app;
    }

    @Provides
    @Singleton
    public Context appContext() {
        return mApp;
    }

    @Provides
    @Singleton
    public Utils utils() {
        return new Utils(mApp);
    }

    @Provides
    @Singleton
    public NetworkCheck networkCheck() {
        return new NetworkCheck(mApp);
    }


    @Provides
    @Singleton
    public Gson getGson() {
        GsonBuilder sGsonBuilder = new GsonBuilder();
        return sGsonBuilder.create();
    }

}
