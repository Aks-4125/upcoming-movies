package com.test.upcoming

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.test.upcoming.di.component.ActivityComponent
import com.test.upcoming.di.component.DaggerActivityComponent
import com.test.upcoming.networkcall.NetworkCheck
import com.test.upcoming.networkcall.WebService
import com.google.gson.Gson

import javax.inject.Inject


/**
 * Created by Aks4125 on 7/25/2017.
 */

abstract class BaseActivity : AppCompatActivity() {

    protected var component: ActivityComponent? = null
        private set

    @Inject
    var mUtils: Utils? = null
    @Inject
    var webService: WebService? = null
    @Inject
    var networkCheck: NetworkCheck? = null

    @Inject
    var mGson: Gson? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component = DaggerActivityComponent.builder()
                .appComponent(app.appComponent).build()
        if (layoutResourceId != 0)
            setContentView(layoutResourceId)
    }

    protected val app: App
        get() = applicationContext as App

    protected abstract val layoutResourceId: Int
}
