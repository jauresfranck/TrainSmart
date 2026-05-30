package com.example.trainsmart.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.trainsmart.R
import com.example.trainsmart.databinding.FragmentOnboardingStep2Binding
import com.google.android.material.button.MaterialButton

class OnboardingStep2Fragment : Fragment() {

    private var _binding: FragmentOnboardingStep2Binding? = null
    private val binding get() = _binding!!

    private var selectedSport = "Musculation"
    private var selectedNiveau = "Intermédiaire"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingStep2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Sélection sport par défaut
        selectSport(binding.btnMusculation)

        // Sélection niveau par défaut
        selectNiveau(binding.btnIntermediaire)

        // Boutons sport
        binding.btnMusculation.setOnClickListener {
            selectedSport = "Musculation"
            selectSport(binding.btnMusculation)
        }
        binding.btnCourse.setOnClickListener {
            selectedSport = "Course"
            selectSport(binding.btnCourse)
        }
        binding.btnMixte.setOnClickListener {
            selectedSport = "Mixte"
            selectSport(binding.btnMixte)
        }

        // Boutons niveau
        binding.btnDebutant.setOnClickListener {
            selectedNiveau = "Débutant"
            selectNiveau(binding.btnDebutant)
        }
        binding.btnIntermediaire.setOnClickListener {
            selectedNiveau = "Intermédiaire"
            selectNiveau(binding.btnIntermediaire)
        }
        binding.btnAvance.setOnClickListener {
            selectedNiveau = "Avancé"
            selectNiveau(binding.btnAvance)
        }

        binding.btnNext.setOnClickListener {
            (activity as OnboardingActivity).userProfile
                .saveSportInfo(selectedSport, selectedNiveau)
            (activity as OnboardingActivity).showStep(3)
        }
    }

    private fun selectSport(selected: MaterialButton) {
        val sportBtns = listOf(binding.btnMusculation, binding.btnCourse, binding.btnMixte)
        sportBtns.forEach { btn ->
            if (btn == selected) {
                btn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_light))
                btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.green_dark))
                btn.strokeColor = android.content.res.ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.green_primary))
            } else {
                btn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary))
                btn.strokeColor = android.content.res.ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.border))
            }
        }
    }

    private fun selectNiveau(selected: MaterialButton) {
        val niveauBtns = listOf(binding.btnDebutant, binding.btnIntermediaire, binding.btnAvance)
        niveauBtns.forEach { btn ->
            if (btn == selected) {
                btn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_light))
                btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.green_dark))
                btn.strokeColor = android.content.res.ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.green_primary))
            } else {
                btn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary))
                btn.strokeColor = android.content.res.ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.border))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}