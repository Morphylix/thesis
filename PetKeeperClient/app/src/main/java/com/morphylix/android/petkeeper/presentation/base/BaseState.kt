package com.morphylix.android.petkeeper.presentation.base

sealed class BaseState {
    object Loading: BaseState()

    class Success(val data: Any?): BaseState()

    class Error(val e: Exception): BaseState()
}
