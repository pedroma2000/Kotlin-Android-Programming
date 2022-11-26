package com.example.tourpediaexample.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesAPI {

    @GET("getPlaces")
    fun getPlaces(@Query("location") location:String) : Call<List<Place>>
}