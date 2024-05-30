package com.example.testdoskaykt.api.category

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class Category(
    val id: Int,
    val name: String,
    val isPriority: Boolean,
    val count: Int,
    val subcategories: List<Subcategory>
) {
    companion object {
        fun toJson(category: Category): String {
            return Gson().toJson(category)
        }

        fun fromJson(json: String): Category {
            return Gson().fromJson(json, object : TypeToken<Category>() {}.type)
        }
    }
}

data class Subcategory(
    val id: Int,
    val name: String,
    val isPriority: Boolean,
    val isShowMap: Boolean,
    val count: Int,
    val rubrics: List<Rubric>,
    val options: List<Option>
)
data class Rubric(
    val id: Int,
    val name: String,
    val isPriority: Boolean,
    val isShowMap: Boolean,
    val count: Int,
)
data class Option(
    val id: Int,
    val name: String,
    val type: String,
    val required: Boolean,
    val multiselect: Boolean,
    val unit: String,
    val values: List<Value>
)
data class Value(
    val id: Int,
    val name: String,
    val activeCount: Int,
    val isPriority: Boolean,
    val subOptions: List<SubOption>
)
data class SubOption(
    val id: Int,
    val name: String,
    val type: String,
    val required: Boolean,
    val multiselect: Boolean,
    val values: List<Value>
)