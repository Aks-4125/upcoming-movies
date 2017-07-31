package com.test.upcoming.di.component

import com.test.upcoming.BaseActivity
import com.test.upcoming.di.scope.ActivityScope

import dagger.Component

/**
 * Created by Aks4125 on 7/25/2017.
 */

@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class))
interface ActivityComponent : AppComponent {
    //  void inject(MainActivity mainActivity);
    override fun inject(baseActivity: BaseActivity)
}
