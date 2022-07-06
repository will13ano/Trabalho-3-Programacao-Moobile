package com.example.pokedex.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.pokedex.model.User

class DatabaseHelper(context: Context) : SQLiteOpenHelper (context, DATABASE_NAME, null, DATABASE_VERSION){
    private val CREATE_USER_TABLE = ("CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT );")

    private val CREATE_POKEMON_TABLE = ( "CREATE TABLE " + TABLE_POKEMON + "("
            + COLUMN_POKE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_POKE_NAME + " TEXT,"
            + COLUMN_POKE_IMAGE + " TEXT );" )

    private val CREATE_USER_POKEMON_TABLE = ("CREATE TABLE " + TABLE_USER_POKEMON + "("
            + COLUMN_USER_ID + " INTEGER , " + COLUMN_POKE_ID + " INTEGER,"
            + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USER + "(Id),"
            + "FOREIGN KEY(" + COLUMN_POKE_ID + ") REFERENCES " + TABLE_POKEMON + "(Id));")

    private val DROP_USER_TABLE = "DROP TABLE IF EXISTS $TABLE_USER"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_USER_TABLE)
        db.execSQL(CREATE_POKEMON_TABLE)
        db.execSQL(CREATE_USER_POKEMON_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL(DROP_USER_TABLE)

        onCreate(db)
    }

    fun addUser(user: User) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_USER_NAME, user.name)
        values.put(COLUMN_USER_EMAIL, user.email)
        values.put(COLUMN_USER_PASSWORD, user.password)

        db.insert(TABLE_USER, null, values)
        db.close()
    }

    fun updateUser(user: User) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_USER_NAME, user.name)
        values.put(COLUMN_USER_EMAIL, user.email)
        values.put(COLUMN_USER_PASSWORD, user.password)

        db.update(TABLE_USER, values, "$COLUMN_USER_ID = ?", arrayOf(user.id.toString()))
        db.close()
    }

    fun deleteUser(user: User) {
        val db = this.writableDatabase

        db.delete(TABLE_USER, "$COLUMN_USER_ID = ?", arrayOf(user.id.toString()))
        db.close()
    }

    fun checkUser(email: String): Boolean{
        val columns = arrayOf(COLUMN_USER_ID)
        val db = this.readableDatabase

        val selection = "$COLUMN_USER_EMAIL = ?"
        val selectionArgs = arrayOf(email)

        val cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null)
        val cursorCount = cursor.count

        cursor.close()
        db.close()

        if (cursorCount > 0 )
            return true

        return false
    }

    fun checkUser(email: String, password: String): User? {
        val columns = arrayOf(COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_USER_EMAIL, COLUMN_USER_PASSWORD)
        val db = this.readableDatabase

        val selection = "$COLUMN_USER_EMAIL = ? AND $COLUMN_USER_PASSWORD = ?"
        val selectionArgs = arrayOf(email, password)

        val cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null)
        var user: User? = null

        try {
            cursor.moveToFirst()

            val userIdIndex = cursor.getColumnIndexOrThrow(COLUMN_USER_ID)
            val nameIndex = cursor.getColumnIndex(COLUMN_USER_NAME)
            val emailIndex = cursor.getColumnIndex(COLUMN_USER_EMAIL)
            val passwordIndex = cursor.getColumnIndex(COLUMN_USER_PASSWORD)

            val userId = cursor.getInt(userIdIndex)
            val name = cursor.getString(nameIndex)
            val email = cursor.getString(emailIndex)
            val password = cursor.getString(passwordIndex)


            user = User(userId, name, email, password)
        } catch (exception: Exception) {
            Log.d("Error in UserCheck: ", exception.message.toString())
        }

        cursor.close()
        db.close()
        return user
    }

    companion object {
        // Database Version
        private const val DATABASE_VERSION = 1

        // Database Name
        private const val DATABASE_NAME = "PokedexApp.db"

        // User table name
        private const val TABLE_USER = "user"

        // User Table Columns names
        private const val COLUMN_USER_ID = "user_id"
        private const val COLUMN_USER_NAME = "user_name"
        private const val COLUMN_USER_EMAIL = "user_email"
        private const val COLUMN_USER_PASSWORD = "user_password"

        // Pokemon table name
        private const val TABLE_POKEMON = "pokemon"

        // Pokemon Table Columns names
        private const val COLUMN_POKE_ID = "pokemon_id"
        private const val COLUMN_POKE_NAME = "pokemon_name"
        private const val COLUMN_POKE_IMAGE = "pokemon_image"

        // User Pokemon table
        private const val TABLE_USER_POKEMON = "user_pokemon"
    }
}