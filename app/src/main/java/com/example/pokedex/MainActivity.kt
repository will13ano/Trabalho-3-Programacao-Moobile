package com.example.pokedex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.pokedex.database.DatabaseHelper
import com.example.pokedex.helpers.InputValidation

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val activity = this@MainActivity

    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var loginButton: Button
    private lateinit var createButton: Button

    private lateinit var inputValidation: InputValidation
    private lateinit var databaseHelper: DatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        initListeners()

        initObjects()
    }

    private fun initViews() {
        emailField = findViewById<EditText>(R.id.emailField)
        passwordField = findViewById<EditText>(R.id.passwordField)
        loginButton = findViewById<Button>(R.id.loginButton)
        createButton = findViewById<Button>(R.id.createButton)
    }

    private fun initListeners() {
        loginButton!!.setOnClickListener(this)
        createButton!!.setOnClickListener(this)
    }

    private fun initObjects() {
        databaseHelper = DatabaseHelper(activity)
        inputValidation = InputValidation(activity)
    }

    private fun verifyFromSQLite() {

        if (!inputValidation!!.isInputEditTextFilled(emailField!!)) {
            return
        }
        if (!inputValidation!!.isInputEditTextEmail(emailField!!)) {
            return
        }
        if (!inputValidation!!.isInputEditTextFilled(passwordField!!)) {
            return
        }

        val user = databaseHelper!!.checkUser(emailField!!.text.toString().trim { it <= ' ' }, passwordField!!.text.toString().trim { it <= ' ' })
        if (user != null) {
            val accountsIntent = Intent(activity, MainActivity2::class.java)
            accountsIntent.putExtra("USER", user)
            emptyInputEditText()
            startActivity(accountsIntent)
        } else {
            // Snack Bar to show success message that record is wrong
             Toast.makeText(this@MainActivity, getString(R.string.error_valid_email_password), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.loginButton -> verifyFromSQLite()
            R.id.createButton -> {
                val intentRegister = Intent(applicationContext, CreateActivity::class.java)
                startActivity(intentRegister)
            }
        }
    }

    private fun emptyInputEditText() {
        emailField!!.text = null
        passwordField!!.text = null
    }
}