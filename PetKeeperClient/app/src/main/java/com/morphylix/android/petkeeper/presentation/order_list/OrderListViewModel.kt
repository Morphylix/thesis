package com.morphylix.android.petkeeper.presentation.order_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morphylix.android.petkeeper.domain.model.domain.Order
import com.morphylix.android.petkeeper.domain.usecase.FetchOrdersUseCase
import com.morphylix.android.petkeeper.domain.usecase.LoadUserInfoUseCase
import com.morphylix.android.petkeeper.domain.usecase.order_list.GetSettingsConfigUseCase
import com.morphylix.android.petkeeper.presentation.base.BaseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderListViewModel @Inject constructor(
    private val loadUserInfoUseCase: LoadUserInfoUseCase,
    private val fetchOrdersUseCase: FetchOrdersUseCase,
    private val getSettingsConfigUseCase: GetSettingsConfigUseCase
    ): ViewModel() {

    private val _state = MutableStateFlow<OrderListState>(OrderListState.Loading)
    val state: StateFlow<OrderListState> get() = _state
    val orderList: MutableList<Order> = mutableListOf()

    fun loadInfo() {
        loadUserInfo()
        loadOrdersInfo()
    }

    fun getSettingsConfig() {
        _state.value = OrderListState.Success(getSettingsConfigUseCase.execute())
    }

    private fun loadUserInfo() {
        viewModelScope.launch {
            val user = loadUserInfoUseCase.execute()
            _state.value = OrderListState.Success(user)
        }
    }

    private fun loadOrdersInfo() {
        viewModelScope.launch {
            _state.value = OrderListState.Success(fetchOrdersUseCase.execute())
        }
    }

    fun setLoadingState() {
        _state.value = OrderListState.Loading
    }
}