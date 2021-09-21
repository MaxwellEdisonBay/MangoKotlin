package com.mangoapp.models

import android.widget.TextView
import com.mangoapp.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView

class UserItem(val user: User) : Item<GroupieViewHolder>() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val usernameTextView =
            viewHolder.root.findViewById<TextView>(R.id.username_textview_new_message)
        val userProfileImage =
            viewHolder.root.findViewById<CircleImageView>(R.id.user_image_new_message)

        usernameTextView.text = user.username
        Picasso.get().load(user.profileImageUrl).into(userProfileImage)

    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}