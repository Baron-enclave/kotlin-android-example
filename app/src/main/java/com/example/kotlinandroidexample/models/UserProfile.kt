package com.example.kotlinandroidexample.models

data class UserProfile(val userId: String, val email: Email, val name: String)

//try using inline classes
@JvmInline
value class Email(val value: String) {
    fun isValidEmail(): Boolean {
        return value.isValidEmail()
    }
}

// Should implement this logic inside Email inline class. I create an extension to learn
private fun String.isValidEmail(): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    return emailRegex.matches(this)
}

fun String.isValidPassword(): Boolean = this.length >= 6