package com.example.trainsmart.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "seances")
data class Seance(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,           // format "yyyy-MM-dd"
    val dureeMinutes: Int,      // durée en minutes
    val type: String,           // "Force" ou "Cardio"
    val rpe: Int,               // 1 à 10
    val chargeUA: Int,          // dureeMinutes × rpe
    val qualiteSommeil: Int,    // 1 à 10
    val douleurLocalisee: Boolean,
    val zoneDouleur: String = "",
    val timestamp: Long = System.currentTimeMillis()
)