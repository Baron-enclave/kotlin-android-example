package com.example.kotlinandroidexample.helpers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.beginTransaction()
        try {
            val query = """CREATE TABLE $USERS_TABLE_NAME (
            $ID_COL INTEGER PRIMARY KEY AUTOINCREMENT,
            $EMAIL_COL TEXT NOT NULL,
            $HASHED_PWD TEXT NOT NULL,
            $SALT_COL BLOB NOT NULL,
            $NAME_COL TEXT NOT NULL
            )"""
            db.execSQL(query)

//            val insertSQL = """INSERT INTO $USERS_TABLE_NAME ($EMAIL_COL, $HASHED_PWD)
//                |VALUES(?, ?)
//            """.trimMargin()
//            val statement = db.compileStatement(insertSQL)
//            statement.bindString(1, "baron@enclave.vn")
//            statement.bindString(2, "123qwe")
//            statement.executeInsert()
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            println(e.printStackTrace())
        } finally {
            db.endTransaction()
        }


    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $USERS_TABLE_NAME")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "ANDROID_DEMO"
        private const val DATABASE_VERSION = 3
        const val USERS_TABLE_NAME = "users"
        const val NAME_COL = "name"
        const val ID_COL = "id"
        const val EMAIL_COL = "email"
        const val HASHED_PWD = "hashed_password"
        const val SALT_COL = "salt"

    }
}
