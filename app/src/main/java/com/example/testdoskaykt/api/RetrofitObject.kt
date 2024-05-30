package com.example.testdoskaykt.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObject {
    val api: Api by lazy {
        Retrofit.Builder()
            .baseUrl("https://doska.ykt.ru/v4/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(Api::class.java)
    }
}