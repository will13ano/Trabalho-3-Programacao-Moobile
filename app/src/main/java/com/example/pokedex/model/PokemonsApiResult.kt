package com.example.pokedex.model

import com.example.pokedex.PokemonType

data class PokemonsApiResult (
    val count: Int,
    val previous: String?,
    val next: String?,
    val results: List<PokemonResult>
)
data class PokemonResult(
    val name: String,
    val url: String
)
data class PokemonApiResult(
    val id: Int,
    val name: String,
    val types: List<PokemonTypeSlot>
)
data class PokemonTypeSlot(
    val slot: Int,
    val type: PokemonType
)