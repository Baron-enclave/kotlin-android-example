package com.example.kotlinandroidexample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinandroidexample.databinding.ActivitySignupBinding
import com.example.kotlinandroidexample.models.Email
import com.example.kotlinandroidexample.models.isValidPassword
import com.example.kotlinandroidexample.services.SQLiteAuthService


class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var tvErrorMessage: TextView

    private val dbHelper = DBHelper(this, null)
    private val authService = SQLiteAuthService(dbHelper)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        tvErrorMessage = binding.tvErrorText
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener() {
            hideMessage()
            val email = Email(binding.etEmail.text.toString())
            val password = binding.etPassword.text.toString()
//            val confirmPassword = binding.etConfirmPassword.text.toString()
            val name = binding.etName.text.toString()
            if (!email.isValidEmail()) {
                displayMessage("Email is invalid")
                return@setOnClickListener
            }
            if (!password.isValidPassword()) {
                displayMessage("Password must be at least 6 characters")
                return@setOnClickListener
            }
//            if (password != confirmPassword){
//                displayMessage("Password and confirm password does not match")
//                return@setOnClickListener
//            }
            authService.signUp(email, password, name)

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