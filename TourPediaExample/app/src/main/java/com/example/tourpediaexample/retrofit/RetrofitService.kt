package com.example.tourpediaexample.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {

    object RetrofitHelper {
        val baseURL = "http://tour-pedia.org/api/"

        fun getInstance() : Retrofit {
            return Retrofit.Builder().baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
    }

    val tourApi = RetrofitHelper.getInstance().create(PlacesAPI::class.java)
}