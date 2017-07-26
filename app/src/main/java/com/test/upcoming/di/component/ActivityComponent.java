package com.test.upcoming.di.component;

import com.test.upcoming.BaseActivity;
import com.test.upcoming.di.scope.ActivityScope;

import dagger.Component;

/**
 * Created by Aks4125 on 7/25/2017.
 */

@ActivityScope
@Component(dependencies = AppComponent.class)
public interface ActivityComponent extends AppComponent {
  //  void inject(MainActivity mainActivity);
    void inject(BaseActivity baseActivity);
}
