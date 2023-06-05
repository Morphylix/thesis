package com.morphylix.android.petkeeper.presentation.filter_settings

import com.morphylix.android.petkeeper.domain.model.domain.settings.FilterType
import com.morphylix.android.petkeeper.domain.model.domain.settings.SortType
import com.morphylix.android.petkeeper.domain.usecase.filter_settings.SaveFilterSettingsUseCase
import com.morphylix.android.petkeeper.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterSettingsViewModel @Inject constructor(
    private val saveFilterSettingsUseCase: SaveFilterSettingsUseCase
) : BaseViewModel() {

    fun saveFilterSettings(
        sortType: SortType?,
        filterType: FilterType?,
        lowerBoundAge: Int?,
        upperBoundAge: Int?,
        isTemp: Boolean?,
        isLitterBoxTrained: Boolean?,
        isVaccinated: Boolean?
    ) {
        saveFilterSettingsUseCase.execute(
            sortType,
            filterType,
            lowerBoundAge,
            upperBoundAge,
            isTemp,
            isLitterBoxTrained,
            isVaccinated
        )
    }

}