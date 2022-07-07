package com.example.pokedex.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.pokedex.model.Pokemon
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

    fun getFavoritesPokemons(user: User): List<Pokemon> {
        val db = this.readableDatabase

        val query = "SELECT $TABLE_POKEMON.* FROM $TABLE_USER_POKEMON INNER JOIN $TABLE_POKEMON ON $TABLE_USER_POKEMON.$COLUMN_USER_ID = ?"

        val cursor = db.rawQuery(query, arrayOf(user.id.toString()))
        var pokemonList: MutableList<Pokemon> = mutableListOf()
        try {
            while (cursor.moveToNext()) {
                val indexId = cursor.getColumnIndex(COLUMN_POKE_ID)
                val indexName = cursor.getColumnIndex(COLUMN_POKE_NAME)
                val indexImage = cursor.getColumnIndex(COLUMN_POKE_IMAGE)

                val id = cursor.getInt(indexId)
                val name = cursor.getString(indexName)
                val imageUrl = cursor.getString(indexImage)

                var pokemon = Pokemon(id, 0, name)
                pokemon.imageUrl = imageUrl

                pokemonList.add(pokemon)
            }
        } catch (e: Exception) {
            Log.e("ERRO FAVORITE POKEMONS", e.message.toString())
        }

        cursor.close()
        db.close()

        return pokemonList
    }

    fun favoritePokemon(user: User, pokemon: Pokemon) {
        if (!checkPokemon(pokemon)) {
            createPokemon(pokemon)
        }

        val pokemonWithId = getPokemon(pokemon)

        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_POKE_ID, pokemonWithId!!.id)
        values.put(COLUMN_USER_ID, user.id)

        db.insert(TABLE_USER_POKEMON, null, values)
        db.close()
    }

    private fun getPokemon(pokemon: Pokemon): Pokemon? {
        val db = this.readableDatabase
        val columns = arrayOf(COLUMN_POKE_ID, COLUMN_POKE_NAME, COLUMN_POKE_IMAGE)

        val selection = "$COLUMN_POKE_NAME = ?"
        val selectionArgs = arrayOf(pokemon.name)

        val cursor = db.query(TABLE_POKEMON, columns, selection, selectionArgs, null, null, null)
        var findPokemon: Pokemon? = null

        try {
            cursor.moveToFirst()

            val indexId = cursor.getColumnIndex(COLUMN_POKE_ID)
            val indexName = cursor.getColumnIndex(COLUMN_POKE_NAME)
            val indexImage = cursor.getColumnIndex(COLUMN_POKE_IMAGE)

            val id = cursor.getInt(indexId)
            val name = cursor.getString(indexName)
            val imageUrl = cursor.getString(indexImage)

            findPokemon = Pokemon(id, 0, name)
            findPokemon.imageUrl = imageUrl
        } catch (e: Exception) {
            Log.d("Error in GetPokemon: ", e.message.toString())
        }

        cursor.close()
        db.close()

        return findPokemon
    }

    private fun createPokemon(pokemon: Pokemon) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_POKE_NAME, pokemon.name)
        values.put(COLUMN_POKE_IMAGE, pokemon.imageUrl)

        db.insert(TABLE_POKEMON, null, values)
        db.close()
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

        db.delete(TABLE_USER_POKEMON, "$COLUMN_USER_ID = ?", arrayOf(user.id.toString()))
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

    private fun checkPokemon(pokemon: Pokemon): Boolean{
        val columns = arrayOf(COLUMN_POKE_ID)
        val db = this.readableDatabase

        val selection = "$COLUMN_POKE_NAME  = ?"
        val selectionArgs = arrayOf(pokemon.name)

        val cursor = db.query(TABLE_POKEMON, columns, selection, selectionArgs, null, null, null)
        val cursorCount = cursor.count

        cursor.close()
        db.close()

        if (cursorCount > 0)
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

            val userIdIndex = cursor.getColumnIndex(COLUMN_USER_ID)
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