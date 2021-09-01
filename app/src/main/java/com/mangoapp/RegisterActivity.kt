package com.mangoapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text

class RegisterActivity : AppCompatActivity() {

    var selectedPhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val registerBtn = findViewById<Button>(R.id.send_btn_register)
        val loginText = findViewById<TextView>(R.id.login_text)
        val addPhotoButton = findViewById<CircleImageView>(R.id.add_photo)

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    val data: Intent? = result.data
                    selectedPhotoUri = data?.data

                    val bitmap: Bitmap? = if (Build.VERSION.SDK_INT < 28) {
                        MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

                    } else {
                        val source = selectedPhotoUri?.let { it1 ->
                            ImageDecoder.createSource(
                                this.contentResolver,
                                it1
                            )
                        }
                        source?.let { it1 -> ImageDecoder.decodeBitmap(it1) }
                    }
                    findViewById<TextView>(R.id.add_photo_text).visibility= View.GONE
                    addPhotoButton.setImageBitmap(bitmap)
                }
            }

        addPhotoButton.setOnClickListener {


            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"

            resultLauncher.launch(intent)

        }


        registerBtn.setOnClickListener {
            performRegister();
        }

        loginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
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
            Toast.makeText(this, "Please, fill in all the required fields", Toast.LENGTH_SHORT)
                .show()
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
                Log.println(Log.DEBUG, "ERR - pass", password)
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }

}