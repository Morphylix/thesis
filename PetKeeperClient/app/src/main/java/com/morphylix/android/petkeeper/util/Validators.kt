package com.morphylix.android.petkeeper.util

object Validators {

    fun isEmailCorrect(email: String): Boolean {
        return (email.contains(Regex("[a-zA-Z0-9.]+@[a-zA-Z]+.[a-zA-Z]+")))
    }

}
