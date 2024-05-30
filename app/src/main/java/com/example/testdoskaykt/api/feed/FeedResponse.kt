package com.example.testdoskaykt.api.feed

data class FeedResponse(
    val result: String,
    val code: Int,
    val data: Posts,
    val msg: String
)
