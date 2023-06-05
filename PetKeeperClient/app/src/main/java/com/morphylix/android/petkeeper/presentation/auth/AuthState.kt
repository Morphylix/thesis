package com.morphylix.android.petkeeper.presentation.auth

sealed class AuthState {

    object Loading: AuthState()

    class Success(val token: String): AuthState()

    class Error(val e: Exception): AuthState()
}
