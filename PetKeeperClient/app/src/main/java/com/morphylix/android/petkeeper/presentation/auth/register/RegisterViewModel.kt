package com.morphylix.android.petkeeper.presentation.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morphylix.android.petkeeper.domain.usecase.RegisterUseCase
import com.morphylix.android.petkeeper.domain.usecase.SaveTokenUseCase
import com.morphylix.android.petkeeper.presentation.auth.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel
@Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val saveTokenUseCase: SaveTokenUseCase
) : ViewModel() {

    private val _authState: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Loading)
    val authState: StateFlow<AuthState> get() = _authState

    fun registerAndFetchToken(login: String, password: String, name: String, surname: String) {
        viewModelScope.launch {
            try {
                val token = registerUseCase.execute(login, password, name, surname)
                saveTokenUseCase.execute(token)
                _authState.value = AuthState.Success(token)
            } catch (e: HttpException) {
                _authState.value = AuthState.Error(e)
            }
        }
    }

    fun setLoadingState() {
        _authState.value = AuthState.Loading
    }

}