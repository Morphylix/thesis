package ru.petkeeper.routes.load_orders

data class SettingsConfig(
    val sortBy: SortType?,
    val filterBy: FilterType?
    )