package ru.petkeeper.routes.close_order

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.petkeeper.database.orders_table.OrdersTable

fun Application.configureCloseOrderRouting() {
    routing {
        get("/close_order") {
            println("got a request")
            val orderId = call.request.queryParameters["orderId"]
            val order = OrdersTable.fetchOrder(orderId?.toInt() ?: -1)
            if (order != null) {
                println("cancelling an order")
                OrdersTable.cancelOrder(order.orderId)
                call.respond("ok")
            }
            call.respond("not ok")
        }
    }
}