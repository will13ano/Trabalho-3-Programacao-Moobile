package com.example.pokedex.model

import java.io.Serializable

data class User(val id: Int=0, var name: String, var email: String, val password: String=" ") : Serializable
