package com.example.testdoskaykt

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testdoskaykt.api.RetrofitObject
import com.example.testdoskaykt.api.category.Category
import com.example.testdoskaykt.api.customfeed.CustomFeed
import com.example.testdoskaykt.api.feed.Post
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _customFeeds = MutableStateFlow<List<CustomFeed>>(emptyList())
    val customFeeds: StateFlow<List<CustomFeed>> = _customFeeds

    private val _feeds = MutableStateFlow<List<Post>>(emptyList())
    val feeds: StateFlow<List<Post>> = _feeds

    private val _recommendedFeeds = MutableStateFlow<List<Post>>(emptyList())
    val recommendedFeeds: StateFlow<List<Post>> = _recommendedFeeds

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _styleState = MutableStateFlow(1)
    val styleState: StateFlow<Int> = _styleState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _categoryId = MutableStateFlow<String?>(null)
    val categoryId: StateFlow<String?> = _categoryId

    private val _subcategoryId = MutableStateFlow<String?>(null)
    val subcategoryId: StateFlow<String?> = _subcategoryId

    private val _rubricId = MutableStateFlow<String?>(null)
    val rubricId: StateFlow<String?> = _rubricId

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchCustomFeeds()
        fetchFeeds()
        fetchCategories()
    }

    fun setCategoryId(id: String?){
        _categoryId.value = id
        refreshFeeds()
    }

    fun setSubcategoryId(id: String?) {
        _subcategoryId.value = id
        refreshFeeds()
    }

    fun setRubricId(id: String?) {
        _rubricId.value = id
        refreshFeeds()
    }

    fun setStyle(style: Int){
        _styleState.value = style
    }

    fun setFeedNull(){
        _feeds.value = emptyList()
    }

    fun fetchRecommendedFeeds(cid: String, sid: String, rid: String){
        viewModelScope.launch {
            try {
                val recommendedFeeds = RetrofitObject.api.getFeeds(cid, sid, rid)
                _recommendedFeeds.value = recommendedFeeds.data.posts
            } catch (e: Exception){
                Log.d("Main", "Error fetching recommended feeds", e)
            }
        }
    }

    private fun fetchCategories(){
        viewModelScope.launch{
            try {
                val categories = RetrofitObject.api.getCategories()
                _categories.value = categories.data
            } catch (e: Exception){
                Log.d("Main", "Error fetching categories", e)
            }
        }
    }


    private fun fetchCustomFeeds(){
        viewModelScope.launch{
            try {
                val customFeeds = RetrofitObject.api.getCustomFeeds()
                _customFeeds.value = customFeeds.data.feeds
            } catch (e: Exception){
                Log.d("Main", "Error fetching custom feeds", e)
            }
        }
    }
    fun fetchFeeds(){
        if (_isLoading.value) return

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val feeds = RetrofitObject.api.getFeeds(categoryId.value, subcategoryId.value, rubricId.value)
                _feeds.value = feeds.data.posts
                _isLoading.value = false
            } catch (e: Exception){
                Log.d("Main", "Error fetching feeds", e)
                _isLoading.value = false
            }
        }
    }
    fun fetchMoreFeeds(){
        if (_isLoading.value) return

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val newFeeds = RetrofitObject.api.getFeeds(categoryId.value, subcategoryId.value, rubricId.value)
                _feeds.value += newFeeds.data.posts
                _isLoading.value = false
            } catch (e: Exception){
                Log.d("Main", "Error fetching more feeds", e)
                _isLoading.value = false
            }
        }
    }
    fun refreshFeeds() {
        viewModelScope.launch {
            _isRefreshing.value = true
            setFeedNull()
            try {
                fetchFeeds()
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun setNull() {
        _categoryId.value = null
        _subcategoryId.value = null
        _rubricId.value = null
        refreshFeeds()
    }

    fun search(newQuery: String) {

    }

}
