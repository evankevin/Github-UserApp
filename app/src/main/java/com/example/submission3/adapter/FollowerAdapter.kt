package com.example.submission3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submission3.databinding.ItemRowUserBinding
import com.example.submission3.response.UserResponse

class FollowerAdapter(private val listFollower: List<UserResponse>) : RecyclerView.Adapter<FollowerAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listFollower[position]

        with(holder.binding) {
            Glide.with(root.context)
                .load(user.avatarUrl)
                .circleCrop()
                .into(imgItemPhoto)
            tvItemUsername.text = user.login
            tvItemUrl.text = user.htmlUrl
        }
    }
    override fun getItemCount(): Int = listFollower.size

}