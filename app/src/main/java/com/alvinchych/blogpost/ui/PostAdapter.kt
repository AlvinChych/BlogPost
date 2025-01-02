package com.alvinchych.blogpost.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.alvinchych.blogpost.api.Post
import com.alvinchych.blogpost.databinding.ItemPostBinding

class PostAdapter(private val onItemClicked: (post: Post) -> Unit): ListAdapter<Post, PostAdapter.PostViewHolder>(DIFF_CALLBACK) {
    
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
               return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }
        }
    }
    
    class PostViewHolder(val onItemClicked: (post: Post) -> Unit, private val binding: ItemPostBinding): ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(post: Post) {
            binding.title.text = "${post.id}. ${post.title}"
            binding.content.text = post.content
            binding.root.setOnClickListener {
                onItemClicked(post)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context))
        return PostViewHolder(onItemClicked, binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}