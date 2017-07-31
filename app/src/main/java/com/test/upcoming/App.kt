package com.test.upcoming

import android.app.Application
import android.support.annotation.VisibleForTesting

import com.test.upcoming.di.component.AppComponent
import com.test.upcoming.di.component.DaggerAppComponent
import com.test.upcoming.di.module.ApplicationModule

/**
 * Created by Aks4125 on 7/25/2017.
 */

class App : Application() {

    @VisibleForTesting
    fun setmAppComponent(mAppComponent: AppComponent) {
        this.appComponent = mAppComponent
    }

    var appComponent: AppComponent? = null
        private set

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder().applicationModule(ApplicationModule(this))
                .build()
    }
}
