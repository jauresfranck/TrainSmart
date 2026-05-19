package com.example.trainsmart.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.trainsmart.databinding.ActivitySplashBinding
import com.example.trainsmart.ui.dashboard.MainActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bouton Commencer → onboarding (pour l'instant on va direct au dashboard)
        binding.btnCommencer.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Bouton Se connecter → AuthActivity
        binding.btnConnexion.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }
}