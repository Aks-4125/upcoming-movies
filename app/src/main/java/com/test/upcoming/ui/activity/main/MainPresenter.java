package com.test.upcoming.ui.activity.main;

import android.graphics.Movie;

import com.test.upcoming.model.Movies;
import com.test.upcoming.networkcall.WebService;
import com.google.gson.JsonObject;


import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Aks4125 on 7/11/2017.
 */

public class MainPresenter implements MainContractor.IMainPresenter {

    private MainContractor.IMainView mainView;
    private WebService webService;
    private Subscription mSubscription;

    @Inject
    public MainPresenter(WebService webService) {
        this.webService = webService;
    }

    public void setMainView(MainContractor.IMainView mainView) {
        this.mainView = mainView;
    }


    @Override
    public void subscribe() {

    }


    @Override
    public void getMovieList(String key) {
        mainView.showProgress();
        //mainView.showMessage("loading" );


        mSubscription = webService.getMovieList(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Movies>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mainView.showMessage("Something went wrong");
                        mainView.stopProgress();
                    }

                    @Override
                    public void onNext(Movies jsonObject) {
                        mainView.stopProgress();
                        mainView.processJson(jsonObject);
                    }
                });


    }

    @Override
    public void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();

        mainView.stopProgress();

    }


}
