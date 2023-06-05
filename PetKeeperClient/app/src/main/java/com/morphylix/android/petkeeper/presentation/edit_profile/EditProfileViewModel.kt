package com.morphylix.android.petkeeper.presentation.edit_profile

import androidx.lifecycle.viewModelScope
import com.morphylix.android.petkeeper.domain.usecase.LoadUserInfoUseCase
import com.morphylix.android.petkeeper.presentation.base.BaseState
import com.morphylix.android.petkeeper.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val loadUserInfoUseCase: LoadUserInfoUseCase
): BaseViewModel() {

    fun loadUserInfo() {
        viewModelScope.launch {
            _state.value = BaseState.Success(loadUserInfoUseCase.execute())
        }
    }

}