package com.example.pokedex.model

import java.io.Serializable

data class User(val id: Int=0, val name: String, val email: String, val password: String=" ") : Serializable
