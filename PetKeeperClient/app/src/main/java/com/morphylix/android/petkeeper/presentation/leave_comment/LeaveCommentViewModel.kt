package com.morphylix.android.petkeeper.presentation.leave_comment

import androidx.lifecycle.viewModelScope
import com.morphylix.android.petkeeper.domain.model.domain.Comment
import com.morphylix.android.petkeeper.domain.usecase.LoadUserInfoUseCase
import com.morphylix.android.petkeeper.domain.usecase.leave_comment.PostCommentUseCase
import com.morphylix.android.petkeeper.domain.usecase.user_profile.FetchUserByEmailUseCase
import com.morphylix.android.petkeeper.presentation.base.BaseState
import com.morphylix.android.petkeeper.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaveCommentViewModel @Inject constructor(
    private val postCommentUseCase: PostCommentUseCase,
    private val loadUserInfoUseCase: LoadUserInfoUseCase
): BaseViewModel() {

    fun postComment(comment: Comment) {
        viewModelScope.launch {
            postCommentUseCase.execute(comment)
        }
    }

    fun loadUserInfo() {
        viewModelScope.launch {
            _state.value = BaseState.Success(loadUserInfoUseCase.execute())
        }
    }

}