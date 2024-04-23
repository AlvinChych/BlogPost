package com.alvinchych.blogpost.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alvinchych.blogpost.api.Post
import com.alvinchych.blogpost.repository.BlogRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class BlogViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: BlogViewModel

    @MockK
    private lateinit var blogRepository : BlogRepository

    private val mainThreadSurrogate = newSingleThreadContext("Main Thread")

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun testFetchData() {
        val postList = mutableListOf<Post>()
        postList.add(Post(1, 1, "test", "test"))
        val result = Result.success(postList)
        coEvery { blogRepository.getPosts(any(), any()) } returns result

        viewModel = BlogViewModel(blogRepository)
        viewModel.fetchBlog()

        var posts: List<Post>? = null
        val latch = CountDownLatch(1)
        val observer = object: Observer<List<Post>> {
            override fun onChanged(value: List<Post>) {
                posts = value
                latch.countDown()
                viewModel.posts.removeObserver(this)
            }
        }
        viewModel.posts.observeForever(observer)
        latch.await(10, TimeUnit.SECONDS)

        assertNotNull(posts)
        assertTrue(posts?.isNotEmpty() ?: false)
    }
}