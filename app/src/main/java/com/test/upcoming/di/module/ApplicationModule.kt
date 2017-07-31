package com.test.upcoming.di.module

import android.content.Context

import com.test.upcoming.App
import com.test.upcoming.networkcall.NetworkCheck
import com.test.upcoming.Utils
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

/**
 * Created by Aks4125 on 7/25/2017.
 */


@Module
class ApplicationModule(private val mApp: App) {

    @Provides
    @Singleton
    fun appContext(): Context {
        return mApp
    }

    @Provides
    @Singleton
    fun utils(): Utils {
        return Utils(mApp)
    }

    @Provides
    @Singleton
    fun networkCheck(): NetworkCheck {
        return NetworkCheck(mApp)
    }


    val gson: Gson
        @Provides
        @Singleton
        get() {
            val sGsonBuilder = GsonBuilder()
            return sGsonBuilder.create()
        }

}
