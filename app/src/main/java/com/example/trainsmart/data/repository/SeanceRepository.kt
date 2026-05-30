package com.example.trainsmart.data.repository

import androidx.lifecycle.LiveData
import com.example.trainsmart.data.dao.SeanceDao
import com.example.trainsmart.data.entity.Seance
import java.util.Calendar

class SeanceRepository(private val seanceDao: SeanceDao) {

    // Toutes les séances
    val allSeances: LiveData<List<Seance>> = seanceDao.getAllSeances()

    // Insérer
    suspend fun insert(seance: Seance) {
        seanceDao.insert(seance)
    }

    // Modifier
    suspend fun update(seance: Seance) {
        seanceDao.update(seance)
    }

    // Supprimer
    suspend fun delete(seance: Seance) {
        seanceDao.delete(seance)
    }

    // Charge des 7 derniers jours
    suspend fun getCharge7Jours(): Int {
        val depuis = getTimestampIlYAJours(7)
        return seanceDao.getChargeTotaleDepuis(depuis)
    }

    // Charge des 28 derniers jours (pour ACWR)
    suspend fun getCharge28Jours(): Int {
        val depuis = getTimestampIlYAJours(28)
        return seanceDao.getChargeTotaleDepuis(depuis)
    }

    // Séances des 7 derniers jours sous forme de liste
    suspend fun getSeances7Jours(): List<Seance> {
        val depuis = getTimestampIlYAJours(7)
        return seanceDao.getSeancesListDepuis(depuis)
    }

    // Séances des 28 derniers jours
    suspend fun getSeances28Jours(): List<Seance> {
        val depuis = getTimestampIlYAJours(28)
        return seanceDao.getSeancesListDepuis(depuis)
    }

    // Calcul ACWR
    suspend fun calculerACWR(): Float {
        val charge7j = getCharge7Jours()
        val charge28j = getCharge28Jours()

        if (charge28j == 0) return 0f

        // Moyenne hebdomadaire sur 28 jours
        val moyenneHebdo = charge28j / 4f
        return if (moyenneHebdo == 0f) 0f else charge7j / moyenneHebdo
    }

    // Zone ACWR
    fun getZoneACWR(acwr: Float): String {
        return when {
            acwr == 0f -> "insuffisant"
            acwr <= 1.3f -> "vert"
            acwr <= 1.5f -> "orange"
            else -> "rouge"
        }
    }

    private fun getTimestampIlYAJours(jours: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -jours)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        return calendar.timeInMillis
    }
}