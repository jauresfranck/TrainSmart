package com.example.trainsmart.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.trainsmart.R
import com.example.trainsmart.databinding.FragmentOnboardingStep3Binding

class OnboardingStep3Fragment : Fragment() {

    private var _binding: FragmentOnboardingStep3Binding? = null
    private val binding get() = _binding!!
    private var hasBlessure = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingStep3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Sélection Non par défaut
        selectNon()

        binding.btnOui.setOnClickListener {
            hasBlessure = true
            selectOui()
            binding.tilZone.visibility = View.VISIBLE
        }

        binding.btnNon.setOnClickListener {
            hasBlessure = false
            selectNon()
            binding.tilZone.visibility = View.GONE
        }

        binding.btnFinish.setOnClickListener {
            val zone = binding.etZone.text.toString().trim()
            val notes = binding.etNotes.text.toString().trim()

            (activity as OnboardingActivity).userProfile
                .saveMedicalInfo(hasBlessure, zone, notes)

            (activity as OnboardingActivity).finishOnboarding()
        }
    }

    private fun selectOui() {
        binding.btnOui.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_light))
        binding.btnOui.setTextColor(ContextCompat.getColor(requireContext(), R.color.green_dark))
        binding.btnOui.strokeColor = android.content.res.ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), R.color.green_primary))
        binding.btnNon.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.btnNon.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary))
        binding.btnNon.strokeColor = android.content.res.ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), R.color.border))
    }

    private fun selectNon() {
        binding.btnNon.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_light))
        binding.btnNon.setTextColor(ContextCompat.getColor(requireContext(), R.color.green_dark))
        binding.btnNon.strokeColor = android.content.res.ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), R.color.green_primary))
        binding.btnOui.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.btnOui.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary))
        binding.btnOui.strokeColor = android.content.res.ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), R.color.border))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}