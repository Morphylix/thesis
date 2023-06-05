package ru.petkeeper.routes.load_user_orders

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.petkeeper.database.orders_table.OrderDto
import ru.petkeeper.database.orders_table.OrdersTable

fun Application.configureLoadUserOrdersRouting() {
    routing {
        post("/load_user_orders") {
            val receive = call.receive(LoadUserOrdersReceiveRemote::class)
            val orders: List<OrderDto> = when (receive.type) {
                OrderType.ALL -> {
                    emptyList()
                }
                OrderType.PLACED -> {
                    OrdersTable.fetchUserOrders(receive.email)
                }
                OrderType.TAKEN -> {
                    emptyList()
                }
            }
            call.respond(orders)
        }
    }
}