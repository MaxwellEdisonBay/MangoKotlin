package com.mangoapp.models

class Chat (val id:String,  val latestmessage:String, val timestamp:Long) {
    constructor(): this ("","",-1)
}