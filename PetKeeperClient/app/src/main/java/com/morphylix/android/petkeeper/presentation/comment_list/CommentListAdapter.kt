package com.morphylix.android.petkeeper.presentation.comment_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.morphylix.android.petkeeper.databinding.CommentListItemBinding
import com.morphylix.android.petkeeper.domain.model.domain.Comment

class CommentListAdapter(private val comments: List<Comment>): RecyclerView.Adapter<CommentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = CommentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(comments[position])
    }
}

class CommentViewHolder(private val binding: CommentListItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(comment: Comment) {
        binding.ratingbar.rating = comment.rating.toFloat()
        binding.userNameTextView.text = comment.senderName
        binding.responseBodyTextView.text = comment.body
    }
}
