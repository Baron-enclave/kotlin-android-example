package com.example.kotlinandroidexample.models

data class UserProfile(val userId: String, val email: Email, val name: String) {
}

//try using inline classes
@JvmInline
value class Email(private val email: String){
    fun isValidEmail(): Boolean{
        return email.isValidEmail();
    }
}

// Should implement this logic inside Email inline class. I create an extension to learn
private fun String.isValidEmail(): Boolean{
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    return emailRegex.matches(this)
}