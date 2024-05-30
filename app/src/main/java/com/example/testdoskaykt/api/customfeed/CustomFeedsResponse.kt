package com.example.testdoskaykt.api.customfeed

data class CustomFeedsResponse(
    val result: String,
    val code: Int,
    val data: CustomFeeds,
    val msg: String
)
