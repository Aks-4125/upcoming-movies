package com.test.upcoming;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.test.upcoming.di.component.ActivityComponent;
import com.test.upcoming.di.component.DaggerActivityComponent;
import com.test.upcoming.networkcall.NetworkCheck;
import com.test.upcoming.networkcall.WebService;
import com.google.gson.Gson;

import javax.inject.Inject;


/**
 * Created by Aks4125 on 7/25/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private ActivityComponent mComponent;

    @Inject
    protected Utils mUtils;
    @Inject
    protected WebService webService;
    @Inject
    protected NetworkCheck networkCheck;

    @Inject
    protected  Gson mGson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComponent = DaggerActivityComponent.builder()
                .appComponent(getApp().getAppComponent()).build();
        if (getLayoutResourceId() != 0)
            setContentView(getLayoutResourceId());
    }

    protected App getApp() {
        return (App) getApplicationContext();
    }

    protected ActivityComponent getComponent() {
        return mComponent;
    }

    protected abstract int getLayoutResourceId();
}
