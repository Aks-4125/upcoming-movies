package com.test.upcoming.di.component;

import com.test.upcoming.BaseActivity;
import com.test.upcoming.di.module.ApiModule;
import com.test.upcoming.di.module.ApplicationModule;
import com.test.upcoming.networkcall.NetworkCheck;
import com.test.upcoming.Utils;
import com.test.upcoming.networkcall.WebService;
import com.test.upcoming.ui.activity.main.MainActivity;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Aks4125 on 7/25/2017.
 */

@Singleton
@Component(modules = {ApplicationModule.class, ApiModule.class})
public interface AppComponent {

    WebService apiService();

    NetworkCheck networkCheck();

    Utils utils();

    Gson getGson();

    void inject(BaseActivity baseActivity);

    void inject(MainActivity mainActivity);

}
