package com.example.trainsmart.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.trainsmart.databinding.FragmentOnboardingStep1Binding

class OnboardingStep1Fragment : Fragment() {

    private var _binding: FragmentOnboardingStep1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingStep1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            val ageStr = binding.etAge.text.toString().trim()
            val poidsStr = binding.etPoids.text.toString().trim()
            val tailleStr = binding.etTaille.text.toString().trim()
            val frequenceStr = binding.etFrequence.text.toString().trim()

            // Validations
            if (ageStr.isEmpty()) { binding.tilAge.error = "Requis"; return@setOnClickListener }
            if (poidsStr.isEmpty()) { binding.tilPoids.error = "Requis"; return@setOnClickListener }
            if (tailleStr.isEmpty()) { binding.tilTaille.error = "Requis"; return@setOnClickListener }
            if (frequenceStr.isEmpty()) { binding.tilFrequence.error = "Requis"; return@setOnClickListener }

            val age = ageStr.toIntOrNull() ?: 0
            val poids = poidsStr.toFloatOrNull() ?: 0f
            val taille = tailleStr.toIntOrNull() ?: 0
            val frequence = frequenceStr.toIntOrNull() ?: 3

            if (age < 10 || age > 100) { binding.tilAge.error = "Âge invalide"; return@setOnClickListener }
            if (poids < 30 || poids > 250) { binding.tilPoids.error = "Poids invalide"; return@setOnClickListener }

            // Sauvegarde
            (activity as OnboardingActivity).userProfile
                .saveBasicInfo(age, poids, taille, "H", frequence)

            // Étape suivante
            (activity as OnboardingActivity).showStep(2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}