package com.example.pokedex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.pokedex.model.User

class ListPokemons : AppCompatActivity() {
    private lateinit var userText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_pokemons)

        initViews()

        getUserIntent()
    }

    private fun initViews() {
        userText = findViewById<TextView>(R.id.userText)
    }

    private fun getUserIntent() {
        val user = intent.getSerializableExtra("USER") as? User
        userText!!.text = user?.name
    }
}