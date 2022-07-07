package com.example.pokedex.model

data class Pokemon(
    val id: Int=0,
    val number: Int,
    val name: String,
    val types: List<PokemonType>?=null
    ) {
        val formattedName = name.capitalize()
        val formattedNumber = number.toString().padStart(3, '0')
        var imageUrl = "https://assets.pokemon.com/assets/cms2/img/pokedex/full/$formattedNumber.png"
    }
