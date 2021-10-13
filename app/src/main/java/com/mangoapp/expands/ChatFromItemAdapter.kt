package com.mangoapp.expands

import android.widget.TextView
import com.mangoapp.R
import com.mangoapp.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView

class ChatFromItemAdapter(private val text: String, private val user: User) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_log_from_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val messageText =
            viewHolder.itemView.findViewById<TextView>(R.id.chat_log_from_message_text)
        val uri = user.profileImageUrl
        val profileImage =
            viewHolder.itemView.findViewById<CircleImageView>(R.id.chat_log_from_user_image)
        Picasso.get().load(uri).into(profileImage)
        messageText.text = text
    }
}