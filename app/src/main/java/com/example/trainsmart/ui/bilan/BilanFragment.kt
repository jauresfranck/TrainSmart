package com.example.trainsmart.ui.bilan

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trainsmart.R
import com.example.trainsmart.databinding.FragmentBilanBinding
import com.example.trainsmart.viewmodel.SeanceViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class BilanFragment : Fragment() {

    private var _binding: FragmentBilanBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SeanceViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBilanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SeanceViewModel::class.java]

        // Période
        val dateFormat = SimpleDateFormat("d MMM", Locale.FRENCH)
        val cal = Calendar.getInstance()
        val fin = dateFormat.format(cal.time)
        cal.add(Calendar.DAY_OF_YEAR, -6)
        val debut = dateFormat.format(cal.time)
        binding.tvPeriode.text = "Semaine du $debut – $fin"

        // Observer les séances
        viewModel.allSeances.observe(viewLifecycleOwner) { seances ->

            // Filtrer les 7 derniers jours
            val il7Jours = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, -6)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }.timeInMillis

            val seancesSemaine = seances.filter { it.timestamp >= il7Jours }

            // Stats semaine
            binding.tvTotalSeances.text = seancesSemaine.size.toString()
            val totalCharge = seancesSemaine.sumOf { it.chargeUA }
            binding.tvTotalCharge.text = totalCharge.toString()
            if (seancesSemaine.isNotEmpty()) {
                val rpeMoyen = seancesSemaine.map { it.rpe }.average()
                binding.tvRpeMoyen.text = String.format("%.1f", rpeMoyen)
            }

            // Graphique en barres
            setupBarChart(seancesSemaine)

            // Corrélations
            setupCorrelations(seancesSemaine)
        }

        binding.btnCorriger.setOnClickListener {
            // TODO: navigation vers liste des séances
        }
    }

    private fun setupBarChart(seances: List<com.example.trainsmart.data.entity.Seance>) {
        val chart = binding.barChart

        // Prépare les 7 derniers jours
        val labels = mutableListOf<String>()
        val entries = mutableListOf<BarEntry>()
        val joursFmt = SimpleDateFormat("EEE", Locale.FRENCH)
        val dateFmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        for (i in 6 downTo 0) {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, -i)
            val dateStr = dateFmt.format(cal.time)
            val label = joursFmt.format(cal.time)
                .replaceFirstChar { it.uppercase() }.take(3)
            labels.add(label)

            val chargeJour = seances
                .filter { it.date == dateStr }
                .sumOf { it.chargeUA }
                .toFloat()

            entries.add(BarEntry((6 - i).toFloat(), chargeJour))
        }

        val dataSet = BarDataSet(entries, "Charge UA").apply {
            color = Color.parseColor("#1D9E75")
            valueTextColor = Color.parseColor("#6B7280")
            valueTextSize = 10f
            setDrawValues(true)
        }

        chart.apply {
            data = BarData(dataSet).apply { barWidth = 0.6f }
            description.isEnabled = false
            legend.isEnabled = false
            setDrawGridBackground(false)
            setDrawBorders(false)
            setTouchEnabled(false)
            animateY(800)

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = IndexAxisValueFormatter(labels)
                granularity = 1f
                setDrawGridLines(false)
                textColor = Color.parseColor("#6B7280")
                textSize = 11f
            }

            axisLeft.apply {
                setDrawGridLines(true)
                gridColor = Color.parseColor("#E5E7EB")
                textColor = Color.parseColor("#6B7280")
                axisMinimum = 0f
            }

            axisRight.isEnabled = false
            invalidate()
        }
    }

    private fun setupCorrelations(
        seances: List<com.example.trainsmart.data.entity.Seance>
    ) {
        val seancesAvecDouleur = seances.filter { it.douleurLocalisee }

        if (seancesAvecDouleur.isEmpty()) {
            binding.tvCorrelations.text =
                "Aucune douleur signalée cette semaine. Continuez ainsi !"
            return
        }

        val sb = StringBuilder()
        seancesAvecDouleur.forEach { seance ->
            val cal = Calendar.getInstance()
            cal.timeInMillis = seance.timestamp
            val jour = SimpleDateFormat("EEEE", Locale.FRENCH)
                .format(Date(seance.timestamp))
                .replaceFirstChar { it.uppercase() }
            sb.append("⚠️ Douleur signalée le $jour")
            if (seance.zoneDouleur.isNotEmpty()) {
                sb.append(" — ${seance.zoneDouleur}")
            }
            sb.append(" (charge : ${seance.chargeUA} UA)\n")
        }

        // Vérifie si les jours de repos sont insuffisants
        val joursAvecSeance = seances.map { it.date }.toSet().size
        if (joursAvecSeance >= 6) {
            sb.append("\n⚠️ Seulement ${7 - joursAvecSeance} jour(s) de repos sur 7")
        }

        binding.tvCorrelations.text = sb.toString().trim()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}