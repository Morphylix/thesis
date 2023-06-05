package com.morphylix.android.petkeeper.presentation.auth

sealed class ValidationState {
    object Loading: ValidationState()

    class Success(val isValidated: Boolean): ValidationState()

    class Error(val e: Exception): ValidationState()
}
