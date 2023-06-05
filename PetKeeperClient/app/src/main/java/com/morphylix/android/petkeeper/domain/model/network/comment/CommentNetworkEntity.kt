package com.morphylix.android.petkeeper.domain.model.network.comment

data class CommentNetworkEntity(
    val id: Int,
    val senderEmail: String,
    val receiverEmail: String,
    val body: String,
    val rating: Double
)
