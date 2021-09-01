package com.mangoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val registerBtn = findViewById<Button>(R.id.send_btn_register)
        val loginText = findViewById<TextView>(R.id.login_text)
        registerBtn.setOnClickListener {
            performRegister();
        }

        loginText.setOnClickListener {
            val intent = Intent (this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun performRegister() {
        val emailEditText = findViewById<EditText>(R.id.email_field_register)
        val passwordEditText = findViewById<EditText>(R.id.password_field_register)
        val usernameEditText = findViewById<EditText>(R.id.username_field_register)

        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val username = usernameEditText.text.toString()

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please, fill in all the required fields", Toast.LENGTH_SHORT).show()
            return
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                passwordEditText.setText("")
                usernameEditText.setText("")
                emailEditText.setText("")
                Toast.makeText(this, "Registration success", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {
                Log.println(Log.DEBUG,"ERR - pass",password)
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }

}