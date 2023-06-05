package ru.petkeeper.routes.load_responses

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.petkeeper.database.responses_table.ResponsesTable

fun Application.configureLoadResponsesByOrderIdRouting() {
    routing {
        get("/load_order_responses") {
            val orderId = call.request.queryParameters["orderId"]
            if (orderId != null) {
                val responses = ResponsesTable.fetchResponsesByOrderId(orderId.toInt())
                call.respond(responses)
            }
        }
    }
}