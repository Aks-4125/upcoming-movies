package com.test.upcoming.networkcall;

import com.google.gson.JsonObject;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface WebService {

    @GET("upcoming")
    Observable<JsonObject> getMovieList(@Query("api_key") String id);


    @GET("{id}/images")
    Observable<JsonObject> getMovieImages(@Path("id") String id, @Query("api_key") String apikey);

}
