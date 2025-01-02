package com.alvinchych.blogpost.repository

import com.alvinchych.blogpost.api.BlogApi
import com.alvinchych.blogpost.api.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

class BlogRepository @Inject constructor(private val blogApi: BlogApi) {
    suspend fun getPosts(page: Int, limit: Int): Result<List<Post>> {
        return try {
            val response = blogApi.getPosts(page, limit)
            if (response.isSuccessful) {
                Result.success(response.body() ?: listOf())
            } else {
                Result.failure(RuntimeException("Fail to fetch data"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getPostsFlow(page: Int, limit: Int): Flow<List<Post>> {
        return flow {
            val response = blogApi.getPosts(page, limit)
            if (response.isSuccessful) {
                response.body()?.let { posts ->
                    emit(posts)
                } ?: emit(listOf())
            } else {
                emit(listOf())
            }
        }
    }

    fun getPostsLegacy(page: Int, limit: Int): Call<List<Post>> {
        return blogApi.getPostsLegacy(page, limit)
    }
}