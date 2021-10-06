package com.mangoapp.network

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.widget.EditText
import android.widget.Toast
import com.mangoapp.R
import com.mangoapp.messages.LatestMessagesActivity
import com.mangoapp.models.User
import org.json.JSONObject

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import android.os.Looper
import android.util.Log
import com.google.gson.Gson


class NetworkConnector() {

    companion object {
        val apiURL = "http://api.mango-friends.com/"
        val OK = 200
        val NOT_FOUND = 404
        val BAD_REQUEST = 400
        val INIT = 0
    }

    fun apiSendRegisterData (context: Context, user: User) {
        val request = buildRequest(user, "create-user")

        val client = OkHttpClient();
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.code == OK){
                    Handler(Looper.getMainLooper()).post(Runnable {
                        val activity = context as Activity
                        activity.findViewById<EditText>(R.id.email_field_register).setText("")
                        activity.findViewById<EditText>(R.id.password_field_register).setText("")
                        activity.findViewById<EditText>(R.id.username_field_register).setText("")
                        Log.d("Register", "User has been saved to Firebase DB using Python API successfully")
                        val intent = Intent(context, LatestMessagesActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        activity.startActivity(intent)

                    })

                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Handler(Looper.getMainLooper()).post(Runnable {
                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                })
            }
        })
    }

    private fun buildRequest(dataClass: Any, type:String): Request {
        val jsonified = Gson().toJson(dataClass)
        val jsonObject = JSONObject(jsonified)

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = jsonObject.toString().toRequestBody(mediaType)

        // creating request
        return Request.Builder()
            .url(apiURL)
            .post(body)
//            .addHeader("Request-type", "api-test")
            .addHeader("Request-type", type)
            .build()
    }
}

