package com.example.trainsmart.utils

import android.content.Context
import android.content.SharedPreferences

class UserProfile(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("trainsmart_profile", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_AGE = "age"
        private const val KEY_POIDS = "poids"
        private const val KEY_TAILLE = "taille"
        private const val KEY_SEXE = "sexe"
        private const val KEY_FREQUENCE = "frequence"
        private const val KEY_SPORT = "sport"
        private const val KEY_NIVEAU = "niveau"
        private const val KEY_BLESSURE = "blessure"
        private const val KEY_ZONE_BLESSURE = "zone_blessure"
        private const val KEY_NOTES = "notes"
    }

    fun saveBasicInfo(age: Int, poids: Float, taille: Int, sexe: String, frequence: Int) {
        prefs.edit()
            .putInt(KEY_AGE, age)
            .putFloat(KEY_POIDS, poids)
            .putInt(KEY_TAILLE, taille)
            .putString(KEY_SEXE, sexe)
            .putInt(KEY_FREQUENCE, frequence)
            .apply()
    }

    fun saveSportInfo(sport: String, niveau: String) {
        prefs.edit()
            .putString(KEY_SPORT, sport)
            .putString(KEY_NIVEAU, niveau)
            .apply()
    }

    fun saveMedicalInfo(blessure: Boolean, zone: String, notes: String) {
        prefs.edit()
            .putBoolean(KEY_BLESSURE, blessure)
            .putString(KEY_ZONE_BLESSURE, zone)
            .putString(KEY_NOTES, notes)
            .apply()
    }

    fun getAge(): Int = prefs.getInt(KEY_AGE, 0)
    fun getPoids(): Float = prefs.getFloat(KEY_POIDS, 0f)
    fun getTaille(): Int = prefs.getInt(KEY_TAILLE, 0)
    fun getSexe(): String = prefs.getString(KEY_SEXE, "") ?: ""
    fun getFrequence(): Int = prefs.getInt(KEY_FREQUENCE, 3)
    fun getSport(): String = prefs.getString(KEY_SPORT, "Musculation") ?: "Musculation"
    fun getNiveau(): String = prefs.getString(KEY_NIVEAU, "Intermédiaire") ?: "Intermédiaire"
    fun hasBlessure(): Boolean = prefs.getBoolean(KEY_BLESSURE, false)
    fun getZoneBlessure(): String = prefs.getString(KEY_ZONE_BLESSURE, "") ?: ""
    fun getNotes(): String = prefs.getString(KEY_NOTES, "") ?: ""
}