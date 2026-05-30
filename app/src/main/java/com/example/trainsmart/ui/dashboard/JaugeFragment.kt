package com.example.trainsmart.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class JaugeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val tv = TextView(requireContext())
        tv.text = "Jauge ACWR — bientôt disponible"
        tv.textSize = 16f
        tv.setPadding(48, 48, 48, 48)
        return tv
    }
}