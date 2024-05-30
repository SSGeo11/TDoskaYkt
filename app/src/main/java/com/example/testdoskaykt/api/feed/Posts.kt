package com.example.testdoskaykt.api.feed

data class Posts(
    val posts: List<Post>,
    val token: String,
    val organization: List<Any>,
    val categories: List<FeedCategory>,
    val count: Int
)
