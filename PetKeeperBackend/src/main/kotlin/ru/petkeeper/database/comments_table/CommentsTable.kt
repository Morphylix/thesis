package ru.petkeeper.database.comments_table

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.petkeeper.database.orders_table.OrderDto
import ru.petkeeper.database.orders_table.OrdersTable
import ru.petkeeper.database.pets_table.PetsTable
import ru.petkeeper.database.responses_table.ResponseDto
import ru.petkeeper.database.responses_table.ResponsesTable
import ru.petkeeper.database.responses_table.ResponsesTable.references
import ru.petkeeper.database.users_table.UsersTable
import ru.petkeeper.util.converters.convertDateTimeToDate

object CommentsTable: Table("comments") {

    var idCounter = 0
    val commentId = CommentsTable.integer("commentId")
    private val senderEmail = CommentsTable.varchar("senderEmail", 50).references(UsersTable.email)
    private val receiverEmail = CommentsTable.varchar("receiverEmail", 50).references(UsersTable.email)
    private val body = CommentsTable.text("body")
    private val rating = CommentsTable.float("rating")

    fun insertComment(commentDto: CommentDto): CommentDto {
        transaction {
            val newId = CommentsTable.selectAll().count().toInt()
            CommentsTable.idCounter = newId
            CommentsTable.insert {
                it[commentId] = newId
                it[senderEmail] = commentDto.senderEmail
                it[receiverEmail] = commentDto.receiverEmail
                it[body] = commentDto.body
                it[rating] = commentDto.rating.toFloat()
            }
        }
        return commentDto
    }

    fun fetchComment(id: Int): CommentDto? {
        return try {
            transaction {
                val commentModel = CommentsTable.select {
                    CommentsTable.commentId.eq(id)
                }.single()
                CommentDto(
                    id = commentModel[commentId],
                    senderEmail = commentModel[receiverEmail],
                    receiverEmail = commentModel[receiverEmail],
                    body = commentModel[body],
                    rating = commentModel[rating].toDouble()
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun fetchCommentsByEmail(email: String): List<CommentDto> {
        return try {
            val resList = mutableListOf<CommentDto>()
            transaction {
                val commentModelList = CommentsTable.select {
                    CommentsTable.receiverEmail.eq(email)
                }.toList()
                for (commentModel in commentModelList) {
                    resList.add(
                        CommentDto(
                            id = commentModel[commentId],
                            senderEmail = commentModel[senderEmail],
                            receiverEmail = commentModel[receiverEmail],
                            body = commentModel[body],
                            rating = commentModel[rating].toDouble()
                            )
                    )
                }
            }
            resList
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun checkCommentAlreadyCreated(senderEmail: String, receiverEmail: String): Boolean {
        return try {
            val resList = mutableListOf<CommentDto>()
            transaction {
                val commentModelList = CommentsTable.select {
                    CommentsTable.receiverEmail.eq(receiverEmail)
                }.toList()
                commentModelList.forEach {  commentModel ->
                    if (commentModel[CommentsTable.senderEmail] == senderEmail) return@transaction true
                }
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}