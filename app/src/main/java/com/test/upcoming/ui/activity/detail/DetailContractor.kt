package com.test.upcoming.ui.activity.detail

import com.google.gson.JsonObject

/**
 * Created by Aks4125 on 7/26/2017.
 */

interface DetailContractor {

    interface IDetailView {
        fun showProgress()

        fun stopProgress()

        fun showMessage(message: String)

        fun processJson(`object`: JsonObject)
    }

    interface IDetailPresenter {
        fun subscribe()

        fun getMovieImages(apiKey: String, movieID: String)

        fun unsubscribe()
    }

}
