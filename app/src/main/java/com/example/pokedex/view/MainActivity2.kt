package com.example.pokedex.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.pokedex.R
import com.example.pokedex.fragments.FavoritesFragment
import com.example.pokedex.fragments.PokedexFragment
import com.example.pokedex.fragments.ProfileFragment
import com.example.pokedex.model.User
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity2 : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        loadFragment(PokedexFragment())

        getUser()

        initView()

        bottomNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.pokedex -> loadFragment(PokedexFragment())
                R.id.favorites -> loadFragment(FavoritesFragment())
                R.id.profile -> loadFragment(ProfileFragment.newInstance(currentUser))
            }
            true
        }
    }

    private fun getUser() {
        currentUser = (intent.getSerializableExtra("USER")as? User)!!
    }

    private fun initView() {
        bottomNav = findViewById(R.id.bottomNav)
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
