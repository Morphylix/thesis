package ru.petkeeper.routes.load_pet_by_id

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.petkeeper.database.pets_table.PetsTable

fun Application.configureLoadPetByIdRouting() {
    routing {
        get("/load_pet_by_id") {
            val petId = call.request.queryParameters["petId"]?.toInt()
            val pet = PetsTable.fetchPet(petId ?: -1)
            if (pet != null) {
                call.respond(pet)
            }
        }
    }
}