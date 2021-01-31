package com.example.testapp2001

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val BASE_URL = "http://ec2-13-125-40-216.ap-northeast-2.compute.amazonaws.com:3000"
    private var retrofit: Retrofit? = null
    val client: Retrofit
        get() {
            while (retrofit == null) {
                retrofit= Retrofit.Builder().baseUrl("http://ec2-3-34-40-71.ap-northeast-2.compute.amazonaws.com:3000") .addConverterFactory(GsonConverterFactory.create()).build();

            }


            return retrofit!!
        }
}