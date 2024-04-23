package com.alvinchych.blogpost.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.alvinchych.blogpost.api.Post
import com.alvinchych.blogpost.databinding.ItemPostBinding

class PostAdapter_old(private var posts: MutableList<Post>): RecyclerView.Adapter<PostAdapter_old.PostViewHolder>() {
    class PostViewHolder(private val binding: ItemPostBinding): ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.title.text = "${post.id}. ${post.title}"
            binding.content.text = post.content
            binding.root.setOnClickListener {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context))
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }

    fun updateItems(newPosts: List<Post>) {
        val currentSize = posts.size
        val newSize = newPosts.size
        posts.addAll(newPosts.subList(currentSize, newSize))
        notifyItemRangeChanged(currentSize, newSize - currentSize)
    }
}