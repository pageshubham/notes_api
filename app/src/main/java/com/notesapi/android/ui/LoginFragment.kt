package com.notesapi.android.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.notesapi.android.R
import com.notesapi.android.databinding.FragmentLoginBinding
import com.notesapi.android.model.UserRequest
import com.notesapi.android.util.NetworkResult
import com.notesapi.android.util.TokenManager
import com.notesapi.android.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val validation = validateUserInput()
            if (validation.first) {
                authViewModel.loginUser(getUserInput())
            } else {
                binding.txtError.text = validation.second
            }
        }

        binding.btnSignUp.setOnClickListener {
            findNavController().popBackStack() //to pop top fragment
        }

        bindObservers()

    }

    private fun getUserInput(): UserRequest {
        val email = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        return UserRequest("", email, password)
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val user = getUserInput()
        return authViewModel.validateCredentials(user.username, user.email, user.password, true)
    }

    private fun bindObservers() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                }
            }
        })
    }

    //make _binding null in onDestroyView to free memory & improve app performance
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}