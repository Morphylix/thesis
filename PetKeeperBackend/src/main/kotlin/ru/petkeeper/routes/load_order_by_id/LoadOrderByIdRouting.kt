package ru.petkeeper.routes.load_order_by_id

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.petkeeper.database.orders_table.OrdersTable


fun Application.configureLoadOrderByIdRouting() {
    routing {
        get("/load_order_by_id") {
            val orderId = call.request.queryParameters["orderId"]?.toInt()
            println("$orderId is orderId")
            if (orderId != null) {
                val order = OrdersTable.fetchOrder(orderId)
                println("order is $order")
                if (order != null) {
                    println("responding...")
                    call.respond(order)
                }
            }
        }
    }
}