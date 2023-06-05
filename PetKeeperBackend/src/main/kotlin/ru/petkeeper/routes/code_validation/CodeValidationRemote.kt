package ru.petkeeper.routes.code_validation

data class CodeValidationReceiveRemote(
    val email: String,
    val code: Int
)

data class CodeValidationResponseRemote(
    val isValidated: Boolean
)