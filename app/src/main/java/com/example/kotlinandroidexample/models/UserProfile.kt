package com.example.kotlinandroidexample.models

data class UserProfile(val userId: String, val email: String, val name: String)

fun String.isValidEmail(): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    return emailRegex.matches(this)
}

fun String.isValidPassword(): Boolean = this.length >= 6