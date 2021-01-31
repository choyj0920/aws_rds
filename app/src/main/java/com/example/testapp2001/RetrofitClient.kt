package com.example.testapp2001

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val BASE_URL = "http://ec2-3-36-49-241.ap-northeast-2.compute.amazonaws.com:3000"
    private var retrofit: Retrofit? = null
    val client: Retrofit
        get() {
            while (retrofit == null) {
                retrofit= Retrofit.Builder().baseUrl(BASE_URL) .addConverterFactory(GsonConverterFactory.create()).build();

            }

            return retrofit!!
        }
}