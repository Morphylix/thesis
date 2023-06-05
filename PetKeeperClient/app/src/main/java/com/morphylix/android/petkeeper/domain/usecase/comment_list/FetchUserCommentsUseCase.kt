package com.morphylix.android.petkeeper.domain.usecase.comment_list

import com.morphylix.android.petkeeper.domain.PetKeeperRepository
import com.morphylix.android.petkeeper.domain.model.domain.Comment
import javax.inject.Inject

class FetchUserCommentsUseCase @Inject constructor(
    private val petKeeperRepository: PetKeeperRepository
) {

    suspend fun execute(email: String): List<Comment> {
        return petKeeperRepository.fetchUserComments(email)
    }

}