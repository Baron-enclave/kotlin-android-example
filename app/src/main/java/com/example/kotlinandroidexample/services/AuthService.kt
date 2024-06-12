package com.example.kotlinandroidexample.services

import com.example.kotlinandroidexample.models.Email
import com.example.kotlinandroidexample.models.UserProfile


interface AuthService {
    fun login(email: Email, password: String): LoginResult
    fun logout()
    data class LoginResult(val isSuccess: Boolean, val message: String?, val userProfile: UserProfile?){
        constructor(message: String?):this(false, message, null)
        constructor(userProfile: UserProfile?):this(true, null, userProfile)
    }
}

class FakeAuthService private constructor() : AuthService{

    // try to apply companion object and singleton design pattern
    companion object{
     val instance = FakeAuthService()
    }
    override fun login(email: Email, password: String): AuthService.LoginResult {
        val fakeEmail = Email("baron@enclave.vn")
        val fakePassword = "123qwe"

        if (email != fakeEmail || password != fakePassword){
            return AuthService.LoginResult("Email or password is incorrect")
        }
        return AuthService.LoginResult(UserProfile("1", email, "Baron"))
    }

    override fun logout() {

    }
}

class FileAuthService : AuthService{
    override fun login(email: Email, password: String): AuthService.LoginResult {
        TODO("Not yet implemented")
    }

    override fun logout() {
        TODO("Not yet implemented")
    }
}