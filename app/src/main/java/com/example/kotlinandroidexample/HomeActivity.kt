package com.example.kotlinandroidexample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinandroidexample.databinding.ActivityHomeBinding
import com.example.kotlinandroidexample.services.AuthService
import com.example.kotlinandroidexample.services.FakeAuthService

class HomeActivity : AppCompatActivity() {
    // Creating FakeAuthService in both HomeActivity and MainActivity isn't necessary
    // Need a DI container to create it once
    private val authService: AuthService = FakeAuthService.instance

    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLogout.setOnClickListener {
            authService.logout()
            val intent = Intent(this, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }
}