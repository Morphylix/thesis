package com.morphylix.android.petkeeper.presentation.auth.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.morphylix.android.petkeeper.databinding.FragmentLoginBinding
import com.morphylix.android.petkeeper.presentation.auth.AuthState
import com.morphylix.android.petkeeper.presentation.base.BaseFragment
import kotlinx.coroutines.launch
import org.w3c.dom.Text

private const val TAG = "LoginFragment"

class LoginFragment : BaseFragment<FragmentLoginBinding>({ inflater, container ->
    FragmentLoginBinding.inflate(inflater, container, false)
}) {

    private val loginFragmentViewModel: LoginViewModel by activityViewModels()
    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    private var googleAcc: GoogleSignInAccount? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)

        googleAcc = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (googleAcc != null) {
            Log.i(TAG, "email before logging in is ${googleAcc?.email}")
            loginFragmentViewModel.loginWithGoogle(googleAcc!!.email!!, googleAcc!!.id!!, googleAcc!!.displayName!!)
        }


        //findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToAppNavGraph())
        loginFragmentViewModel.autoLoginWithToken()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                task.getResult(ApiException::class.java)

                googleAcc = GoogleSignIn.getLastSignedInAccount(requireContext())
                loginFragmentViewModel.loginWithGoogle(googleAcc!!.email!!, googleAcc!!.id!!, googleAcc!!.displayName!!)

                Log.i(TAG, "email is ${googleAcc?.email}, id is ${googleAcc?.id}")
                //gsc.signOut()
                // TODO: WRITE LOGIC
            } catch (e: ApiException) {
                Log.i(TAG, "something went wrong")
            }
        }

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(requireContext(), gso)

        binding.signInWithGoogleButton.setOnClickListener {
            val intent = gsc.signInIntent
            resultLauncher.launch(intent)
        }

        binding.registerUserTextView.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToValidateEmailFragment()
            findNavController().navigate(action)
        }

        binding.loginButton.setOnClickListener {
            val login: String
            val password: String
            with(binding) {
                login = emailEditText.text.toString()
                password = passwordEditText.text.toString()

                if (login.isEmpty()) {
                    //loginWarningTextView.visibility = View.VISIBLE
                    emailInputLayout.error = "Login is empty"
                }
                if (password.isEmpty()) {
                    passwordInputLayout.error = "Password is empty"
                }
                if (login.isNotEmpty() && password.isNotEmpty()) {
                    loginFragmentViewModel.loginAndFetchToken(login, password)
                }
            }
        }

        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null && p0.isNotEmpty()) {
                    binding.emailInputLayout.error = null
                }
            }

        })

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null && p0.isNotEmpty()) {
                    binding.passwordInputLayout.error = null
                }
            }

        })

        lifecycleScope.launchWhenStarted {
            loginFragmentViewModel.authState.collect { state ->
                when (state) {
                    is AuthState.Loading -> {

                    }
                    is AuthState.Success -> {
                        Log.i(TAG, "Token is ${state.token}")
                        Log.i(TAG, "I NAVIGATE IT")
                        loginFragmentViewModel.setLoadingState()
                        val action =
                            LoginFragmentDirections.actionLoginFragmentToAppNavGraph()
                        findNavController().navigate(action)
                    }
                    is AuthState.Error -> {
                        Snackbar.make(
                            binding.root,
                            state.e.message.toString(),
                            Snackbar.LENGTH_LONG
                        ).show()
                        Log.i(TAG, "Error: ${state.e}")
                    }
                }
            }
        }
    }
}