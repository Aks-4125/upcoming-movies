package com.test.upcoming.ui.activity.detail;

import com.google.gson.JsonObject;
import com.test.upcoming.model.MovieImages;

/**
 * Created by Aks4125 on 7/26/2017.
 */

public interface DetailContractor {

    interface IDetailView {
        void showProgress();

        void stopProgress();

        void showMessage(String message);

        void processJson(MovieImages object);
    }

    interface IDetailPresenter {
        void subscribe();

        void getMovieImages(String apiKey, String movieID);

        void unsubscribe();
    }

}
