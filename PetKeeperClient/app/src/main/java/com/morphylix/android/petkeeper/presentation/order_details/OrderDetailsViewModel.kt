package com.morphylix.android.petkeeper.presentation.order_details

import androidx.lifecycle.viewModelScope
import com.morphylix.android.petkeeper.domain.model.domain.Response
import com.morphylix.android.petkeeper.domain.usecase.LoadUserInfoUseCase
import com.morphylix.android.petkeeper.domain.usecase.order_details.CloseOrderUseCase
import com.morphylix.android.petkeeper.domain.usecase.order_details.FetchOrderByIdUseCase
import com.morphylix.android.petkeeper.domain.usecase.order_details.FetchOrderResponsesUseCase
import com.morphylix.android.petkeeper.domain.usecase.order_details.PostResponseUseCase
import com.morphylix.android.petkeeper.presentation.base.BaseState
import com.morphylix.android.petkeeper.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    private val fetchOrderByIdUseCase: FetchOrderByIdUseCase,
    private val loadUserInfoUseCase: LoadUserInfoUseCase,
    private val fetchOrderResponsesUseCase: FetchOrderResponsesUseCase,
    private val postResponseUseCase: PostResponseUseCase,
    private val closeOrderUseCase: CloseOrderUseCase
): BaseViewModel() {

    fun fetchOrderById(orderId: Int) {
        viewModelScope.launch {
            _state.value = BaseState.Success(fetchOrderByIdUseCase.execute(orderId))
        }
    }

    fun closeOrder(orderId: Int) {
        viewModelScope.launch {
            try {
                closeOrderUseCase.execute(orderId)
            } catch (e: Exception) {

            }
        }
    }

    fun fetchUserInfo() {
        viewModelScope.launch {
            _state.value = BaseState.Success(loadUserInfoUseCase.execute())
        }
    }

    fun fetchOrderResponses(orderId: Int) {
        viewModelScope.launch {
            _state.value = BaseState.Success(fetchOrderResponsesUseCase.execute(orderId))
        }
    }

    fun postResponse(response: Response) {
        viewModelScope.launch {
            _state.value = BaseState.Success(postResponseUseCase.execute(response))
        }
    }
}