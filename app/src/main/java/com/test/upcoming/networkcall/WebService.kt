package com.test.upcoming.networkcall

import com.google.gson.JsonObject

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

interface WebService {

    @GET("upcoming")
    fun getMovieList(@Query("api_key") id: String): Observable<JsonObject>


    @GET("{id}/images")
    fun getMovieImages(@Path("id") id: String, @Query("api_key") apikey: String): Observable<JsonObject>

}
