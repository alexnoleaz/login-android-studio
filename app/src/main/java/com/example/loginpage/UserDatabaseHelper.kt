package com.example.loginpage

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDatabaseHelper(context: Context): SQLiteOpenHelper(context, "Users.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                username TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL
            );
        """
        db?.execSQL(createTable)

        // Initial data
        insertInitialUser(db, "Administrador", "admin", "admin123qwe")
        insertInitialUser(db, "Mantenedor","maintainer", "maintainer123qwe")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun signIn(username: String, password: String): String? {
        val db = this.readableDatabase
        val query = "SELECT * FROM users WHERE username = ? AND password = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))
        val result = if(cursor.moveToFirst())
            cursor.getString(cursor.getColumnIndexOrThrow("name"))
        else null

        cursor.close()
        return result
    }

    private fun insertInitialUser(db: SQLiteDatabase?,name: String, username: String, password: String){
        val values = ContentValues().apply {
            put("name", name)
            put("username", username)
            put("password",password)
        }
        db?.insert("users", null, values)
    }
}