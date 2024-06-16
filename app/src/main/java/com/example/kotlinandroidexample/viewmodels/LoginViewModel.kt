package com.example.kotlinandroidexample.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.kotlinandroidexample.models.Email
import com.example.kotlinandroidexample.services.AuthService

class LoginViewModel(private val authService: AuthService) : ViewModel() {
    val loginLiveDataResponse = MutableLiveData<LoginResponse>()

    fun login(email: Email, password: String): LoginResponse {
        try {
            if (!email.isValidEmail()) {
                return LoginResponse.Failure("Email is invalid")
            } else {

                val result = authService.login(email, password)
                return if (result.isSuccess) LoginResponse.Success else LoginResponse.Failure(
                    result.message ?: "Unknown Error"
                )

            }
        } catch (e: Exception) {
            e.printStackTrace()
            return LoginResponse.Failure(e.toString())
        }
    }

    sealed class LoginResponse {
        data object Success : LoginResponse()
        class Failure(val message: String) : LoginResponse()
        data object Loading : LoginResponse()
    }

    class LoginViewModelFactory(private val authService: AuthService) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(authService) as T
            }
            throw Exception("Unknown ViewModel class")
        }
    }
}