package com.test.upcoming.networkcall;

import com.google.gson.JsonObject;
import com.test.upcoming.model.MovieImages;
import com.test.upcoming.model.Movies;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface WebService {

    @GET("upcoming")
    Single<Movies> getMovieList(@Query("api_key") String id);


    @GET("{id}/images")
    Single<MovieImages> getMovieImages(@Path("id") String id, @Query("api_key") String apikey);

}
