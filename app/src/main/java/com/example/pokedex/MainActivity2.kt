package com.example.pokedex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.pokedex.fragments.FavoritesFragment
import com.example.pokedex.fragments.PokedexFragment
import com.example.pokedex.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity2 : AppCompatActivity() {
    lateinit var bottomNav: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        loadFragment(PokedexFragment())

        initView()

        bottomNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.pokedex -> loadFragment(PokedexFragment())
                R.id.favorites -> loadFragment(FavoritesFragment())
                R.id.profile -> loadFragment(ProfileFragment())
            }
            true
        }
    }

    private fun initView() {
        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
