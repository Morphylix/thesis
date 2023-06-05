package ru.petkeeper.routes.code_validation

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.petkeeper.database.validation_table.ValidationsTable

fun Application.configureCodeValidationRouting() {
    routing {
        post("/code_validation") {
            val receive = call.receive(CodeValidationReceiveRemote::class)

            val email = receive.email
            val code = receive.code

            val fetchedValidation = ValidationsTable.fetchValidation(email)
            if (fetchedValidation == null || fetchedValidation.code != code) {
                call.respond(HttpStatusCode.BadRequest, "Wrong code")
            } else {
                call.respond(
                    CodeValidationResponseRemote(true)
                )
            }
        }
    }
}