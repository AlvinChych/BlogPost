package com.alvinchych.blogpost.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BlogApi {
    @GET("posts")
    fun getPostsLegacy(@Query("_page") page: Int = 1, @Query("_limit") limit: Int = 10): Call<List<Post>>

    @GET("posts")
    suspend fun getPosts(@Query("_page") page: Int = 1, @Query("_limit") limit: Int = 10): Response<List<Post>>
}