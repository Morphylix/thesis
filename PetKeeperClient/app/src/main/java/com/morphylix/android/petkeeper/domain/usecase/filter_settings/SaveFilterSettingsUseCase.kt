package com.morphylix.android.petkeeper.domain.usecase.filter_settings

import com.morphylix.android.petkeeper.domain.PetKeeperRepository
import com.morphylix.android.petkeeper.domain.model.domain.settings.FilterType
import com.morphylix.android.petkeeper.domain.model.domain.settings.SettingsConfig
import com.morphylix.android.petkeeper.domain.model.domain.settings.SortType
import javax.inject.Inject

class SaveFilterSettingsUseCase @Inject constructor(
    private val petKeeperRepository: PetKeeperRepository
) {

    fun execute(
        sortType: SortType?,
        filterType: FilterType?,
        lowerBoundAge: Int?,
        upperBoundAge: Int?,
        isTemp: Boolean?,
        isLitterBoxTrained: Boolean?,
        isVaccinated: Boolean?
    ) {
        petKeeperRepository.saveSettingsConfig(
            SettingsConfig(
                sortType,
                filterType,
                lowerBoundAge,
                upperBoundAge,
                isTemp,
                isLitterBoxTrained,
                isVaccinated
            )
        )
    }

}