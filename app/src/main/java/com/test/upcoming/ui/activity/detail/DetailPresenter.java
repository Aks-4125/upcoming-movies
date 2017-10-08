package com.test.upcoming.ui.activity.detail;

import com.test.upcoming.model.MovieImages;
import com.test.upcoming.networkcall.WebService;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Aks4125 on 7/26/2017.
 */

public class DetailPresenter implements DetailContractor.IDetailPresenter {

    private DetailContractor.IDetailView detailView;
    private WebService webService;
    private CompositeDisposable mDisposable;

    @Inject
    public DetailPresenter(WebService webService) {
        this.webService = webService;
    }

    public void setDetailView(DetailContractor.IDetailView detailView) {
        this.detailView = detailView;
    }

    private void onError(Throwable throwable) {
        unsubscribe();// dispose the disposable
        detailView.showMessage("Something went wrong");
        detailView.stopProgress();
    }

    private void onSuccess(MovieImages movieImages) {
        unsubscribe();
        detailView.stopProgress();
        detailView.processJson(movieImages);
    }

    @Override
    public void subscribe() {
        mDisposable = new CompositeDisposable();
    }


    @Override
    public void getMovieImages(String apiKey, String movieID) {
        detailView.showProgress();
        subscribe();
        mDisposable.add(
                webService.getMovieImages(movieID, apiKey)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onSuccess, this::onError)

        );
    }

    @Override
    public void unsubscribe() {
        if (mDisposable != null && !mDisposable.isDisposed())
            mDisposable.dispose();
        detailView.stopProgress();
    }


}
