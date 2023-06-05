package com.morphylix.android.petkeeper.domain.model.network.pet

import com.morphylix.android.petkeeper.domain.model.domain.Pet
import com.morphylix.android.petkeeper.util.EntityMapper
import javax.inject.Inject

class PetNetworkMapper @Inject constructor(): EntityMapper<PetNetworkEntity, Pet>() {
    override fun mapFromEntity(entity: PetNetworkEntity): Pet {
        return Pet(
            id = entity.id,
            name = entity.name,
            type = entity.type,
            breed = entity.breed,
            gender = entity.gender,
            years = entity.years,
            months = entity.months,
            weight = entity.weight,
            isFriendlyToPeople = entity.isFriendlyToPeople,
            isFriendlyToChildren = entity.isFriendlyToChildren,
            isFriendlyToCats = entity.isFriendlyToCats,
            isFriendlyToDogs = entity.isFriendlyToDogs,
            isFriendlyToOtherAnimals = entity.isFriendlyToOtherAnimals,
            canLiveWithDogs = entity.canLiveWithDogs,
            canLiveWithCats = entity.canLiveWithCats,
            canLiveWithOtherAnimals = entity.canLiveWithOtherAnimals,
            isLitterBoxTrained = entity.isLitterBoxTrained,
            marksThings = entity.marksThings,
            chewsThings = entity.chewsThings,
            isVaccinated = entity.isVaccinated,
            isSterilised = entity.isSterilised,
            diseases = entity.diseases,
            canStayAlone = entity.canStayAlone,
            foodToEat = entity.foodToEat,
            foodToAvoid = entity.foodToAvoid,
            howOftenToFeed = entity.howOftenToFeed,
            needsWalking = entity.needsWalking,
            howOftenToWalk = entity.howOftenToWalk,
            warningsWhileWalking = entity.warningsWhileWalking,
            needsPlaying = entity.needsPlaying,
            howToPlay = entity.howToPlay,
            howToWash = entity.howToWash,
            otherRecs = entity.otherRecs,
            picture = entity.picture





        )
    }

    override fun mapToEntity(model: Pet): PetNetworkEntity {
        TODO("Not yet implemented")
    }


}