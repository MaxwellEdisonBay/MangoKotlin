package com.mangoapp.models

class ChatMessage (val id:String, val text: String, val toId:String, val fromId:String, val timestamp:Long) {
    constructor(): this ("","","","",-1)
}