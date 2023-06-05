package com.morphylix.android.petkeeper.domain.usecase.create_order

import com.morphylix.android.petkeeper.domain.PetKeeperRepository
import java.io.InputStream
import javax.inject.Inject

class PostPetImageUseCase @Inject constructor(
    private val petKeeperRepository: PetKeeperRepository
) {

    suspend fun execute(inputStream: InputStream, petId: Int) {
        petKeeperRepository.uploadPetImage(inputStream, petId)
    }

}