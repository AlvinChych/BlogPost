package com.alvinchych.blogpost.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alvinchych.blogpost.databinding.ActivityShowBlogBBinding
import com.alvinchych.blogpost.viewmodel.BlogViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowBlogActivityC : AppCompatActivity() {

    private lateinit var binding: ActivityShowBlogBBinding
    private val viewModel: BlogViewModel by viewModels()
    private var postAdapter: PostAdapter_withDiffUtil? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowBlogBBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setContentView(binding.root)

        viewModel.posts.observe(this) { posts ->
            Log.d("Blog", posts.toString())
            postAdapter?.updateItems(posts)
        }

        postAdapter = PostAdapter_withDiffUtil(mutableListOf()) {
            Toast.makeText(this, "Clicked ${it.title}", Toast.LENGTH_SHORT).show()
        }
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
                    viewModel.fetchBlogToLiveData()
                }
            }
        })

        viewModel.fetchBlogToLiveData()
    }
}