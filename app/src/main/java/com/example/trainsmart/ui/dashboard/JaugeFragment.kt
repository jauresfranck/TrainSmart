package com.example.trainsmart.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.trainsmart.R
import com.example.trainsmart.databinding.FragmentJaugeBinding
import com.example.trainsmart.viewmodel.SeanceViewModel
import kotlinx.coroutines.launch

class JaugeFragment : Fragment() {

    private var _binding: FragmentJaugeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SeanceViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJaugeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SeanceViewModel::class.java]

        lifecycleScope.launch {
            val charge7j  = viewModel.getCharge7Jours()
            val charge28j = viewModel.getCharge28Jours()
            val acwr      = viewModel.calculerACWR()
            val zone      = viewModel.getZoneACWR(acwr)

            // Charges
            binding.tvCharge7j.text  = "$charge7j UA"
            binding.tvCharge28j.text = "$charge28j UA"

            if (acwr > 0f) {
                val acwrStr = String.format("%.2f", acwr)
                binding.tvRatioAcwr.text = acwrStr
                binding.tvBadgeZone.visibility = View.VISIBLE
                updateZone(zone, acwr)
                updateMarqueur(acwr)
            } else {
                binding.tvRatioAcwr.text = "—"
                binding.tvRecommandationTitre.text = "Données insuffisantes"
                binding.tvRecommandationDesc.text =
                    "Enregistrez au moins 7 jours de séances pour calculer votre ACWR."
            }
        }
    }

    private fun updateZone(zone: String, acwr: Float) {
        when (zone) {
            "vert" -> {
                binding.tvBadgeZone.text = "Zone verte · Prêt"
                binding.tvBadgeZone.setTextColor(
                    requireContext().getColor(R.color.green_dark))
                binding.tvBadgeZone.setBackgroundResource(R.drawable.badge_green)
                binding.tvRecommandationTitre.text = "Scénario vert"
                binding.tvRecommandationDesc.text =
                    "Maintenir la séance difficile prévue. Votre corps est bien préparé."
                binding.tvRatioAcwr.setTextColor(
                    requireContext().getColor(R.color.green_primary))
            }
            "orange" -> {
                binding.tvBadgeZone.text = "Zone orange · Attention"
                binding.tvBadgeZone.setTextColor(
                    requireContext().getColor(R.color.gauge_orange))
                binding.tvBadgeZone.setBackgroundResource(R.drawable.badge_orange)
                binding.tvRecommandationTitre.text = "Scénario orange"
                binding.tvRecommandationDesc.text =
                    "Réduire le volume de 30%. Votre charge récente est élevée."
                binding.tvRatioAcwr.setTextColor(
                    requireContext().getColor(R.color.gauge_orange))
            }
            "rouge" -> {
                binding.tvBadgeZone.text = "Zone rouge · Repos"
                binding.tvBadgeZone.setTextColor(
                    requireContext().getColor(R.color.red_primary))
                binding.tvBadgeZone.setBackgroundResource(R.drawable.badge_red)
                binding.tvRecommandationTitre.text = "Scénario rouge"
                binding.tvRecommandationDesc.text =
                    "Annuler la séance. Repos complet conseillé aujourd'hui."
                binding.tvRatioAcwr.setTextColor(
                    requireContext().getColor(R.color.red_primary))
            }
            else -> {
                binding.tvRecommandationTitre.text = "Données insuffisantes"
                binding.tvRecommandationDesc.text =
                    "Continuez à enregistrer vos séances pour obtenir votre ACWR."
            }
        }
    }

    private fun updateMarqueur(acwr: Float) {
        binding.viewJaugeBar.post {
            val largeur = binding.viewJaugeBar.width.toFloat()
            val pct = ((acwr - 0.8f) / (1.5f - 0.8f)).coerceIn(0f, 1f)
            binding.viewMarker.translationX = pct * largeur
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}