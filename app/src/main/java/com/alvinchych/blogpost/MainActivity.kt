package com.alvinchych.blogpost

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alvinchych.blogpost.api.Post
import com.alvinchych.blogpost.api.RetrofitHelper
import com.alvinchych.blogpost.databinding.ActivityMainBinding
import com.alvinchych.blogpost.repository.BlogRepository
import com.alvinchych.blogpost.ui.FragmentTestActivity
import com.alvinchych.blogpost.ui.PostAdapter
import com.alvinchych.blogpost.ui.PostAdapter_old
import com.alvinchych.blogpost.ui.PostAdapter_withDiffUtil
import com.alvinchych.blogpost.ui.TouchEventTestActivity
import com.alvinchych.blogpost.viewmodel.BlogViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding
//    private val viewModel: BlogViewModel by viewModels<BlogViewModel> {
//        BlogViewModelFactory(BlogRepository(RetrofitHelper.apiService))
//    }
    private val viewModel: BlogViewModel by viewModels()

    private var postAdapter: PostAdapter? = null
//    private var postAdapter: PostAdapter_old? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.posts.observe(this) { posts ->
            Log.d("Blog", posts.toString())
            postAdapter?.submitList(posts)
//            postAdapter?.updateItems(posts)
        }

        postAdapter = PostAdapter()
//        postAdapter = PostAdapter_old(mutableListOf())
        binding.list.adapter = postAdapter
        binding.list.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(binding.list.context,
            (binding.list.layoutManager as LinearLayoutManager).orientation)
        binding.list.addItemDecoration(dividerItemDecoration)

        binding.list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (totalItemCount <= firstVisibleItemPosition + visibleItemCount) {
                    viewModel.fetchBlog()
//                    viewModel.fetchBlogLegacy()
                }
            }
        })

        binding.touchTest.setOnClickListener {
            val intent = Intent(this, TouchEventTestActivity::class.java)
            startActivity(intent)
        }

        binding.fragmentTest.setOnClickListener {
            val intent = Intent(this, FragmentTestActivity::class.java)
            intent.putExtra(FragmentTestActivity.ACTIVITY_PARAM_COUNT, 0)
            startActivity(intent)
        }

        binding.refresh.setOnClickListener {
            viewModel.fetchBlog()
//            viewModel.fetchBlogLegacy()
        }
    }

    class BlogViewModelFactory(private val blogRepository: BlogRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BlogViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return BlogViewModel(blogRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}