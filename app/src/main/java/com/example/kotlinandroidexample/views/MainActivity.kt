package com.example.kotlinandroidexample.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinandroidexample.databinding.ActivityMainBinding
import com.example.kotlinandroidexample.helpers.DBHelper
import com.example.kotlinandroidexample.services.AuthService
import com.example.kotlinandroidexample.services.SQLiteAuthService
import com.example.kotlinandroidexample.viewmodels.LoginViewModel
import com.example.kotlinandroidexample.views.home.HomeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var dbHelper: DBHelper
    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)
        dbHelper = DBHelper(this, null)
        authService = SQLiteAuthService.getInstance(dbHelper)

        val loginViewModelFactory = LoginViewModel.LoginViewModelFactory(authService)
        loginViewModel =
            ViewModelProvider(this, loginViewModelFactory)[LoginViewModel::class.java]


        binding.viewModel = loginViewModel
        loginViewModel.loginLiveDataResponse.observe(this) {
            when (it) {
                is LoginViewModel.LoginResponse.Success -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }

                is LoginViewModel.LoginResponse.Failure -> displayMessage(it.message)
                is LoginViewModel.LoginResponse.Loading -> {
                    /*show loading UI */
                }
            }
        }

        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }

    private fun displayMessage(message: String) {
        binding.tvLoginMessage.text = message
        binding.tvLoginMessage.visibility = View.VISIBLE
    }
}
