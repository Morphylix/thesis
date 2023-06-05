package ru.petkeeper.routes.email_validation

data class EmailValidationReceiveRemote(
    val email: String
)

data class EmailValidationResponseRemote(
    val isValidated: Boolean
)