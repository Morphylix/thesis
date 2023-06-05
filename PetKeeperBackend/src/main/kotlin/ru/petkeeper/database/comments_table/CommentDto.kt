package ru.petkeeper.database.comments_table

data class CommentDto(
    val id: Int,
    val senderEmail: String,
    val receiverEmail: String,
    val body: String,
    val rating: Double
)
