package com.mangoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val usernameEditText = findViewById<EditText>(R.id.username_field_register)
        val emailEditText = findViewById<EditText>(R.id.email_field_register)
        val passwordEditText = findViewById<EditText>(R.id.password_field_register)

        val registerBtn = findViewById<Button>(R.id.send_btn_register)
        val loginText = findViewById<TextView>(R.id.login_text)
        registerBtn.setOnClickListener {

        }

        loginText.setOnClickListener {
            val intent = Intent (this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

}