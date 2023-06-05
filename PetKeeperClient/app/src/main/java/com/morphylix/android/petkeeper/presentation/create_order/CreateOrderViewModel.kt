package com.morphylix.android.petkeeper.presentation.create_order

import androidx.lifecycle.viewModelScope
import com.morphylix.android.petkeeper.domain.model.domain.Order
import com.morphylix.android.petkeeper.domain.usecase.create_order.PostOrderUseCase
import com.morphylix.android.petkeeper.domain.usecase.create_order.PostPetImageUseCase
import com.morphylix.android.petkeeper.presentation.base.BaseState
import com.morphylix.android.petkeeper.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.InputStream
import java.security.PrivateKey
import javax.inject.Inject

@HiltViewModel
class CreateOrderViewModel @Inject constructor(
    private val postOrderUseCase: PostOrderUseCase,
    private val postPetImageUseCase: PostPetImageUseCase
): BaseViewModel() {

    fun postOrder(order: Order) {
        viewModelScope.launch {
            _state.value = BaseState.Success(postOrderUseCase.execute(order))
        }
    }

    fun postPetImage(inputStream: InputStream, petId: Int) {
        viewModelScope.launch {
            postPetImageUseCase.execute(inputStream, petId)
        }
    }

}