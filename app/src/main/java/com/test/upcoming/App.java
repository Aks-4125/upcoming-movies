package com.test.upcoming;

import android.app.Application;
import android.support.annotation.VisibleForTesting;

import com.test.upcoming.di.component.AppComponent;
import com.test.upcoming.di.component.DaggerAppComponent;
import com.test.upcoming.di.module.ApplicationModule;

/**
 * Created by Aks4125 on 7/25/2017.
 */

public class App extends Application {
    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    @VisibleForTesting
    public void setmAppComponent(AppComponent mAppComponent) {
        this.mAppComponent = mAppComponent;
    }

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder().applicationModule(new ApplicationModule(this))
                .build();
    }
}
