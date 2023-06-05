package com.morphylix.android.petkeeper.presentation.user_profile

import androidx.lifecycle.viewModelScope
import com.morphylix.android.petkeeper.domain.usecase.LoadUserInfoUseCase
import com.morphylix.android.petkeeper.domain.usecase.user_profile.FetchOrdersByEmailUseCase
import com.morphylix.android.petkeeper.domain.usecase.user_profile.FetchUserByEmailUseCase
import com.morphylix.android.petkeeper.presentation.base.BaseState
import com.morphylix.android.petkeeper.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val loadUserInfoUseCase: LoadUserInfoUseCase,
    private val loadOrdersByEmailUseCase: FetchOrdersByEmailUseCase,
    private val fetchUserByEmailUseCase: FetchUserByEmailUseCase
): BaseViewModel() {

    fun fetchUserInfo() {
        viewModelScope.launch {
            _state.value = BaseState.Success(loadUserInfoUseCase.execute())
        }
    }

    fun fetchUserInfoByEmail(email: String) {
        viewModelScope.launch {
            _state.value = BaseState.Success(fetchUserByEmailUseCase.execute(email))
        }
    }

    fun fetchUserOrders(email: String, orderType: OrderType) {
        viewModelScope.launch {
            _state.value = BaseState.Success(loadOrdersByEmailUseCase.execute(email, orderType))
        }
    }

}