package com.alvinchych.blogpost.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.alvinchych.blogpost.api.Post
import com.alvinchych.blogpost.databinding.ItemPostBinding

class PostAdapter_withDiffUtil(private var posts: MutableList<Post>, private val onItemClicked: (post: Post) -> Unit): RecyclerView.Adapter<PostAdapter_withDiffUtil.PostViewHolder>() {
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

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newPosts: List<Post>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() = posts.size
            override fun getNewListSize() = newPosts.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return posts[oldItemPosition].id == newPosts[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return posts[oldItemPosition] == newPosts[newItemPosition]
            }
        })

        this.posts.clear()
        this.posts.addAll(newPosts)
        diffResult.dispatchUpdatesTo(this)
    }
}