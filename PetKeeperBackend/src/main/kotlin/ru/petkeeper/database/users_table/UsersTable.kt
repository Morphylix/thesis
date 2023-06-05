package ru.petkeeper.database.users_table

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.petkeeper.database.pets_table.PetsTable

object UsersTable: Table("users") {
    val email = UsersTable.varchar("email", 25)
    private val password = UsersTable.varchar("password", 256)
    private val name = UsersTable.varchar("name", 25)
    private val surname = UsersTable.varchar("surname", 25)
    private val phone = UsersTable.varchar("phone", 25)
    private val rating = UsersTable.double("rating")
    private val totalVotes = UsersTable.integer("totalVotes")

    fun insertUser(usersDto: UsersDto) {
        transaction {
            UsersTable.insert {
                it[UsersTable.email] = usersDto.email
                it[UsersTable.password] = usersDto.password
                it[UsersTable.name] = usersDto.name
                it[UsersTable.surname] = usersDto.surname
                it[UsersTable.phone] = usersDto.phone
                it[UsersTable.rating] = usersDto.rating
                it[UsersTable.totalVotes] = usersDto.totalVotes
            }
        }
    }

    fun updateRating(rating: Double, userEmail: String) { // TODO: ADD REVIEWS
        try {
            transaction {
                val model = UsersTable.select {
                    UsersTable.email eq userEmail
                }.single()
                var totalVotes = model[totalVotes]
                UsersTable.update ({ UsersTable.email eq userEmail }) {
                    val user = fetchUser(userEmail)
                    it[UsersTable.rating] = (((user?.rating ?: 0.0) * (user?.totalVotes ?: 0)) + rating) / ((user?.totalVotes ?: 0) + 1)
                    totalVotes++
                    it[UsersTable.totalVotes] = totalVotes
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    fun fetchUser(email: String): UsersDto? {
        return try {
            transaction {
                val userModel = UsersTable.select {
                    UsersTable.email.eq(email)
                }.single()
                println("${userModel[UsersTable.rating]} stars for user $email")

                UsersDto(
                    userModel[UsersTable.email],
                    userModel[UsersTable.password],
                    userModel[UsersTable.name],
                    userModel[UsersTable.surname],
                    userModel[UsersTable.phone],
                    userModel[UsersTable.rating],
                    userModel[UsersTable.totalVotes]
                )
            }
        } catch (e: Exception) {
            null
        }
    }

}