package com.morphylix.android.petkeeper.presentation.base

import androidx.lifecycle.ViewModel
import com.morphylix.android.petkeeper.presentation.order_list.OrderListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class BaseViewModel: ViewModel() {

    protected val _state = MutableStateFlow<BaseState>(BaseState.Loading)
    val state: StateFlow<BaseState> get() = _state

    fun setLoadingState() {
        _state.value = BaseState.Loading
    }

}