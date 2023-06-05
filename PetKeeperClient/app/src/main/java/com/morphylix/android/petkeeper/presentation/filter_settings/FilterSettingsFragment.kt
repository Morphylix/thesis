package com.morphylix.android.petkeeper.presentation.filter_settings

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SimpleAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.google.android.material.slider.Slider.OnSliderTouchListener
import com.morphylix.android.petkeeper.R
import com.morphylix.android.petkeeper.data.workers.NotificationWorker
import com.morphylix.android.petkeeper.databinding.FragmentFilterSettingsBinding
import com.morphylix.android.petkeeper.domain.model.domain.settings.FilterType
import com.morphylix.android.petkeeper.domain.model.domain.settings.SortType
import com.morphylix.android.petkeeper.presentation.base.BaseFragment
import com.morphylix.android.petkeeper.util.handleHintData
import com.morphylix.android.petkeeper.util.provideOnItemClickListener
import java.util.concurrent.TimeUnit

private const val TAG = "FilterSettingsFragment"

class FilterSettingsFragment : BaseFragment<FragmentFilterSettingsBinding>({ inflater, container ->
    FragmentFilterSettingsBinding.inflate(inflater, container, false)
}) {

    private var lastChosenPetType: String? = null
    private var lastChosenPetBreed: String? = null
    private val filterSettingsViewModel: FilterSettingsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            handleHintData(
                resources.getStringArray(R.array.pet_type_array).toList(),
                requireContext(),
                choosePetTypeEditText
            ) {
                it
            }
            ageSlider.setValues(0.0f, 100.0f)
            ageSlider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: RangeSlider) {

                }

                override fun onStopTrackingTouch(slider: RangeSlider) {
                    ageTextView.text = getString(R.string.age, slider.values[0].toInt(), slider.values[1].toInt())
                }

            })
            provideOnPetTypeItemClickListener()
            ageTextView.text = getString(R.string.age, 1, 100)
            clearFilterTextView.setOnClickListener {
                WorkManager.getInstance(requireContext()).cancelAllWork()
                filterSettingsViewModel.saveFilterSettings(null, null, null, null, null, null, null)
                findNavController().popBackStack()
            }
            submitButton.setOnClickListener {
                val sortType = when (sortRadioGroup.checkedRadioButtonId) {
                    createdDateOldestRadioButton.id -> {
                        SortType.CREATED_DATE_OLDEST
                    }
                    createdDateNewestRadioButton.id -> {
                        SortType.CREATED_DATE_NEWEST
                    }
                    else -> {
                        SortType.USER_RATING_DESC
                    }
                }
                val ageLowerBound = ageSlider.valueFrom.toInt()
                val ageUpperBound = ageSlider.valueTo.toInt()
                val isTemporary = getBooleanRadioGroupValue(tempRadioGroup)
                val isLitterBoxTrained = getBooleanRadioGroupValue(litterBoxRadioGroup)
                val isVaccinated = getBooleanRadioGroupValue(vaccinatedRadioGroup)

                val filterType = FilterType(lastChosenPetType, lastChosenPetBreed)
                filterSettingsViewModel.saveFilterSettings(
                    sortType,
                    filterType,
                    ageLowerBound,
                    ageUpperBound,
                    isTemporary,
                    isLitterBoxTrained,
                    isVaccinated
                )

                if (notifyCheckbox.isChecked) {
                    Log.i(TAG, "Activating worker")
                    setupNotificationWork()
                }

                val action =
                    FilterSettingsFragmentDirections.actionFilterSettingsFragmentToOrderListFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun provideOnPetTypeItemClickListener() {
        with(binding) {
            choosePetTypeEditText.setOnItemClickListener { adapterView, view, i, l ->
                provideOnItemClickListener<String>(
                    resources.getStringArray(R.array.pet_type_array).toList(),
                    adapterView.getItemAtPosition(i) as String,
                    i,
                    mapData = { list -> list.map { (it as String) } },
                    doAfterItemWasFound = { lastChosenPetType = it }
                )
                choosePetTypeInputLayout.error = null
                choosePetTypeInputLayout.isErrorEnabled = false
            }
        }
    }

    private fun setupNotificationWork() {
        WorkManager.getInstance(requireContext()).cancelAllWork()
        val repeatingRequest =
            PeriodicWorkRequestBuilder<NotificationWorker>(8, TimeUnit.SECONDS).build()

        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            "test",
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }

    private fun getRadioGroupValue(radioGroup: RadioGroup): String {
        return try {
            requireActivity().findViewById<RadioButton>(radioGroup.checkedRadioButtonId).text.toString()
        } catch (e: Exception) {
            return ""
        }
    }

    private fun getBooleanRadioGroupValue(radioGroup: RadioGroup): Boolean? {
        return when (getRadioGroupValue(radioGroup).lowercase()) {
            requireContext().getString(R.string.yes).lowercase() -> true
            requireContext().getString(R.string.no).lowercase() -> false
            else -> null

        }
    }

}