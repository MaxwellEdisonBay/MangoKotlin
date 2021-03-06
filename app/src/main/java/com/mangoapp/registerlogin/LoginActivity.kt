package com.mangoapp.registerlogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.mangoapp.R
import com.mangoapp.messages.LatestMessagesActivity

class LoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginBtn = findViewById<Button>(R.id.send_btn_login)
        val backBtn = findViewById<MaterialButton>(R.id.login_back_btn)

        loginBtn.setOnClickListener {
            performLogin()

        }

        backBtn.setOnClickListener {
            finish()
        }

    }

    private fun performLogin() {
        val emailEdittext = findViewById<EditText>(R.id.email_field_login)
        val email = emailEdittext.text.toString()
        val passwordEdittext = findViewById<EditText>(R.id.password_field_login)
        val password = passwordEdittext.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Fill in all the required fields", Toast.LENGTH_SHORT).show()
            return
        }
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnFailureListener {
                Toast.makeText(this, it.message , Toast.LENGTH_SHORT).show()
            }
            .addOnSuccessListener {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                passwordEdittext.setText("")
                emailEdittext.setText("")

                val intent = Intent (this, LatestMessagesActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
    }
}