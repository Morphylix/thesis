package com.morphylix.android.petkeeper.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morphylix.android.petkeeper.domain.usecase.AutoLoginUseCase
import com.morphylix.android.petkeeper.domain.usecase.LoginUseCase
import com.morphylix.android.petkeeper.domain.usecase.SaveTokenUseCase
import com.morphylix.android.petkeeper.domain.usecase.login.LoginWithGoogleUseCase
import com.morphylix.android.petkeeper.presentation.auth.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

private const val TAG = "LoginViewModel"

@HiltViewModel
class LoginViewModel
@Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val autoLoginUseCase: AutoLoginUseCase,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase
) : ViewModel() {

    private val _authState: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Loading)
    val authState: StateFlow<AuthState> get() = _authState

    fun loginAndFetchToken(email: String, password: String) {
        viewModelScope.launch {
            try {
                val token = loginUseCase.execute(email, password)
                saveTokenUseCase.execute(token)
                _authState.value = AuthState.Success(token)
            } catch (e: HttpException) {
                _authState.value = AuthState.Error(e)
            }
        }
    }

    fun loginWithGoogle(email: String, password: String, name: String) {
        viewModelScope.launch {
            try {
                val token = loginWithGoogleUseCase.execute(email, password, name)
                saveTokenUseCase.execute(token)
                _authState.value = AuthState.Success(token)
            } catch (e: HttpException) {
                _authState.value = AuthState.Error(e)
            }
        }
    }

    fun autoLoginWithToken() {
        viewModelScope.launch {
            if (autoLoginUseCase.execute()) {
                _authState.value = AuthState.Success("")
            }
        }
    }

    fun setLoadingState() {
        _authState.value = AuthState.Loading
    }
}