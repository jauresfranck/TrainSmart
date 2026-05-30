package com.example.trainsmart.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.trainsmart.R
import com.example.trainsmart.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Si on vient du bouton "Se connecter" du Splash → démarrer sur Login
        val startLogin = intent.getBooleanExtra("start_login", false)
        if (startLogin) {
            val navHost = supportFragmentManager
                .findFragmentById(R.id.auth_nav_host) as NavHostFragment
            navHost.navController.navigate(R.id.loginFragment)
        }
    }
}