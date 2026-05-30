package com.example.trainsmart.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.trainsmart.databinding.ActivityOnboardingBinding
import com.example.trainsmart.ui.dashboard.MainActivity
import com.example.trainsmart.utils.AuthManager
import com.example.trainsmart.utils.UserProfile

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    lateinit var userProfile: UserProfile
    lateinit var authManager: AuthManager
    private var currentStep = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userProfile = UserProfile(this)
        authManager = AuthManager(this)
        showStep(1)
    }

    fun showStep(step: Int) {
        currentStep = step
        val fragment = when (step) {
            1 -> OnboardingStep1Fragment()
            2 -> OnboardingStep2Fragment()
            3 -> OnboardingStep3Fragment()
            else -> OnboardingStep1Fragment()
        }
        supportFragmentManager.beginTransaction()
            .replace(binding.onboardingContainer.id, fragment)
            .commit()
    }

    fun finishOnboarding() {
        authManager.setOnboardingDone()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}