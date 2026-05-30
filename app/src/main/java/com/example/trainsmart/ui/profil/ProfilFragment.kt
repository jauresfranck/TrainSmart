package com.example.trainsmart.ui.profil

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.trainsmart.databinding.FragmentProfilBinding
import com.example.trainsmart.ui.auth.SplashActivity
import com.example.trainsmart.utils.AuthManager
import com.example.trainsmart.utils.UserProfile

class ProfilFragment : Fragment() {

    private var _binding: FragmentProfilBinding? = null
    private val binding get() = _binding!!
    private lateinit var authManager: AuthManager
    private lateinit var userProfile: UserProfile

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authManager = AuthManager(requireContext())
        userProfile = UserProfile(requireContext())

        // Initiales et nom
        val prenom = authManager.getPrenom()
        val initiales = prenom.take(2).uppercase()
        binding.tvInitiales.text = initiales
        binding.tvNomComplet.text = prenom
        binding.tvMembreDepuis.text = "Membre TrainSmart"

        // Données personnelles
        val age = userProfile.getAge()
        val poids = userProfile.getPoids()
        val sport = userProfile.getSport()
        val frequence = userProfile.getFrequence()
        val niveau = userProfile.getNiveau()

        binding.tvAge.text = if (age > 0) "$age ans" else "—"
        binding.tvPoids.text = if (poids > 0) "$poids kg" else "—"
        binding.tvSport.text = if (sport.isNotEmpty()) "$sport · $niveau" else "—"
        binding.tvFrequence.text = if (frequence > 0) "$frequence séances/sem." else "—"

        // Historique médical
        val hasBlessure = userProfile.hasBlessure()
        val zone = userProfile.getZoneBlessure()
        binding.tvBlessure.text = if (hasBlessure) "Oui" else "Non"
        binding.tvZoneBlessure.text = if (zone.isNotEmpty()) zone else "—"

        // Bouton modifier
        binding.btnModifierProfil.setOnClickListener {
            // TODO: navigation vers édition profil
        }

        // Bouton déconnexion
        binding.btnDeconnexion.setOnClickListener {
            authManager.logout()
            startActivity(Intent(requireContext(), SplashActivity::class.java))
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}