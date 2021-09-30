package com.mangoapp.registerlogin

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.androidnetworking.AndroidNetworking
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.mangoapp.R
import com.mangoapp.models.User
import com.mangoapp.messages.LatestMessagesActivity
import com.mangoapp.network.NetworkConnector
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class RegisterActivity : AppCompatActivity() {

    var selectedPhotoUri: Uri? = null
    val networkConnector = NetworkConnector()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        networkConnector.initialize(this)
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
                    findViewById<TextView>(R.id.add_photo_text).visibility = View.GONE
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
                Toast.makeText(this, "Registration success", Toast.LENGTH_SHORT).show()
                uploadImageToFirebaseStorage()

            }
            .addOnFailureListener {
                Log.println(Log.DEBUG, "ERR - pass", password)
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener { it ->
                Log.d("Register", "Success ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    saveUserToFirebaseDatabase(it.toString())
                    //TODO create a success toast

                }
            }
            .addOnFailureListener {
                //TODO create a failure toast
            }
    }

//    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
//        val uid = FirebaseAuth.getInstance().uid ?: ""
//        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
//        val username = findViewById<EditText>(R.id.username_field_register).text.toString()
//        ref.setValue(User(uid, username, profileImageUrl))
//            .addOnSuccessListener {
//                Log.d("Register", "Saved user to Firebase DB")
//                findViewById<EditText>(R.id.username_field_register).setText("")
//                findViewById<EditText>(R.id.email_field_register).setText("")
//                findViewById<EditText>(R.id.password_field_register).setText("")
//
//                val intent = Intent(this, LatestMessagesActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
//                startActivity(intent)
//                //TODO create a success toast
//            }
//            .addOnFailureListener {
//                //TODO create a failure toast
//            }
//    }
    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
//        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val username = findViewById<EditText>(R.id.username_field_register).text.toString()
//        ref.setValue(User(uid, username, profileImageUrl))
//            .addOnSuccessListener {
//                Log.d("Register", "Saved user to Firebase DB")
        val user = User(uid,username,profileImageUrl)
        networkConnector.send_post(user, "http://api.mango-friends.com/")
                findViewById<EditText>(R.id.username_field_register).setText("")
                findViewById<EditText>(R.id.email_field_register).setText("")
                findViewById<EditText>(R.id.password_field_register).setText("")

                val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
//            }
//            .addOnFailureListener {
//                //TODO create a failure toast
//            }
    }

}
