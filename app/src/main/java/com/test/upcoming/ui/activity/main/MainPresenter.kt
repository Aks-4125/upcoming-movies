package com.test.upcoming.ui.activity.main

import com.test.upcoming.networkcall.WebService
import com.google.gson.JsonObject


import javax.inject.Inject

import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by Aks4125 on 7/11/2017.
 */

class MainPresenter @Inject
constructor(private val webService: WebService) : MainContractor.IMainPresenter {

    private var mainView: MainContractor.IMainView? = null
    private var mSubscription: Subscription? = null

    public fun setMainView(mainView: MainContractor.IMainView) {
        this.mainView = mainView
    }


    override fun subscribe() {

    }


    override fun getMovieList(key: String) {
        mainView!!.showProgress()
        //mainView.showMessage("loading" );


        mSubscription = webService.getMovieList(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<JsonObject>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        mainView!!.showMessage("Something went wrong")
                        mainView!!.stopProgress()
                    }

                    override fun onNext(jsonObject: JsonObject) {
                        mainView!!.stopProgress()
                        mainView!!.processJson(jsonObject)
                    }
                })


    }

    override fun unsubscribe() {
        if (mSubscription != null && !mSubscription!!.isUnsubscribed)
            mSubscription!!.unsubscribe()

        mainView!!.stopProgress()

    }


}
