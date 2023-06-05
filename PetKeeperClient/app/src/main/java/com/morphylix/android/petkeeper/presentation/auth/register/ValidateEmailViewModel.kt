package com.morphylix.android.petkeeper.presentation.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morphylix.android.petkeeper.domain.PetKeeperRepository
import com.morphylix.android.petkeeper.domain.usecase.GetValidationCodeOnEmailUseCase
import com.morphylix.android.petkeeper.domain.usecase.SubmitValidationCodeUseCase
import com.morphylix.android.petkeeper.presentation.auth.ValidationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ValidateEmailViewModel
@Inject constructor(private val getValidationCodeOnEmailUseCase: GetValidationCodeOnEmailUseCase,
private val submitValidationCodeUseCase: SubmitValidationCodeUseCase) :
    ViewModel() {

    private val _validationState: MutableStateFlow<ValidationState> = MutableStateFlow(ValidationState.Loading)
    val validationState: StateFlow<ValidationState> get() = _validationState

    fun getValidationCodeOnEmail(email: String) {
        viewModelScope.launch {
            getValidationCodeOnEmailUseCase.execute(email)
        }
    }

    fun submitValidationCode(email: String, code: String) {
        viewModelScope.launch {
            submitValidationCodeUseCase.execute(email, code)
            _validationState.value = ValidationState.Success(true)
        }
    }

    fun setLoadingState() {
        _validationState.value = ValidationState.Loading
    }
}