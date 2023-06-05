package com.morphylix.android.petkeeper.presentation.create_order


import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.morphylix.android.petkeeper.databinding.FragmentChooseOrderTypeBinding
import com.morphylix.android.petkeeper.presentation.base.BaseFragment
import java.util.Calendar

class ChooseOrderTypeFragment :
    BaseFragment<FragmentChooseOrderTypeBinding>({ inflater, container ->
        FragmentChooseOrderTypeBinding.inflate(inflater, container, false)
    }) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            fullTimeCareCardView.setOnClickListener {
                val action =
                    ChooseOrderTypeFragmentDirections.actionChooseOrderTypeFragmentToCreateOrderFragment(
                        -1,
                        -1
                    )
                findNavController().navigate(action)
            }

            temporaryCareCardView.setOnClickListener {
                val datePickerBuilder = MaterialDatePicker.Builder.dateRangePicker()
                val now = Calendar.getInstance()
                datePickerBuilder.setSelection(
                    androidx.core.util.Pair(
                        now.timeInMillis,
                        now.timeInMillis
                    )
                )
                val picker = datePickerBuilder.build()
                picker.show(activity?.supportFragmentManager!!, picker.toString())
                picker.addOnPositiveButtonClickListener {
                    val action =
                        ChooseOrderTypeFragmentDirections.actionChooseOrderTypeFragmentToCreateOrderFragment(
                            it.first,
                            it.second
                        )
                    findNavController().navigate(action)
                }
            }
        }
    }

}