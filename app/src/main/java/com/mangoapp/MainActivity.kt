package com.mangoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        val email = findViewById<EditText>(R.id.email_field_register).text.toString()
        val password = findViewById<EditText>(R.id.password_field_register).text.toString()
        val username = findViewById<EditText>(R.id.username_field_register).text.toString()

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please, fill in all the required fields", Toast.LENGTH_SHORT).show()
            return
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to create a user", Toast.LENGTH_SHORT).show()
            }
    }

}