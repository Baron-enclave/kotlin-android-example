package com.example.kotlinandroidexample

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinandroidexample.databinding.ActivityMainBinding
import com.example.kotlinandroidexample.models.Email
import com.example.kotlinandroidexample.services.FakeAuthService
import com.example.kotlinandroidexample.services.AuthService
import com.example.kotlinandroidexample.viewmodels.LoginViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var email: Email = Email("")
    private var password = ""

    private lateinit var loginViewModel: LoginViewModel
    private val authService: AuthService = FakeAuthService()

    //  Creating FakeAuthService in both HomeActivity and MainActivity isn't necessary
    // Need a DI container to create it once
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginViewModelFactory = LoginViewModel.LoginViewModelFactory(authService)
        loginViewModel =
            ViewModelProvider(this, loginViewModelFactory).get(LoginViewModel::class.java)

        loginViewModel.loginLiveDataResponse.observe(this, Observer {
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
        })

        binding.btnLogin.setOnClickListener {
            val email = Email(binding.etEmail.text.toString())
            val password = binding.etPassword.text.toString()
            loginViewModel.login(email, password)
        }


        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                email = Email(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                password = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.tvSignUp.setOnClickListener() {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }

    fun displayMessage(message: String) {
        binding.tvLoginMessage.text = message
        binding.tvLoginMessage.visibility = View.VISIBLE
    }

    fun hideMessage() {
        binding.tvLoginMessage.visibility = View.INVISIBLE
    }

}
