package com.mangoapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class User (val uid:String, val username: String, val profileImageUrl: String): Parcelable {
    constructor() : this ("","","")

}