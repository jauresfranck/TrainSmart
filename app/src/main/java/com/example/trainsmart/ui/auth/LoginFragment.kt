package com.example.trainsmart.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.trainsmart.R
import com.example.trainsmart.databinding.FragmentLoginBinding
import com.example.trainsmart.ui.dashboard.MainActivity
import com.example.trainsmart.utils.AuthManager

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var authManager: AuthManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        authManager = AuthManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // Reset erreurs
            binding.tilEmail.error = null
            binding.tilPassword.error = null

            // Validations
            if (email.isEmpty()) {
                binding.tilEmail.error = "Veuillez entrer votre e-mail"
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tilEmail.error = "E-mail invalide"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.tilPassword.error = "Veuillez entrer votre mot de passe"
                return@setOnClickListener
            }
            if (password.length < 6) {
                binding.tilPassword.error = "Mot de passe trop court (6 caractères min.)"
                return@setOnClickListener
            }

            // Tentative de connexion
            if (authManager.login(email, password)) {
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            } else {
                binding.tilEmail.error = "E-mail ou mot de passe incorrect"
                binding.tilPassword.error = " "
            }
        }

        binding.tvGoRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}