package ru.petkeeper.database.responses_table

data class ResponseDto(
    val userEmail: String,
    val orderId: Int,
    val body: String,
    val status: Boolean
)