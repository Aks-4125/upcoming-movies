package com.test.upcoming.networkcall;

import com.google.gson.JsonObject;
import com.test.upcoming.model.MovieImages;
import com.test.upcoming.model.Movies;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface WebService {

    @GET("upcoming")
    Observable<Movies> getMovieList(@Query("api_key") String id);


    @GET("{id}/images")
    Observable<MovieImages> getMovieImages(@Path("id") String id, @Query("api_key") String apikey);

}
