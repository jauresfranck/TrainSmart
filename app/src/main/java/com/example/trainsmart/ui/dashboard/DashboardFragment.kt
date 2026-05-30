package com.example.trainsmart.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.trainsmart.R
import com.example.trainsmart.databinding.FragmentDashboardBinding
import com.example.trainsmart.utils.AuthManager
import com.example.trainsmart.viewmodel.SeanceViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Calendar

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SeanceViewModel
    private lateinit var authManager: AuthManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SeanceViewModel::class.java]
        authManager = AuthManager(requireContext())

        // Greeting
        val prenom = authManager.getPrenom()
        binding.tvGreeting.text = "Bonjour $prenom 👋"

        // Date
        val dateFormat = SimpleDateFormat("EEEE d MMMM", Locale.FRENCH)
        binding.tvDate.text = dateFormat.format(Date())

        // Observer les séances
        viewModel.allSeances.observe(viewLifecycleOwner) { seances ->
            // Séances ce mois
            val cal = Calendar.getInstance()
            val moisCourant = cal.get(Calendar.MONTH)
            val anneeCourante = cal.get(Calendar.YEAR)
            val seancesMois = seances.filter { seance ->
                val c = Calendar.getInstance()
                c.timeInMillis = seance.timestamp
                c.get(Calendar.MONTH) == moisCourant &&
                        c.get(Calendar.YEAR) == anneeCourante
            }
            binding.tvSeancesMois.text = seancesMois.size.toString()

            // Jours de repos cette semaine
            val debutSemaine = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }.timeInMillis

            val joursAvecSeance = seances
                .filter { it.timestamp >= debutSemaine }
                .map {
                    val c = Calendar.getInstance()
                    c.timeInMillis = it.timestamp
                    c.get(Calendar.DAY_OF_YEAR)
                }.toSet().size

            val joursRepos = 7 - joursAvecSeance
            binding.tvJoursRepos.text = joursRepos.toString()
        }

        // Calcul ACWR
        lifecycleScope.launch {
            val charge7j = viewModel.getCharge7Jours()
            val acwr = viewModel.calculerACWR()
            val zone = viewModel.getZoneACWR(acwr)

            binding.tvCharge7j.text = charge7j.toString()

            if (acwr > 0f) {
                val acwrFormate = String.format("%.2f", acwr)
                binding.tvAcwr.text = acwrFormate
                updateJauge(acwr, zone)
            } else {
                binding.tvAcwr.text = "—"
            }
        }

        // Bouton enregistrer → onglet Séance
        binding.btnEnregistrer.setOnClickListener {
            findNavController().navigate(R.id.seanceFragment)
        }
    }

    private fun updateJauge(acwr: Float, zone: String) {
        // Mise à jour du statut
        when (zone) {
            "vert" -> {
                binding.tvStatutTitre.text = "Prêt à s'entraîner"
                binding.tvStatutDesc.text = "Zone verte · Maintenir la séance"
                binding.tvBadgeZone.text = "Vert"
                binding.tvBadgeZone.visibility = View.VISIBLE
                binding.tvBadgeZone.setTextColor(
                    requireContext().getColor(R.color.green_dark))
            }
            "orange" -> {
                binding.tvStatutTitre.text = "Attention à la charge"
                binding.tvStatutDesc.text = "Zone orange · Réduire de 30%"
                binding.tvBadgeZone.text = "Orange"
                binding.tvBadgeZone.visibility = View.VISIBLE
                binding.tvBadgeZone.setTextColor(
                    requireContext().getColor(R.color.gauge_orange))
            }
            "rouge" -> {
                binding.tvStatutTitre.text = "Repos conseillé"
                binding.tvStatutDesc.text = "Zone rouge · Annuler la séance"
                binding.tvBadgeZone.text = "Rouge"
                binding.tvBadgeZone.visibility = View.VISIBLE
                binding.tvBadgeZone.setTextColor(
                    requireContext().getColor(R.color.gauge_red))
            }
            else -> {
                binding.tvStatutTitre.text = "Données insuffisantes"
                binding.tvStatutDesc.text = "Continuez à enregistrer vos séances"
            }
        }

        // Position du marqueur sur la jauge
        binding.viewGaugeMarker.post {
            val largeurJauge = binding.viewGaugeMarker.parent as View
            val largeur = (largeurJauge as ViewGroup).width.toFloat()
            // ACWR 0.8 → 0%, 1.5 → 100%
            val pct = ((acwr - 0.8f) / (1.5f - 0.8f)).coerceIn(0f, 1f)
            binding.viewGaugeMarker.translationX = pct * largeur
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}