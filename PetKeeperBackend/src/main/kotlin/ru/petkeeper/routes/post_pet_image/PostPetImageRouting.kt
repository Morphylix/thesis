package ru.petkeeper.routes.post_pet_image

import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.petkeeper.database.pets_table.PetsTable

fun Application.configurePostPetImageRouting() {
    routing {
        post("/upload_pet_image") {
            var fileName = ""
            val multipartData = call.receiveMultipart()
            val petId = call.request.queryParameters["petId"]?.toInt()
            multipartData.forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        println("got a file")
                        fileName = part.originalFileName as String
                        val fileBytes = part.streamProvider().readBytes()
                        //val image = String(fileBytes).replace(Regex("""\u0000"""), "LOL")
                        val image = String(fileBytes)
                        println("file is ${image}")
                        if (petId != null) {
                            PetsTable.postPetPic(petId, fileBytes)
                        }
                    }

                    else -> {
                        println("other stuff")
                    }
                }
                part.dispose()
            }
            println("multipart got $multipartData")
            call.respond("ABOBA")
        }
    }
}