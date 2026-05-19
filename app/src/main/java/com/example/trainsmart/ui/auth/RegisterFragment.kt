package com.example.trainsmart.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.trainsmart.R
import com.example.trainsmart.databinding.FragmentRegisterBinding
import com.example.trainsmart.ui.dashboard.MainActivity
import com.example.trainsmart.utils.AuthManager

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var authManager: AuthManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        authManager = AuthManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val prenom = binding.etPrenom.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirm = binding.etConfirmPassword.text.toString().trim()

            // Reset erreurs
            binding.tilPrenom.error = null
            binding.tilEmail.error = null
            binding.tilPassword.error = null
            binding.tilConfirmPassword.error = null

            // Validations
            if (prenom.isEmpty()) {
                binding.tilPrenom.error = "Veuillez entrer votre prénom"
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                binding.tilEmail.error = "Veuillez entrer votre e-mail"
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tilEmail.error = "E-mail invalide"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.tilPassword.error = "Veuillez entrer un mot de passe"
                return@setOnClickListener
            }
            if (password.length < 6) {
                binding.tilPassword.error = "6 caractères minimum"
                return@setOnClickListener
            }
            if (password != confirm) {
                binding.tilConfirmPassword.error = "Les mots de passe ne correspondent pas"
                return@setOnClickListener
            }

            // Tentative d'inscription
            if (authManager.register(prenom, email, password)) {
                // Inscription réussie → Dashboard
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            } else {
                binding.tilEmail.error = "Un compte existe déjà avec cet e-mail"
            }
        }

        binding.tvGoLogin.setOnClickListener {
            findNavController().navigate(R.id.action_register_to_login)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}