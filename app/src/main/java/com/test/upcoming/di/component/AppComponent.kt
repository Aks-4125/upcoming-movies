package com.test.upcoming.di.component

import com.test.upcoming.BaseActivity
import com.test.upcoming.di.module.ApiModule
import com.test.upcoming.di.module.ApplicationModule
import com.test.upcoming.networkcall.NetworkCheck
import com.test.upcoming.Utils
import com.test.upcoming.networkcall.WebService
import com.test.upcoming.ui.activity.main.MainActivity
import com.google.gson.Gson

import javax.inject.Singleton

import dagger.Component

/**
 * Created by Aks4125 on 7/25/2017.
 */

@Singleton
@Component(modules = arrayOf(ApplicationModule::class, ApiModule::class))
interface AppComponent {

    fun apiService(): WebService

    fun networkCheck(): NetworkCheck

    fun utils(): Utils

    val gson: Gson

    fun inject(baseActivity: BaseActivity)

    fun inject(mainActivity: MainActivity)

}
