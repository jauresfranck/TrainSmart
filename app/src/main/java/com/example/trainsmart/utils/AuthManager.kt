package com.example.trainsmart.utils

import android.content.Context
import android.content.SharedPreferences

class AuthManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("trainsmart_auth", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_EMAIL = "user_email"
        private const val KEY_PASSWORD = "user_password"
        private const val KEY_PRENOM = "user_prenom"
        private const val KEY_IS_LOGGED = "is_logged_in"
        private const val KEY_ONBOARDING_DONE = "onboarding_done"
    }

    // ── Inscription ──────────────────────────────────────────────
    fun register(prenom: String, email: String, password: String): Boolean {
        // Vérifie si le compte existe déjà
        if (prefs.getString(KEY_EMAIL, null) == email) {
            return false // compte déjà existant
        }
        prefs.edit()
            .putString(KEY_PRENOM, prenom)
            .putString(KEY_EMAIL, email)
            .putString(KEY_PASSWORD, password)
            .putBoolean(KEY_IS_LOGGED, true)
            .apply()
        return true
    }

    // ── Connexion ────────────────────────────────────────────────
    fun login(email: String, password: String): Boolean {
        val savedEmail = prefs.getString(KEY_EMAIL, null)
        val savedPassword = prefs.getString(KEY_PASSWORD, null)
        return if (savedEmail == email && savedPassword == password) {
            prefs.edit().putBoolean(KEY_IS_LOGGED, true).apply()
            true
        } else {
            false
        }
    }

    // ── Déconnexion ──────────────────────────────────────────────
    fun logout() {
        prefs.edit().putBoolean(KEY_IS_LOGGED, false).apply()
    }

    // ── Getters ──────────────────────────────────────────────────
    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED, false)

    fun getPrenom(): String = prefs.getString(KEY_PRENOM, "Utilisateur") ?: "Utilisateur"

    fun getEmail(): String = prefs.getString(KEY_EMAIL, "") ?: ""

    fun isOnboardingDone(): Boolean = prefs.getBoolean(KEY_ONBOARDING_DONE, false)

    fun setOnboardingDone() {
        prefs.edit().putBoolean(KEY_ONBOARDING_DONE, true).apply()
    }
}