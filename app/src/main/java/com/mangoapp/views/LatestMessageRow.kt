package com.mangoapp.views

import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mangoapp.R
import com.mangoapp.models.ChatMessage
import com.mangoapp.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView

class LatestMessageRow (private val chatMessage: ChatMessage): Item<GroupieViewHolder>(){

    var chatPartnerUser: User? = null

    override fun getLayout(): Int {
        return R.layout.latest_message_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val messageTextView = viewHolder.itemView.findViewById<TextView>(R.id.latest_message_text)
        messageTextView.text = chatMessage.text

        val chatPartnerId: String = if (chatMessage.fromId == FirebaseAuth.getInstance().uid){
            chatMessage.toId
        } else{
            chatMessage.fromId
        }

        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatPartnerUser = snapshot.getValue(User::class.java)
                val usernameTextView = viewHolder.itemView.findViewById<TextView>(R.id.latest_message_username)
                usernameTextView.text = chatPartnerUser?.username
                val userImageView = viewHolder.itemView.findViewById<CircleImageView>(R.id.latest_message_user_image)
                Picasso.get().load(chatPartnerUser?.profileImageUrl).into(userImageView)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}