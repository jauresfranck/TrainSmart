package com.example.trainsmart.ui.seance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trainsmart.R
import com.example.trainsmart.data.entity.Seance
import com.example.trainsmart.databinding.FragmentSeanceBinding
import com.example.trainsmart.viewmodel.SeanceViewModel
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SeanceFragment : Fragment() {

    private var _binding: FragmentSeanceBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SeanceViewModel

    private var typeSelectionne = "Force"
    private var douleurOui = false
    private var rpeValue = 7
    private var sommeilValue = 6

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SeanceViewModel::class.java]

        // Sélection Force par défaut
        selectType(binding.btnForce, "Force")

        // Sélection Non douleur par défaut
        selectDouleur(false)

        // Boutons type
        binding.btnForce.setOnClickListener {
            selectType(binding.btnForce, "Force")
        }
        binding.btnCardio.setOnClickListener {
            selectType(binding.btnCardio, "Cardio")
        }

        // Boutons douleur
        binding.btnDouleurNon.setOnClickListener { selectDouleur(false) }
        binding.btnDouleurOui.setOnClickListener { selectDouleur(true) }

        // SeekBar RPE
        binding.seekbarRpe.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                rpeValue = progress + 1
                binding.tvRpeValue.text = "RPE : $rpeValue"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // SeekBar Sommeil
        binding.seekbarSommeil.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sommeilValue = progress + 1
                binding.tvSommeilValue.text = "$sommeilValue / 10"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Bouton Valider
        binding.btnValider.setOnClickListener {
            validerSeance()
        }
    }

    private fun selectType(selected: MaterialButton, type: String) {
        typeSelectionne = type
        val buttons = listOf(binding.btnForce, binding.btnCardio)
        buttons.forEach { btn ->
            if (btn == selected) {
                btn.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.green_light))
                btn.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.green_dark))
                btn.strokeColor = android.content.res.ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.green_primary))
            } else {
                btn.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.white))
                btn.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.text_secondary))
                btn.strokeColor = android.content.res.ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.border))
            }
        }
    }

    private fun selectDouleur(oui: Boolean) {
        douleurOui = oui
        binding.tilZoneDouleur.visibility = if (oui) View.VISIBLE else View.GONE

        if (oui) {
            binding.btnDouleurOui.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.red_light))
            binding.btnDouleurOui.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.red_primary))
            binding.btnDouleurOui.strokeColor = android.content.res.ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.red_primary))
            binding.btnDouleurNon.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.white))
            binding.btnDouleurNon.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.text_secondary))
            binding.btnDouleurNon.strokeColor = android.content.res.ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.border))
        } else {
            binding.btnDouleurNon.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.green_light))
            binding.btnDouleurNon.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.green_dark))
            binding.btnDouleurNon.strokeColor = android.content.res.ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.green_primary))
            binding.btnDouleurOui.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.white))
            binding.btnDouleurOui.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.text_secondary))
            binding.btnDouleurOui.strokeColor = android.content.res.ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.border))
        }
    }

    private fun validerSeance() {
        val dureeStr = binding.etDuree.text.toString().trim()

        if (dureeStr.isEmpty()) {
            binding.tilDuree.error = "Veuillez entrer la durée"
            return
        }

        val duree = dureeStr.toIntOrNull() ?: 0
        if (duree <= 0 || duree > 600) {
            binding.tilDuree.error = "Durée invalide"
            return
        }

        binding.tilDuree.error = null

        val chargeUA = duree * rpeValue
        val zone = binding.etZoneDouleur.text.toString().trim()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateStr = dateFormat.format(Date())

        val seance = Seance(
            date = dateStr,
            dureeMinutes = duree,
            type = typeSelectionne,
            rpe = rpeValue,
            chargeUA = chargeUA,
            qualiteSommeil = sommeilValue,
            douleurLocalisee = douleurOui,
            zoneDouleur = zone
        )

        viewModel.insert(seance)

        Toast.makeText(
            requireContext(),
            "Séance enregistrée ! Charge : $chargeUA UA",
            Toast.LENGTH_SHORT
        ).show()

        // Reset du formulaire
        binding.etDuree.setText("")
        binding.seekbarRpe.progress = 6
        binding.seekbarSommeil.progress = 5
        selectType(binding.btnForce, "Force")
        selectDouleur(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}