package com.example.pokedex.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.*
import com.example.pokedex.model.Pokemon

class PokedexFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pokedex, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.rvPokemons)
        Thread(Runnable {
            loadPokemon()
        }).start()
    }

    private fun loadPokemon() {
        val pokemonsApiResult = PokemonRepository.listPokemons()

        pokemonsApiResult?.results?.let {
            val pokemons: List<Pokemon?> = it.map { pokemonResult->
                val number = pokemonResult.url
                    .replace("https://pokeapi.co/api/v2/pokemon/", "")
                    .replace("/", "").toInt()

                val pokemonApiResult = PokemonRepository.getPokemon(number)

                pokemonApiResult?.let{
                    Pokemon(0,
                        pokemonApiResult.id,
                        pokemonApiResult.name,
                        pokemonApiResult.types.map { type ->
                            type.type
                        }
                    )
                }
            }

            val layoutManager = LinearLayoutManager(context)

            recyclerView.post{
                recyclerView.layoutManager = layoutManager
                recyclerView.adapter = PokemonAdapter(pokemons)
            }
        }
    }
}