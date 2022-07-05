package com.example.pokedex.helpers

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class InputValidation (private val context: Context) {
    fun isInputEditTextFilled(textInputEditText: EditText): Boolean {
        val value = textInputEditText.text.toString().trim()
        if (value.isEmpty()) {
            hideKeyboardFrom(textInputEditText)
            return false
        }
        return true
    }

    fun isInputEditTextEmail(textInputEditText: EditText): Boolean {
        val value = textInputEditText.text.toString().trim()
        if (value.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            hideKeyboardFrom(textInputEditText)
            return false
        }
        return true
    }

    fun isInputEditTextMatches(textInputEditText1: EditText, textInputEditText2: EditText): Boolean {
        val value1 = textInputEditText1.text.toString().trim()
        val value2 = textInputEditText2.text.toString().trim()
        if (!value1.contentEquals(value2)) {
            hideKeyboardFrom(textInputEditText2)
            return false
        }
        return true
    }


    private fun hideKeyboardFrom(view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }
}