package com.test.upcoming.ui.activity.detail

import com.test.upcoming.networkcall.WebService
import com.google.gson.JsonObject

import javax.inject.Inject

import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by Aks4125 on 7/26/2017.
 */

class DetailPresenter @Inject
constructor(private val webService: WebService) : DetailContractor.IDetailPresenter {

    private var detailView: DetailContractor.IDetailView? = null
    private var mSubscription: Subscription? = null

    fun setDetailView(detailView: DetailContractor.IDetailView) {
        this.detailView = detailView
    }


    override fun subscribe() {

    }

    override fun getMovieImages(apiKey: String, movieID: String) {
        detailView!!.showProgress()

        mSubscription = webService.getMovieImages(movieID, apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<JsonObject>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        detailView!!.showMessage("Something went wrong")
                        detailView!!.stopProgress()
                    }

                    override fun onNext(jsonObject: JsonObject) {
                        detailView!!.stopProgress()
                        detailView!!.processJson(jsonObject)
                    }
                })
    }

    override fun unsubscribe() {
        if (mSubscription != null && !mSubscription!!.isUnsubscribed)
            mSubscription!!.unsubscribe()

        detailView!!.stopProgress()
    }
}
