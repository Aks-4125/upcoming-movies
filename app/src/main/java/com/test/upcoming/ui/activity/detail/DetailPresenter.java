package com.test.upcoming.ui.activity.detail;

import com.test.upcoming.model.MovieImages;
import com.test.upcoming.networkcall.WebService;
import com.google.gson.JsonObject;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Aks4125 on 7/26/2017.
 */

public class DetailPresenter implements DetailContractor.IDetailPresenter {

    private DetailContractor.IDetailView detailView;
    private WebService webService;
    private Subscription mSubscription;

    @Inject
    public DetailPresenter(WebService webService) {
        this.webService = webService;
    }

    public void setDetailView(DetailContractor.IDetailView detailView) {
        this.detailView = detailView;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void getMovieImages(String apiKey, String movieID) {
        detailView.showProgress();

        mSubscription = webService.getMovieImages(movieID,apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieImages>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        detailView.showMessage("Something went wrong");
                        detailView.stopProgress();
                    }

                    @Override
                    public void onNext(MovieImages jsonObject) {
                        detailView.stopProgress();
                        detailView.processJson(jsonObject);
                    }
                });
    }

    @Override
    public void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();

        detailView.stopProgress();
    }
}
