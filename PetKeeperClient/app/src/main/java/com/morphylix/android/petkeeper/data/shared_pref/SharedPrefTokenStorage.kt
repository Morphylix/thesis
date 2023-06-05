package com.morphylix.android.petkeeper.data.shared_pref

import android.content.Context
import android.util.Log
import com.morphylix.android.petkeeper.domain.model.domain.Order
import com.morphylix.android.petkeeper.domain.model.domain.settings.FilterType
import com.morphylix.android.petkeeper.domain.model.domain.settings.SettingsConfig
import com.morphylix.android.petkeeper.domain.model.domain.settings.SortType
import javax.inject.Inject

private const val SHARED_PREF_NAME = "shared_pref_name"
private const val TOKEN_KEY = "token_key"
private const val SORT_KEY = "sort_key"
private const val ORDER_KEY = "order_key"
private const val PET_TYPE_KEY = "pet_type_key"
private const val PET_BREED_KEY = "pet_breed_key"
private const val AGE_LB_KEY = "age_lb_key"
private const val AGE_UB_KEY = "age_ub_key"
private const val IS_TEMP_KEY = "is_temp_key"
private const val IS_LITTER_BOX_TRAINED_KEY = "is_litterbox_trained_key"
private const val IS_VACCINATED_KEY = "is_vaccinated_key"

private const val TAG = "SharedPrefTokenStorage"

class SharedPrefTokenStorage
@Inject constructor(context: Context) {

    private val sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        sharedPref.edit().putString(TOKEN_KEY, token).apply()
    }

    fun loadToken(): String? {
        //sharedPref.edit().clear().apply() // TO RESET SHAREDPREF TOKEN
        return sharedPref.getString(TOKEN_KEY, null)
    }

    fun saveSettingsConfig(settingsConfig: SettingsConfig) {
        Log.i(TAG, "saving settings config sort by is ${settingsConfig.sortBy?.name}")
        sharedPref.edit().putString(SORT_KEY, settingsConfig.sortBy?.name).apply()
        sharedPref.edit().putString(PET_BREED_KEY, settingsConfig.filterBy?.petBreed).apply()
        sharedPref.edit().putString(PET_TYPE_KEY, settingsConfig.filterBy?.petType).apply()
        if (settingsConfig.ageLowerBound != null) sharedPref.edit()
            .putInt(AGE_LB_KEY, settingsConfig.ageLowerBound).apply()
        if (settingsConfig.ageUpperBound != null) sharedPref.edit()
            .putInt(AGE_UB_KEY, settingsConfig.ageUpperBound).apply()
        if (settingsConfig.isTemporary != null) sharedPref.edit()
            .putBoolean(IS_TEMP_KEY, settingsConfig.isTemporary).apply()
        if (settingsConfig.isLitterBoxTrained != null) sharedPref.edit().putBoolean(
            IS_LITTER_BOX_TRAINED_KEY, settingsConfig.isLitterBoxTrained
        ).apply()
        if (settingsConfig.isVaccinated != null) sharedPref.edit()
            .putBoolean(IS_VACCINATED_KEY, settingsConfig.isVaccinated).apply()
    }

    fun loadSettingsConfig(): SettingsConfig {
        val sortBy = when (sharedPref.getString(SORT_KEY, null)) {
            SortType.USER_RATING_DESC.name -> {
                SortType.USER_RATING_DESC
            }
            SortType.USER_RATING_ASC.name -> {
                SortType.USER_RATING_ASC
            }
            SortType.CREATED_DATE_OLDEST.name -> {
                SortType.CREATED_DATE_OLDEST
            }
            SortType.CREATED_DATE_NEWEST.name -> {
                SortType.CREATED_DATE_NEWEST
            }
            else -> null
        }
        Log.i(TAG, "loading settings config sort by is ${sortBy?.name}")
        return SettingsConfig(
            sortBy, FilterType(
                sharedPref.getString(PET_TYPE_KEY, null), sharedPref.getString(
                    PET_BREED_KEY, null
                )
            ),
            if (sharedPref.getInt(AGE_LB_KEY, -1) == -1) null else sharedPref.getInt(AGE_LB_KEY, -1),
            if (sharedPref.getInt(AGE_UB_KEY, -1) == -1) null else sharedPref.getInt(AGE_UB_KEY, -1),
            sharedPref.getBoolean(IS_TEMP_KEY, false),
            sharedPref.getBoolean(IS_LITTER_BOX_TRAINED_KEY, false),
            sharedPref.getBoolean(IS_VACCINATED_KEY, false)
        )
    }

    fun saveLastOrderString(order: Order) {
        sharedPref.edit().putString(ORDER_KEY, order.toString()).apply()
    }

    fun getLastOrderString(): String {
        return sharedPref.getString(ORDER_KEY, "") ?: ""
    }
}