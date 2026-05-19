package com.example.trainsmart.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.trainsmart.databinding.ActivitySplashBinding
import com.example.trainsmart.ui.dashboard.MainActivity
import com.example.trainsmart.utils.AuthManager

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authManager = AuthManager(this)

        // Si déjà connecté → Dashboard direct
        if (authManager.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        // Bouton Commencer → Inscription
        binding.btnCommencer.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }

        // Bouton Se connecter → Login
        binding.btnConnexion.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }
}