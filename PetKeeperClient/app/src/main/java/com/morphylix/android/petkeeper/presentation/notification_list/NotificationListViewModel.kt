package com.morphylix.android.petkeeper.presentation.notification_list

import androidx.lifecycle.viewModelScope
import com.morphylix.android.petkeeper.domain.usecase.LoadUserInfoUseCase
import com.morphylix.android.petkeeper.domain.usecase.notification_list.FetchUserNotificationsUseCase
import com.morphylix.android.petkeeper.presentation.base.BaseState
import com.morphylix.android.petkeeper.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationListViewModel @Inject constructor(
    private val loadUserInfoUseCase: LoadUserInfoUseCase,
    private val fetchUserNotificationsUseCase: FetchUserNotificationsUseCase
): BaseViewModel() {


    fun fetchUserNotifications() {
        viewModelScope.launch {
            val userInfo = loadUserInfoUseCase.execute()
            _state.value = BaseState.Success(fetchUserNotificationsUseCase.execute(userInfo.email))
        }
    }

}