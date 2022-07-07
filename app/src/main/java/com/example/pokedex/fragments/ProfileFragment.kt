package com.example.pokedex.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton

import com.example.pokedex.R
import com.example.pokedex.database.DatabaseHelper
import com.example.pokedex.helpers.InputValidation
import com.example.pokedex.model.User
import com.example.pokedex.view.MainActivity2

class ProfileFragment : Fragment(), View.OnClickListener {
    private lateinit var currentUser: User
    private lateinit var nameField: EditText
    private lateinit var emailField: EditText
    private lateinit var changeProfileButton: AppCompatButton
    private lateinit var deleteProfileButton: AppCompatButton

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var inputValidation: InputValidation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        currentUser = arguments?.getSerializable("USER") as User
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        initView(view)
        initObjects()
        initListeners()
        initValues()

        return view
    }

    private fun initValues() {
        nameField.setText(currentUser.name)
        emailField.setText(currentUser.email)
    }

    private fun initListeners() {
        changeProfileButton!!.setOnClickListener(this)
        deleteProfileButton!!.setOnClickListener(this)
    }

    private fun initView(v: View) {
        nameField = v.findViewById(R.id.nameField)
        emailField = v.findViewById(R.id.emailField)
        changeProfileButton = v.findViewById(R.id.changeProfileButton)
        deleteProfileButton = v.findViewById(R.id.deleteProfile)
    }


    private fun initObjects() {
        dbHelper = DatabaseHelper(requireContext())
        inputValidation = InputValidation(requireContext())
    }

    companion object {
        fun newInstance(user: User): ProfileFragment {
            val fragment = ProfileFragment()

            val bundle = Bundle().apply {
                putSerializable("USER", user)
            }

            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.changeProfileButton -> updateProfileOnDatabase()
            R.id.deleteProfile -> confirmDeleteProfile()
        }
    }

    private fun confirmDeleteProfile() {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(R.string.confirmDelete)
        builder.setMessage(R.string.confirmDeleteMessage)
        builder.setPositiveButton(R.string.confirm) {dialog, which ->
            dbHelper.deleteUser(currentUser);
            Toast.makeText(context, getText(R.string.deleteSuccess), Toast.LENGTH_LONG).show()
            val accountsIntent = Intent(null, MainActivity2::class.java)
            startActivity(accountsIntent)
        }
        builder.setNegativeButton(R.string.cancel) {dialog, which ->
            Toast.makeText(context, getText(R.string.deleteCanceled), Toast.LENGTH_SHORT).show()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun updateProfileOnDatabase() {
        if (!inputValidation!!.isInputEditTextFilled(emailField!!)) {
            return
        }
        if (!inputValidation!!.isInputEditTextEmail(emailField!!)) {
            return
        }

        currentUser.email = emailField.text.toString()
        currentUser.name = nameField.text.toString()

        try {
            dbHelper.updateUser(currentUser);
            Toast.makeText(context, getText(R.string.user_update_success), Toast.LENGTH_SHORT).show()
        } catch (e : Exception) {
            Toast.makeText(context, getText(R.string.user_update_error), Toast.LENGTH_SHORT).show()
        }

    }
}