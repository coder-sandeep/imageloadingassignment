package com.codersandeep.imageloadingassignment

import com.codersandeep.imageloadingassignment.models.ArticlesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticlesAPI {
    @GET("/api/v2/content/misc/media-coverages")
    suspend fun getAllArticles(@Query("limit") limit: Int): Response<ArticlesResponse>
}
