package ru.petkeeper.routes.load_orders

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.petkeeper.database.orders_table.OrdersTable

fun Application.configureLoadOrdersRouting() {
    routing {
        post("/load_orders") {
            val receive = call.receive<SettingsConfig>()
            println("Got settings config sort by ${receive.sortBy} filter by ${receive.filterBy}")
            val orders = OrdersTable.fetchAllOrders(receive)
            //println("orderlist is $orders")
            call.respond(orders.reversed())
        }
    }
}