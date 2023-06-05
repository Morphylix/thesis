package com.morphylix.android.petkeeper.domain.usecase.leave_comment

import com.morphylix.android.petkeeper.domain.PetKeeperRepository
import com.morphylix.android.petkeeper.domain.model.domain.Comment
import javax.inject.Inject

class PostCommentUseCase @Inject constructor(
    private val petKeeperRepository: PetKeeperRepository
) {

    suspend fun execute(comment: Comment) {
        petKeeperRepository.postComment(comment)
    }

}