package com.mangoapp.network

import android.content.Context
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.mangoapp.models.User
import org.json.JSONArray
import com.androidnetworking.interfaces.JSONArrayRequestListener
import org.json.JSONObject

import com.androidnetworking.interfaces.JSONObjectRequestListener







class NetworkConnector() {

    fun initialize(context: Context){
        AndroidNetworking.initialize(context);
    }

    fun send_post(user: User, url:String){
        AndroidNetworking.post(url)
            .addBodyParameter("uid",user.uid)
            .addBodyParameter("username",user.username)
            .addBodyParameter("profileImageUrl",user.profileImageUrl)

            .setTag("test")
            .addHeaders("Request-Type", "create-user")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    // do anything with response
                }

                override fun onError(error: ANError) {
                    // handle error
                }
            })
    }

}