package com.test.upcoming.ui.activity.main;

import com.google.gson.JsonObject;
import com.test.upcoming.model.Movies;

/**
 * Created by Aks4125 on 7/11/2017.
 */

public class MainContractor {


    interface IMainView {
        void showProgress();

        void stopProgress();

        void showMessage(String message);

        void processJson(Movies object);
    }

    interface IMainPresenter {
        void subscribe();

        void getMovieList(String key);

        void unsubscribe();
    }

}
