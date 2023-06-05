package com.morphylix.android.petkeeper.domain.usecase.order_details

import com.morphylix.android.petkeeper.domain.PetKeeperRepository
import com.morphylix.android.petkeeper.domain.model.domain.Response
import javax.inject.Inject

class PostResponseUseCase @Inject constructor(
    private val petKeeperRepository: PetKeeperRepository
) {

    suspend fun execute(response: Response) {
        petKeeperRepository.postResponse(response)
    }

}