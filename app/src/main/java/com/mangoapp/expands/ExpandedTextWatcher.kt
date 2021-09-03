package com.mangoapp.expands
import android.text.Editable

import android.widget.EditText

import android.text.TextWatcher
import android.util.Log


class ExpandedTextWatcher(var et: EditText) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        var converted = s.toString()
        if (converted==("\n") || converted== " ") {
            converted = ""
            et.setText(converted)
        }
    }

    override fun afterTextChanged(s: Editable) {

    }

}