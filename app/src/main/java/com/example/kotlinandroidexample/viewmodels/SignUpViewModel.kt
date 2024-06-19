package com.example.kotlinandroidexample.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kotlinandroidexample.models.isValidEmail
import com.example.kotlinandroidexample.models.isValidPassword
import com.example.kotlinandroidexample.services.AuthService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class SignUpViewModel(private val authService: AuthService) : ViewModel() {
    val responseLiveData = MutableLiveData<SignUpResponse>(SignUpResponse.Loading)
    var email: String = ""
    var password: String = ""
    var name: String = ""

    fun signUp() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            e.printStackTrace()
            responseLiveData.value = SignUpResponse.Failure(e.message ?: "Unknown error")
        }
        viewModelScope.launch(exceptionHandler) {
            responseLiveData.value = SignUpResponse.Loading
            if (!email.isValidEmail()) {
                responseLiveData.value = SignUpResponse.Failure("Email is invalid")
                return@launch
            }
            if (!password.isValidPassword()) {
                responseLiveData.value =
                    SignUpResponse.Failure("Password must be at least 6 characters")
                return@launch
            }
            val result = authService.signUp(email, password, name)

            responseLiveData.value = if (result.isSuccess) SignUpResponse.Success
            else SignUpResponse.Failure(result.message ?: "Unknown error")
        }

    }

    sealed class SignUpResponse {
        data object Success : SignUpResponse()
        class Failure(val message: String) : SignUpResponse()
        data object Loading : SignUpResponse()
    }

    @Suppress("UNCHECKED_CAST")
    class SignUpViewModelFactory(private val authService: AuthService) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
                return SignUpViewModel(authService) as T
            }
            throw Exception("Unknown ViewModel class")
        }
    }

}