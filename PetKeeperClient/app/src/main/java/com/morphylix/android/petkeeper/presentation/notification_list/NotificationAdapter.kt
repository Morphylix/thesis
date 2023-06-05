package com.morphylix.android.petkeeper.presentation.notification_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.morphylix.android.petkeeper.R
import com.morphylix.android.petkeeper.databinding.CommentListItemBinding
import com.morphylix.android.petkeeper.databinding.NotificationItemBinding
import com.morphylix.android.petkeeper.domain.model.domain.Comment
import com.morphylix.android.petkeeper.domain.model.domain.Notification

class NotificationAdapter(private val notifications: List<Notification>): RecyclerView.Adapter<NotificationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notifications[position])
    }
}

    class NotificationViewHolder(private val binding: NotificationItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(notification: Notification) {
            binding.userNameTextView.text = binding.root.context.getString(R.string.person_chose_you, notification.senderName)
            binding.notificationBodyTextView.text = notification.body
        }
    }