package com.codersandeep.imageloadingassignment.network

import com.codersandeep.imageloadingassignment.api.ArticlesAPI
import com.codersandeep.imageloadingassignment.utils.Urls
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = Urls.BASE_URL

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

object ApiClient {
    val apiService: ArticlesAPI by lazy {
        RetrofitClient.retrofit.create(ArticlesAPI::class.java)
    }
}