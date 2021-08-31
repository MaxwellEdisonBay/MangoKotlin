package com.mangoapp

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class LoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginBtn = findViewById<Button>(R.id.send_btn_login)
        val backBtn = findViewById<FloatingActionButton>(R.id.login_back_btn)

        loginBtn.setOnClickListener {
            performLogin()

        }

        backBtn.setOnClickListener {
            finish()
        }

    }

    private fun performLogin() {
        val email = findViewById<EditText>(R.id.email_field_login).text.toString()
        val password = findViewById<EditText>(R.id.password_field_login).text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Fill in all the required fields", Toast.LENGTH_SHORT).show()
            return
        }
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnFailureListener {
                Toast.makeText(this, "Failed to sign in", Toast.LENGTH_SHORT).show()
            }
            .addOnSuccessListener {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            }
    }
}