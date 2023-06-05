package com.morphylix.android.petkeeper.presentation.comment_list

import androidx.lifecycle.viewModelScope
import com.morphylix.android.petkeeper.domain.usecase.comment_list.FetchUserCommentsUseCase
import com.morphylix.android.petkeeper.presentation.base.BaseState
import com.morphylix.android.petkeeper.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentListViewModel @Inject constructor(
    private val fetchUserCommentsUseCase: FetchUserCommentsUseCase
): BaseViewModel() {

    fun fetchUserComments(email: String) {
        viewModelScope.launch {
            try {
                _state.value = BaseState.Success(fetchUserCommentsUseCase.execute(email))
            } catch (e: Exception) {

            }
        }
    }

}