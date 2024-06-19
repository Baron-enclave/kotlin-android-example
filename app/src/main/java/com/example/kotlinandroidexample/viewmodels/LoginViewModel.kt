package com.example.kotlinandroidexample.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.kotlinandroidexample.models.isValidEmail
import com.example.kotlinandroidexample.services.AuthService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class LoginViewModel(private val authService: AuthService) : ViewModel() {
    val loginLiveDataResponse = MutableLiveData<LoginResponse>()
    val emailLiveData = MutableLiveData<String>("")
    val passwordLiveData = MutableLiveData<String>("")

    fun login() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            e.printStackTrace()
            loginLiveDataResponse.value = LoginResponse.Failure(e.message ?: "Unknown Error")
        }
        viewModelScope.launch(exceptionHandler) {

            if (emailLiveData.value?.isValidEmail() != true) {
                loginLiveDataResponse.value = LoginResponse.Failure("Email is invalid")
            } else {
                Log.d(
                    "LoginViewModel",
                    "Email: ${emailLiveData.value} Password: ${passwordLiveData.value}"
                )
                val result =
                    authService.login(emailLiveData.value ?: "", passwordLiveData.value ?: "")
                loginLiveDataResponse.value =
                    if (result.isSuccess) LoginResponse.Success else LoginResponse.Failure(
                        result.message ?: "Unknown Error"
                    )
            }
        }
    }

    fun onEmailChanged(value: String) {
//        this.email = value
        emailLiveData.value = value
    }

    fun onPasswordChanged(value: String) {
        passwordLiveData.value = value
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