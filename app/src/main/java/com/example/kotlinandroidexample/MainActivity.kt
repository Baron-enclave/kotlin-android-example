package com.example.kotlinandroidexample

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.example.kotlinandroidexample.databinding.ActivityMainBinding
import com.example.kotlinandroidexample.models.Email
import com.example.kotlinandroidexample.services.FakeAuthService
import com.example.kotlinandroidexample.services.AuthService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var email: Email =Email("")
    private var password = ""
    //  Creating FakeAuthService in both HomeActivity and MainActivity isn't necessary
    // Need a DI container to create it once
    private val loginService: AuthService = FakeAuthService.instance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etEmail.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                email = Email(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }
        } )

        binding.etPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                password = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.btnLogin.setOnClickListener {

            if (!email.isValidEmail()){
                binding.tvLoginMessage.text = "Email is invalid"
                binding.tvLoginMessage.visibility = View.VISIBLE
            }
            else{

                binding.tvLoginMessage.visibility = View.INVISIBLE
                var result = loginService.login(email, password)
                if(result.isSuccess){
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                else{
                    if(result.message != null){
                        binding.tvLoginMessage.text = result.message
                        binding.tvLoginMessage.visibility = View.VISIBLE
                    }
                }
            }
        }

    }
}
