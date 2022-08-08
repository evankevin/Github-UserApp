package com.example.submission3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submission3.helper.UserDiffCallback
import com.example.submission3.databinding.ItemRowUserBinding
import com.example.submission3.response.UserResponse

class FavoriteAdapter :RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback
    private val user = ArrayList<UserResponse>()



    inner class ViewHolder(private var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user:UserResponse){
            binding.root.setOnClickListener{
                onItemClickCallback.onItemClicked(user)
            }
            binding.apply {
                Glide.with(itemView)
                    .load(user.avatarUrl)
                    .circleCrop()
                    .into(binding.imgItemPhoto)
                tvItemUsername.text = user.login
                tvItemUrl.text = user.htmlUrl
            }
        }
    }

    fun setListUser(user : ArrayList<UserResponse>){
        val diffCallback = UserDiffCallback(this.user, user)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.user.clear()
        this.user.addAll(user)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ViewHolder((view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(user[position])
    }

    override fun getItemCount(): Int = user.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback{
        fun onItemClicked(user: UserResponse)
    }


}