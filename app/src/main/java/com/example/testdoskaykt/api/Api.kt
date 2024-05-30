package com.example.testdoskaykt.api

import com.example.testdoskaykt.api.category.CategoryResponse
import com.example.testdoskaykt.api.customfeed.CustomFeedsResponse
import com.example.testdoskaykt.api.feed.FeedResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("custom-feed?name=android")
    suspend fun getCustomFeeds() : CustomFeedsResponse
    @GET("feed")
    suspend fun getFeeds(
        @Query("cid") categoryId: String?,
        @Query("sid") subcategoryId: String?,
        @Query("rid") rubricId: String?
    ) : FeedResponse
    @GET("categories?scope=simple")
    suspend fun getCategories() : CategoryResponse
}