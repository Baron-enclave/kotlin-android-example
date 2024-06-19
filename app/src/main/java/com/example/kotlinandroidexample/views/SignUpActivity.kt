package com.example.kotlinandroidexample.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinandroidexample.databinding.ActivitySignupBinding
import com.example.kotlinandroidexample.helpers.DBHelper
import com.example.kotlinandroidexample.services.SQLiteAuthService
import com.example.kotlinandroidexample.viewmodels.SignUpViewModel
import com.example.kotlinandroidexample.views.home.HomeActivity


class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var tvErrorMessage: TextView

    private lateinit var signUpViewModel: SignUpViewModel
    private val dbHelper = DBHelper(this, null)
    private val authService = SQLiteAuthService.getInstance(dbHelper)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        tvErrorMessage = binding.tvErrorText
        setContentView(binding.root)

        val factory = SignUpViewModel.SignUpViewModelFactory(authService)
        signUpViewModel = ViewModelProvider(this, factory)[SignUpViewModel::class.java]
        binding.viewModel = signUpViewModel
        signUpViewModel.responseLiveData.observe(this) {
            when (it) {
                is SignUpViewModel.SignUpResponse.Failure -> displayMessage(it.message)
                is SignUpViewModel.SignUpResponse.Success -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }

                is SignUpViewModel.SignUpResponse.Loading -> {
                    /*show loading UI */
                }
            }
        }

        binding.etEmail.addTextChangedListener {
            signUpViewModel.email = it.toString()

        }

        binding.etPassword.addTextChangedListener {
            signUpViewModel.password = it.toString()
        }

        binding.etName.addTextChangedListener {
            signUpViewModel.name = it.toString()
        }


    }

    private fun displayMessage(message: String) {
        tvErrorMessage.text = message
        tvErrorMessage.visibility = View.VISIBLE
    }

    private fun hideMessage() {
        tvErrorMessage.visibility = View.INVISIBLE
    }
}