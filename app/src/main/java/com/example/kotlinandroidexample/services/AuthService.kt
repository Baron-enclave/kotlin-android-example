package com.example.kotlinandroidexample.services

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Base64
import com.example.kotlinandroidexample.DBHelper
import com.example.kotlinandroidexample.models.Email
import com.example.kotlinandroidexample.models.UserProfile
import java.security.MessageDigest
import java.security.SecureRandom
import kotlin.Exception


interface AuthService {
    fun login(email: Email, password: String): LoginResult
    fun logout()
    data class LoginResult(
        val isSuccess: Boolean,
        val message: String?,
        val userProfile: UserProfile?
    ) {
        constructor(message: String?) : this(false, message, null)
        constructor(userProfile: UserProfile?) : this(true, null, userProfile)
    }
}

class FakeAuthService() : AuthService {
    override fun login(email: Email, password: String): AuthService.LoginResult {
        val fakeEmail = Email("baron@enclave.vn")
        val fakePassword = "123qwe"

        if (email != fakeEmail || password != fakePassword) {
            return AuthService.LoginResult("Email or password is incorrect")
        }
        return AuthService.LoginResult(UserProfile("1", email, "Baron"))
    }

    override fun logout() {
    }
}

class SQLiteAuthService(private val dbHelper: DBHelper) : AuthService {

    // try to apply companion object and singleton design pattern
    companion object {
        private var instance: FakeAuthService? = null
        fun getInstance(dbHelper: DBHelper): SQLiteAuthService {
            return SQLiteAuthService(dbHelper)
        }
    }


    override fun login(email: Email, password: String): AuthService.LoginResult {
        val db = dbHelper.readableDatabase
        try {
            val query =
                "SELECT ${DBHelper.ID_COL}, ${DBHelper.NAME_COL}, ${DBHelper.HASHED_PWD}, ${DBHelper.SALT_COL} FROM ${DBHelper.USERS_TABLE_NAME} WHERE ${DBHelper.EMAIL_COL} = ?"

            val cursor = db.rawQuery(query, arrayOf(email.value))
            if (cursor.moveToFirst()) {
                val userId = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.ID_COL))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.NAME_COL))
                val storeSalt = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.SALT_COL))
                val storeHashedPassword =
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.HASHED_PWD))

                val saltDecode = Base64.decode(
                    cursor.getString(cursor.getColumnIndexOrThrow("salt")),
                    Base64.NO_WRAP
                )
                val hashedPassword = hashPassword(password, saltDecode)
                if (hashedPassword == storeHashedPassword) {
                    return AuthService.LoginResult(
                        true,
                        null,
                        UserProfile(userId.toString(), email, name)
                    )
                }
            }
            cursor.close()
            return AuthService.LoginResult(
                false,
                "Email or password is incorrect",
                null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return AuthService.LoginResult(
                false,
                "An unknown error occurred",
                null
            )
        } finally {
            db.close()
        }
    }

    fun signUp(email: Email, password: String, name: String): SignUpResult {
        val db = dbHelper.writableDatabase
        return try {
            val salt = generateSalt()
            val hashedPassword = hashPassword(password, salt)

            val contentValues = ContentValues().apply {
                put(DBHelper.EMAIL_COL, email.value)
                put(DBHelper.NAME_COL, name)
                put(DBHelper.HASHED_PWD, hashedPassword)
                put(DBHelper.SALT_COL, salt)
            }

            val newRowId = db.insert(DBHelper.USERS_TABLE_NAME, null, contentValues)
            if (newRowId != -1L) {
                SignUpResult.Success()
            } else {
                SignUpResult.Failure("Error inserting user into database")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            SignUpResult.Failure("Exception: ${e.message}")
        } finally {
            db.close()
        }
    }

    data class SignUpResult(val isSuccess: Boolean, val message: String?) {
        companion object {
            fun Success(): SignUpResult {
                return SignUpResult(isSuccess = true, message = null)
            }

            fun Failure(message: String): SignUpResult {
                return SignUpResult(isSuccess = false, message = message)
            }
        }
    }

    override fun logout() {
        TODO("Not yet implemented")
    }

    private fun hashPassword(password: String, salt: ByteArray): String {
        val md = MessageDigest.getInstance("SHA-256")
        md.update(salt)
        val hashedPassword = md.digest(password.toByteArray())
        return Base64.encodeToString(hashedPassword, Base64.NO_WRAP)
    }

    private fun generateSalt(): ByteArray {
        val salt = ByteArray(16)
        SecureRandom().nextBytes(salt)
        return salt
    }
}