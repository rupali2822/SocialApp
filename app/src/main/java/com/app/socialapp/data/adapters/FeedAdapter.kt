package com.app.socialapp.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.socialapp.data.model.FeedPostModel
import com.app.socialapp.databinding.ItemFeedPostBinding

class FeedAdapter(
    private val items: List<FeedPostModel>
) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    inner class FeedViewHolder(
        val binding: ItemFeedPostBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val binding = ItemFeedPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FeedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val item = items[position]
        val b = holder.binding

        b.tvUserName.text = item.userName
        b.tvTimeLocation.text = item.timeLocation
        b.tvTitle.text = item.title
        b.tvDescription.text = item.description
        b.tvLikes.text = item.likes
        b.tvComments.text = item.comments

        b.imgUser.setImageResource(item.userImage)
        b.imgPost.setImageResource(item.postImage)
    }

    override fun getItemCount(): Int = items.size
}

