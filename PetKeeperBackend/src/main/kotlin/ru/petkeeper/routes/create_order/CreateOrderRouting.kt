package ru.petkeeper.routes.create_order

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.petkeeper.database.orders_table.OrderDto
import ru.petkeeper.database.orders_table.OrdersTable
import ru.petkeeper.database.pets_table.PetsTable
import ru.petkeeper.database.token_table.TokensTable

fun Application.configureCreateOrderRouting() {
    routing {
        post("/create_order") {
            val token = call.request.queryParameters["token"]
            if (token != null) {
                val orderDtoReceive = call.receive(CreateOrderReceiveRemote::class)
                val email = TokensTable.fetchEmail(token)
                PetsTable.insertPet(orderDtoReceive.pet)
                val petId = PetsTable.idCounter
                val pet = PetsTable.fetchPet(petId)
                println(pet)
                println("petId = idCounter in routing is $petId")
                val order = OrderDto(
                    pet = pet,
                    createDate = orderDtoReceive.createDate,
                    isCancelled = orderDtoReceive.isCancelled,
                    recs = orderDtoReceive.recs,
                    about = orderDtoReceive.about,
                    userEmail = email ?: "",
                    isTemporary = orderDtoReceive.isTemporary,
                    startDate = orderDtoReceive.startDate,
                    endDate = orderDtoReceive.endDate
                )
                val insertedOrder = OrdersTable.insertOrder(order)
                call.respond(insertedOrder)
            }
        }
    }
}