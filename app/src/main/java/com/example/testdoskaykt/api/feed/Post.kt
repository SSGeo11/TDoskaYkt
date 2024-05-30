package com.example.testdoskaykt.api.feed

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.math.BigInteger

data class Post(
    val id: Int,
    val title: String,
    val titleView: String,
    val text: String,
    val price: String,
    val phone: String,
    val hasWhatsapp: Boolean,
    val views: Int,
    val favorites: Int,
    val isVip: Boolean,
    val isExpress: Boolean,
    val isActive: Boolean,
    val isOwner: Boolean,
    val options: List<FeedOption>,
    val publicDate: String,
    val timestamp: BigInteger,
    val picsUrl: List<PicsUrl>,
    val video: List<Video>,
    val latitude: Float,
    val longitude: Float,
    val categoryId: Int,
    val categoryName: String,
    val subcategoryId: Int,
    val subcategoryName: String,
    val rubricId: Int,
    val rubricName: String,
    val isAgency: Boolean,
    val isVerified: Boolean,
    val stickers: List<Any>,
    val bgColor: String,
    val soldCount: Int
){
    companion object {
        fun toJson(post: Post): String {
            return Gson().toJson(post)
        }

        fun fromJson(json: String): Post {
            return Gson().fromJson(json, object : TypeToken<Post>() {}.type)
        }
    }
}
