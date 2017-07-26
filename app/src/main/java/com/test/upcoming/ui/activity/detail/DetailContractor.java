package com.test.upcoming.ui.activity.detail;

import com.google.gson.JsonObject;

/**
 * Created by Aks4125 on 7/26/2017.
 */

public interface DetailContractor {

    interface IDetailView {
        void showProgress();

        void stopProgress();

        void showMessage(String message);

        void processJson(JsonObject object);
    }

    interface IDetailPresenter {
        void subscribe();

        void getMovieImages(String apiKey, String movieID);

        void unsubscribe();
    }

}
