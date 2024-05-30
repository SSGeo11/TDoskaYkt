package com.example.testdoskaykt.api.customfeed

data class CustomFeed(
    val id: Int,
    val title: String,
    val subtitle: String,
    val text: String,
    val thumbUrl: String,
    val previewUrl: String,
    val normalUrl: String,
    val originalUrl: String,
    val searchUrl: String,
    val ymGoal: String,
    val url: String,
    val type: String,
    val sort: Int,
    val markLabel: String,
    val markDesc: String,
    val markBright: Boolean
)