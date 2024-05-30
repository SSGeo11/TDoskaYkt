package com.example.testdoskaykt.api.feed

data class Video(
    val url:  String,
    val fileId: String,
    val fileName: String,
    val thumb: String,
    val metadata: Metadata
)

data class Metadata(
    val OS: String,
    val version: String,
    val model: String
)