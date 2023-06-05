package ru.petkeeper.routes.load_user_orders

data class LoadUserOrdersReceiveRemote(
    val email: String,
    val type: OrderType //
)
