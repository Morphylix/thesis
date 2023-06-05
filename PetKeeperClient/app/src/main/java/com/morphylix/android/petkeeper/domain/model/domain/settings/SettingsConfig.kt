package com.morphylix.android.petkeeper.domain.model.domain.settings

data class SettingsConfig(
    val sortBy: SortType? = null,
    val filterBy: FilterType? = null,
    val ageLowerBound: Int? = null,
    val ageUpperBound: Int? = null,
    val isTemporary: Boolean? = null,
    val isLitterBoxTrained: Boolean? = null,
    val isVaccinated: Boolean? = null
    )