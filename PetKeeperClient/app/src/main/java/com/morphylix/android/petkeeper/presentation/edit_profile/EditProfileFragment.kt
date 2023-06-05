package com.morphylix.android.petkeeper.presentation.edit_profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.morphylix.android.petkeeper.databinding.FragmentEditProfileBinding
import com.morphylix.android.petkeeper.domain.model.domain.User
import com.morphylix.android.petkeeper.presentation.base.BaseFragment
import com.morphylix.android.petkeeper.presentation.base.BaseState
import kotlinx.coroutines.launch

class EditProfileFragment: BaseFragment<FragmentEditProfileBinding>({ inflater, container ->
    FragmentEditProfileBinding.inflate(inflater, container, false)
}) {

    private val editProfileViewModel: EditProfileViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        editProfileViewModel.loadUserInfo()

        lifecycleScope.launchWhenStarted {
            editProfileViewModel.state.collect { state ->
                when (state) {
                    is BaseState.Loading -> {}
                    is BaseState.Success -> {
                        if (state.data is User) {
                            initEditTexts(state.data)
                        }
                    }
                    is BaseState.Error -> {}
                }
            }
        }

    }

    private fun initEditTexts(user: User) {
        with(binding) {
            nameEditText.setText(user.name)
            surnameEditText.setText(user.surname)
            phoneEditText.setText(user.phone)
        }
    }

}