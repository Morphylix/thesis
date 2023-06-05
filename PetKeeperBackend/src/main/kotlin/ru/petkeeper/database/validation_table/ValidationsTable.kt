package ru.petkeeper.database.validation_table

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object ValidationsTable : Table("validations") {
    private val email = ValidationsTable.varchar("email", 25)
    private val code = ValidationsTable.integer("code")

    fun insertValidation(validationDto: ValidationDto) {
        transaction {
            ValidationsTable.insert {
                it[email] = validationDto.email
                it[code] = validationDto.code
            }
        }
    }

    fun replaceValidation(validationDto: ValidationDto) {
        transaction {
            ValidationsTable.update({ email.eq(validationDto.email) }) {
                it[code] = validationDto.code
            }
        }
    }

    fun fetchValidation(email: String): ValidationDto? {
        return try {
            transaction {
                val validationModel = ValidationsTable.select {
                    ValidationsTable.email.eq(email)
                }.single()

                ValidationDto(
                    validationModel[ValidationsTable.email],
                    validationModel[ValidationsTable.code]
                )
            }
        } catch (e: Exception) {
            null
        }
    }

}