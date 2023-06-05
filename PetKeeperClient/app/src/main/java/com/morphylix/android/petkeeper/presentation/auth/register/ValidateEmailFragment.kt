package com.morphylix.android.petkeeper.presentation.auth.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.morphylix.android.petkeeper.databinding.FragmentValidateEmailBinding
import com.morphylix.android.petkeeper.presentation.auth.ValidationState
import com.morphylix.android.petkeeper.presentation.base.BaseFragment
import com.morphylix.android.petkeeper.util.PIN_LENGTH
import com.morphylix.android.petkeeper.util.Validators
import kotlinx.coroutines.launch

class ValidateEmailFragment : BaseFragment<FragmentValidateEmailBinding>({ inflater, container ->
    FragmentValidateEmailBinding.inflate(inflater, container, false)
}) {

    private val validateEmailViewModel: ValidateEmailViewModel by activityViewModels()
    private var email: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        provideListeners()

        lifecycleScope.launchWhenStarted {
            validateEmailViewModel.validationState.collect { state ->

                when (state) {
                    is ValidationState.Loading -> {

                    }
                    is ValidationState.Success -> {
                        if (state.isValidated) {
                            validateEmailViewModel.setLoadingState()
                            val action =
                                ValidateEmailFragmentDirections.actionValidateEmailFragmentToRegisterFragment(email!!)
                            findNavController().navigate(action)
                        }
                    }
                    is ValidationState.Error -> {

                    }
                }

            }
        }
    }

    private fun provideListeners() {
        provideSubmitEmailButtonListener()
        provideSubmitCodeListener()
    }

    private fun provideSubmitEmailButtonListener() {
        with(binding) {
            submitEmailButton.setOnClickListener {

                email = binding.emailEditText.text.toString()

                if (email!!.isEmpty()) {
                    emailInputLayout.error = "Email is empty"
                } else if (!Validators.isEmailCorrect(email!!)) {
                    emailInputLayout.error = "Wrong email format"
                } else {
                    validateEmailViewModel.getValidationCodeOnEmail(email!!)
                    codeEditText.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun provideSubmitCodeListener() {
        with(binding) {
            codeEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0?.length == PIN_LENGTH) {
                        validateEmailViewModel.submitValidationCode(email!!, p0.toString())
                    }
                }
            })
        }
    }
}