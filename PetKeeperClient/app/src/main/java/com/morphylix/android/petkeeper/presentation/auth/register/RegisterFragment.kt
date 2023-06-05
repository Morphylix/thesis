package com.morphylix.android.petkeeper.presentation.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.morphylix.android.petkeeper.databinding.FragmentRegisterBinding
import com.morphylix.android.petkeeper.presentation.auth.AuthState
import com.morphylix.android.petkeeper.presentation.base.BaseFragment
import kotlinx.coroutines.launch

class RegisterFragment : BaseFragment<FragmentRegisterBinding>({ inflater, container ->
    FragmentRegisterBinding.inflate(inflater, container, false)
}) {

    private val registerFragmentViewModel: RegisterViewModel by activityViewModels()
    private val args: RegisterFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerButton.setOnClickListener {
            with(binding) {
                val name = nameEditText.text.toString()
                val surname = surnameEditText.text.toString()
                val password = passwordEditText.text.toString()
                val repeatPassword = repeatPasswordEditText.text.toString()
                val email = args.email


                if (password != repeatPassword) {
                    repeatPasswordInputLayout.error = "Passwords are not equal"
                }
                if (name.isEmpty()) {
                    nameInputLayout.error = "Login is empty"
                }
                if (surname.isEmpty()) {
                    surnameInputLayout.error = "Login is empty"
                }
                if (password.isEmpty()) {
                    passwordInputLayout.error = "Password is empty"
                }
                if (repeatPassword.isEmpty()) {
                    repeatPasswordInputLayout.error ="Repeat Password is empty"
                }
                if (name.isNotEmpty() && surname.isNotEmpty() && password.isNotEmpty() && repeatPassword.isNotEmpty()) {
                    if (password == repeatPassword) {
                        registerFragmentViewModel.registerAndFetchToken(email, password, name, surname)
                    } else {
                        repeatPasswordInputLayout.error = "Passwords are not equal"
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            registerFragmentViewModel.authState.collect { state ->
                when(state) {
                    is AuthState.Loading -> {

                    }
                    is AuthState.Success -> {
                        registerFragmentViewModel.setLoadingState()
                        val action = RegisterFragmentDirections.actionRegisterFragmentToAppNavGraph()
                        findNavController().navigate(action)
                    }
                    is AuthState.Error -> {
                        Snackbar.make(binding.root, state.e.message.toString(), Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

}