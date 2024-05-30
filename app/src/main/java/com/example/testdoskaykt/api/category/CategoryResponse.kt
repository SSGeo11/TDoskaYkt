package com.example.testdoskaykt.api.category

data class CategoryResponse(
    val result: String,
    val code: Int,
    val data: List<Category>,
    val msg: String
)
