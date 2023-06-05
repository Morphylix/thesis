package ru.petkeeper.routes.email_validation

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.petkeeper.database.users_table.UsersTable
import ru.petkeeper.database.validation_table.ValidationDto
import ru.petkeeper.database.validation_table.ValidationsTable
import ru.petkeeper.util.mail.MailService
import kotlin.random.Random

fun Application.configureEmailValidationRouting() {
    routing {
        post("/email_validation") {
            val receive = call.receive(EmailValidationReceiveRemote::class)
            val email = receive.email

            if (UsersTable.fetchUser(email) != null) {
                call.respond(HttpStatusCode.BadRequest, "User already exists")
            } else {
                val code = Random.nextInt(1000, 9999)
                MailService.sendMail(email, "Validation code", "Your validation code is $code")

                if (ValidationsTable.fetchValidation(email) == null) {
                    ValidationsTable.insertValidation(
                        ValidationDto(
                            email,
                            code
                        )
                    )
                } else {
                    ValidationsTable.replaceValidation(
                        ValidationDto(
                            email,
                            code
                        )
                    )
                }
                call.respond(
                    EmailValidationResponseRemote(true)
                )
            }
        }
    }
}