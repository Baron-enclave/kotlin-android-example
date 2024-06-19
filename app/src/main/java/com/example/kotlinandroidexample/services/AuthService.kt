package com.example.kotlinandroidexample.services

import android.content.ContentValues
import android.util.Base64
import com.example.kotlinandroidexample.DBHelper
import com.example.kotlinandroidexample.models.Email
import com.example.kotlinandroidexample.models.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.security.SecureRandom
import kotlin.Exception


interface AuthService {
    suspend fun login(email: Email, password: String): LoginResult
    fun logout()
    suspend fun signUp(email: Email, password: String, name: String): SignUpResult
    data class LoginResult(
        val isSuccess: Boolean,
        val message: String?,
        val userProfile: UserProfile?
    ) {
        companion object {
            fun success(userProfile: UserProfile): LoginResult =
                LoginResult(true, null, userProfile)

            fun failure(message: String): LoginResult = LoginResult(false, message, null)

        }
    }

    data class SignUpResult(val isSuccess: Boolean, val message: String?) {
        companion object {
            fun success(): SignUpResult {
                return SignUpResult(isSuccess = true, message = null)
            }

            fun failure(message: String): SignUpResult {
                return SignUpResult(isSuccess = false, message = message)
            }
        }
    }
}

class FakeAuthService : AuthService {
    override suspend fun login(email: Email, password: String): AuthService.LoginResult {
        val fakeEmail = Email("baron@enclave.vn")
        val fakePassword = "123qwe"

        if (email != fakeEmail || password != fakePassword) {
            return AuthService.LoginResult.failure("Email or password is incorrect")
        }
        return AuthService.LoginResult.success(UserProfile("1", email, "Baron"))
    }

    override fun logout() {
    }

    override suspend fun signUp(
        email: Email,
        password: String,
        name: String
    ): AuthService.SignUpResult {
        TODO("Not yet implemented")
    }
}

class SQLiteAuthService private constructor(private val dbHelper: DBHelper) : AuthService {

    // try to apply companion object and singleton design pattern
    companion object {
        private var instance: SQLiteAuthService? = null
        fun getInstance(dbHelper: DBHelper): SQLiteAuthService {
            instance = instance ?: SQLiteAuthService(dbHelper)
            return instance!!
        }
    }


    override suspend fun login(email: Email, password: String): AuthService.LoginResult =
        withContext(Dispatchers.IO) {
            val db = dbHelper.readableDatabase
            try {
                val query =
                    """SELECT ${DBHelper.ID_COL}, 
                        ${DBHelper.NAME_COL}, 
                        ${DBHelper.HASHED_PWD}, 
                        ${DBHelper.SALT_COL} 
                        FROM ${DBHelper.USERS_TABLE_NAME} 
                        WHERE ${DBHelper.EMAIL_COL} = ?"""

                val cursor = db.rawQuery(query, arrayOf(email.value))
                if (cursor.moveToFirst()) {
                    println(cursor.count)
                    val userId = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.ID_COL))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.NAME_COL))
                    val storeSalt = cursor.getBlob(cursor.getColumnIndexOrThrow(DBHelper.SALT_COL))
                    val storeHashedPassword =
                        cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.HASHED_PWD))


                    val hashedPassword = hashPassword(password, storeSalt)
                    if (hashedPassword == storeHashedPassword) {
                        return@withContext AuthService.LoginResult(
                            true,
                            null,
                            UserProfile(userId.toString(), email, name)
                        )
                    }
                }
                cursor.close()
                return@withContext AuthService.LoginResult(
                    false,
                    "Email or password is incorrect",
                    null
                )
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext AuthService.LoginResult(
                    false,
                    "An unknown error occurred",
                    null
                )
            } finally {
                db.close()
            }
        }

    override suspend fun signUp(
        email: Email,
        password: String,
        name: String
    ): AuthService.SignUpResult = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        return@withContext try {
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
                AuthService.SignUpResult.success()
            } else {
                AuthService.SignUpResult.failure("Error inserting user into database")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            AuthService.SignUpResult.failure("Exception: ${e.message}")
        } finally {
            db.close()
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