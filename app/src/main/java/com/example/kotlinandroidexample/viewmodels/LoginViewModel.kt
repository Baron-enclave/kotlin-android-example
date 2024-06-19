package com.example.kotlinandroidexample.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.kotlinandroidexample.models.Email
import com.example.kotlinandroidexample.services.AuthService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class LoginViewModel(private val authService: AuthService) : ViewModel() {
    val loginLiveDataResponse = MutableLiveData<LoginResponse>()
    var email: Email = Email("")
    var password: String = ""

    fun login() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            e.printStackTrace()
            loginLiveDataResponse.value = LoginResponse.Failure(e.message ?: "Unknown Error")
        }
        viewModelScope.launch(exceptionHandler) {
            if (!email.isValidEmail()) {
                loginLiveDataResponse.value = LoginResponse.Failure("Email is invalid")
            } else {
                val result = authService.login(email, password)
                loginLiveDataResponse.value =
                    if (result.isSuccess) LoginResponse.Success else LoginResponse.Failure(
                        result.message ?: "Unknown Error"
                    )
            }
        }

    }

    sealed class LoginResponse {
        data object Success : LoginResponse()
        class Failure(val message: String) : LoginResponse()
        data object Loading : LoginResponse()
    }

    @Suppress("UNCHECKED_CAST")
    class LoginViewModelFactory(private val authService: AuthService) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(authService) as T
            }
            throw Exception("Unknown ViewModel class")
        }
    }
}