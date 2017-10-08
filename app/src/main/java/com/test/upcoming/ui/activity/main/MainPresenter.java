package com.test.upcoming.ui.activity.main;

import com.test.upcoming.networkcall.WebService;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Aks4125 on 7/11/2017.
 */

public class MainPresenter implements MainContractor.IMainPresenter {

    private MainContractor.IMainView mainView;
    private WebService webService;
    private CompositeDisposable mDisposable;

    //must inject the constructor
    @Inject
    public MainPresenter(WebService webService) {
        this.webService = webService;
    }

    public void setMainView(MainContractor.IMainView mainView) {
        this.mainView = mainView;
    }


    @Override
    public void subscribe() {
        mDisposable = new CompositeDisposable();
    }


    @Override
    public void getMovieList(String key) {
        mainView.showProgress();
        //mainView.showMessage("loading" );
        subscribe();
        mDisposable.add(
                webService.getMovieList(key)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSuccess(movies -> {
                            mainView.stopProgress();
                            mainView.processJson(movies);
                            unsubscribe();
                        })
                        .doOnError(throwable -> {
                            mainView.showMessage("Something went wrong");
                            mainView.stopProgress();
                            unsubscribe();
                        })
                        .subscribe()
        );

    }

    @Override
    public void unsubscribe() {
        if (mDisposable != null && !mDisposable.isDisposed())
            mDisposable.dispose();
        mainView.stopProgress();
    }

}
