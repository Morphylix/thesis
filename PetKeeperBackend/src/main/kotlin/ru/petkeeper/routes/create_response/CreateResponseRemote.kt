package ru.petkeeper.routes.create_response

data class CreateResponseReceiveRemote(
    val userEmail: String,
    val orderId: Int,
    val body: String,
    val status: Boolean
)

data class CreateResponseRespondRemote(
    val userEmail: String,
    val orderId: Int,
    val body: String,
    val status: Boolean
)