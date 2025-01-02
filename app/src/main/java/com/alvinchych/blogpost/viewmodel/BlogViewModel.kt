package com.alvinchych.blogpost.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alvinchych.blogpost.api.Post
import com.alvinchych.blogpost.repository.BlogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class BlogViewModel @Inject constructor(private val blogRepository: BlogRepository): ViewModel() {

    companion object {
        const val LIMIT = 10
    }

    private var pages: Int = 1
    private var isLoading: Boolean = false
    private var _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    private val _postsState = MutableStateFlow<List<Post>>(emptyList())
    val postsState = _postsState.asStateFlow()

    fun fetchBlogLegacy() {
        if (!isLoading) {
            isLoading = true
            blogRepository.getPostsLegacy(pages, LIMIT).enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    if (response.isSuccessful) {
                        response.body()?.let { posts ->
                            if (posts.isNotEmpty()) {
                                pages++
                                val currentPosts = _posts.value ?: listOf()
                                _posts.postValue(currentPosts + posts)
                            }
                        }
                    }
                    isLoading = false
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    isLoading = false
                }
            })
        }
    }

    fun fetchBlogToLiveData() {
        if (!isLoading) {
            isLoading = true
            CoroutineScope(Dispatchers.IO).launch {
                val result = blogRepository.getPosts(pages, LIMIT)
                if (result.isSuccess) {
                    result.getOrNull()?.let { posts ->
                        if (posts.isNotEmpty()) {
                            pages++
                            val currentPosts = _posts.value ?: listOf()
                            _posts.postValue(currentPosts + posts)
                        }
                    }
                }
                isLoading = false
            }
        }
    }

    fun fetchBlogToStateFlow() {
        if (!isLoading) {
            isLoading = true
            CoroutineScope(Dispatchers.IO).launch {
//                delay(1000)
                val result = blogRepository.getPosts(pages, LIMIT)
                if (result.isSuccess) {
                    result.getOrNull()?.let { posts ->
                        if (posts.isNotEmpty()) {
                            pages++
                            _postsState.update { currentPosts ->
                                currentPosts + posts
                            }
                        }
                    }
                }
                isLoading = false
            }
        }
    }
}