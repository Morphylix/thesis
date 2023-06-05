package ru.petkeeper.database.responses_table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.petkeeper.database.orders_table.OrderDto
import ru.petkeeper.database.orders_table.OrdersTable
import ru.petkeeper.database.users_table.UsersTable

object ResponsesTable : Table("responses") {

    var idCounter = 0
    val responseId = ResponsesTable.integer("responseId")
    private val userEmail = ResponsesTable.varchar("userEmail", 50).references(UsersTable.email)
    private val orderId = ResponsesTable.integer("orderId").references(OrdersTable.orderId)
    private val body = ResponsesTable.text("body")
    private val status = ResponsesTable.bool("status") // true - accepted response

    fun insertResponse(responseDto: ResponseDto): ResponseDto {
        transaction {
            val newId = ResponsesTable.selectAll().count().toInt()
            idCounter = newId
            ResponsesTable.insert {
                it[responseId] = newId
                it[userEmail] = responseDto.userEmail
                it[orderId] = responseDto.orderId
                it[body] = responseDto.body
                it[status] = responseDto.status
            }
        }
        return responseDto
    }

    fun fetchResponse(id: Int): ResponseDto? {
        return try {
            transaction {
                val responseModel = ResponsesTable.select {
                    responseId.eq(id)
                }.single()
                ResponseDto(
                    userEmail = responseModel[userEmail],
                    orderId = responseModel[orderId],
                    body = responseModel[body],
                    status = responseModel[status]
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun checkResponseAlreadyCreated(userEmail: String, orderId: Int): Boolean {
        return try {
            transaction {
                val responseModel = ResponsesTable.select {
                    ResponsesTable.userEmail.eq(userEmail)
                    ResponsesTable.orderId.eq(orderId)
                }.single()
                println("responseModel is ${responseModel[ResponsesTable.userEmail]}")
                true
            }
        } catch (e: Exception) {
            false
        }
    }

    fun fetchResponsesByOrderId(orderId: Int): List<ResponseDto> {
        return try {
            val resList = mutableListOf<ResponseDto>()
            transaction {
                val responseModelList = ResponsesTable.select {
                    ResponsesTable.orderId.eq(orderId)
                }.toList()
                for (responseModel in responseModelList) {
                    resList.add(
                        ResponseDto(
                            userEmail = responseModel[userEmail],
                            orderId = responseModel[ResponsesTable.orderId],
                            body = responseModel[body],
                            status = responseModel[status]
                        )
                    )
                }
                resList
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}