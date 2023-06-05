package ru.petkeeper.routes.create_response

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.petkeeper.database.responses_table.ResponseDto
import ru.petkeeper.database.responses_table.ResponsesTable

fun Application.configureCreateResponseRouting() {
    routing {
        post("/create_response") {
            val response = call.receive(CreateResponseReceiveRemote::class)
            val isAlreadyCreated = ResponsesTable.checkResponseAlreadyCreated(response.userEmail, response.orderId)
            if (isAlreadyCreated) {
                call.respond("Such response already exists")
            } else {
                call.respond(
                    ResponsesTable.insertResponse(
                        ResponseDto(
                            response.userEmail,
                            response.orderId,
                            response.body,
                            response.status
                        )
                    )
                )
            }
        }
    }
}