package com.example.pokedex.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.pokedex.R
import com.example.pokedex.database.DatabaseHelper
import com.example.pokedex.helpers.InputValidation
import com.example.pokedex.model.User

class CreateActivity : AppCompatActivity(), View.OnClickListener {
    private val activity = this@CreateActivity

    private lateinit var nameField: EditText
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var passwordConfirmField: EditText
    private lateinit var haveAccountButton: Button
    private lateinit var createButton: Button

    private lateinit var inputValidation: InputValidation
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        initViews()

        initListeners()

        initObjects()
    }

    private fun initViews() {
        nameField = findViewById<EditText>(R.id.nameField)
        emailField = findViewById<EditText>(R.id.emailField)
        passwordField = findViewById<EditText>(R.id.passwordField)
        passwordConfirmField = findViewById<EditText>(R.id.passwordConfirmField)
        haveAccountButton = findViewById<Button>(R.id.haveAccountButton)
        createButton = findViewById<Button>(R.id.createButton)
    }

    private fun initListeners() {
        haveAccountButton!!.setOnClickListener(this)
        createButton!!.setOnClickListener(this)
    }

    private fun initObjects() {
        databaseHelper = DatabaseHelper(activity)
        inputValidation = InputValidation(activity)
    }

    private fun postDataToSQLite() {
        if (!inputValidation!!.isInputEditTextFilled(nameField)) {
            return
        }
        if (!inputValidation!!.isInputEditTextFilled(emailField)) {
            return
        }
        if (!inputValidation!!.isInputEditTextEmail(emailField)) {
            return
        }
        if (!inputValidation!!.isInputEditTextFilled(passwordField)) {
            return
        }
        if (!inputValidation!!.isInputEditTextMatches(passwordField, passwordConfirmField)) {
            return
        }
        if (!databaseHelper!!.checkUser(emailField!!.text.toString().trim())) {
            var user = User(name = nameField!!.text.toString().trim(),
                email = emailField!!.text.toString().trim(),
                password = passwordField!!.text.toString().trim())
            databaseHelper!!.addUser(user)

            makeShortToast(getString(R.string.user_create_success_message))
            emptyInputEditText()
            navigateToMain()
        } else {
            // Snack Bar to show error message that record already exists
            makeShortToast(getString(R.string.error_email_exists))
        }
    }

    private fun makeShortToast(message: String) {
        Toast.makeText(this@CreateActivity, message, Toast.LENGTH_SHORT).show()
    }
    override fun onClick(v: View) {
        when (v.id) {
            R.id.createButton -> postDataToSQLite()
            R.id.haveAccountButton -> navigateToMain()
        }
    }

    private fun navigateToMain() {
        val intentRegister = Intent(null, MainActivity::class.java)
        startActivity(intentRegister)
    }

    private fun emptyInputEditText() {
        nameField!!.text = null
        emailField!!.text = null
        passwordField!!.text = null
        passwordConfirmField!!.text = null
    }
}