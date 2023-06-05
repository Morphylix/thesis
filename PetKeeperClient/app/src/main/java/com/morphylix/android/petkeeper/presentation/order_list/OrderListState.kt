package com.morphylix.android.petkeeper.presentation.order_list

import com.morphylix.android.petkeeper.domain.model.domain.User

sealed class OrderListState {
    object Loading: OrderListState()

    class Success<T>(val data: T): OrderListState()

    class Error(val e: Exception): OrderListState()
}
