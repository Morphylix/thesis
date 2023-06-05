package ru.petkeeper.database.token_table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object TokensTable: Table("tokens") {
    private val token = TokensTable.varchar("token", 50)
    private val email = TokensTable.varchar("email", 25)

    fun insertToken(tokenDto: TokenDto) {
        transaction {
            TokensTable.insert {
                it[token] = tokenDto.token
                it[email] = tokenDto.email
            }
        }
    }

    fun fetchToken(token: String): TokenDto? {
        return try {
            transaction {
                val tokenModel = TokensTable.select {
                    TokensTable.token.eq(token)
                }.single()

                TokenDto(
                    tokenModel[TokensTable.token],
                    tokenModel[TokensTable.email]
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun fetchEmail(token: String): String?{
        return try {
            transaction {
                val tokenModel = TokensTable.select {
                    TokensTable.token.eq(token)
                }.single()

                tokenModel[email]
            }
        } catch (e: Exception) {
            null
        }
    }
}