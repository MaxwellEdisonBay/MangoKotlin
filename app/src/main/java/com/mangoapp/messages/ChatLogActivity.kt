package com.mangoapp.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.mangoapp.R
import com.mangoapp.expands.ExpandedTextWatcher
import com.mangoapp.models.ChatMessage
import com.mangoapp.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.GroupieViewHolder
import de.hdodenhof.circleimageview.CircleImageView

class ChatLogActivity : AppCompatActivity() {
    companion object {
        const val TAG = "Chatlog"
    }

    val adapter = GroupAdapter<GroupieViewHolder>()
    var toUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        val recyclerView = findViewById<RecyclerView>(R.id.recycle_view_chat_log)
        val sendButton = findViewById<Button>(R.id.btn_chat_log)
        val editText = findViewById<EditText>(R.id.edit_text_chat_log)
        toUser = intent.getParcelableExtra(NewMessageActivity.USER_KEY)
        supportActionBar?.title = toUser?.username

        recyclerView.adapter = adapter
        listenForMessages()

        sendButton.setOnClickListener {
            Log.d(TAG, "Attempt to send message...")
            performSendMessage()
        }

        editText.addTextChangedListener(ExpandedTextWatcher(editText))
    }

    private fun listenForMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                if (chatMessage != null) {
                    Log.d(TAG, chatMessage.text)
                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid){
                        val currentUser = LatestMessagesActivity.currentUser ?:return

                        adapter.add(ChatFromItem(chatMessage.text, currentUser))
                    }
                    else {
                        adapter.add(ChatToItem(chatMessage.text, toUser!!))
                    }
                    val recyclerView = findViewById<RecyclerView>(R.id.recycle_view_chat_log)
                    recyclerView.smoothScrollToPosition(adapter.itemCount-1)

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })
    }

    private fun performSendMessage() {
//        val ref = FirebaseDatabase.getInstance().getReference("/messages").push()


        val editText = findViewById<EditText>(R.id.edit_text_chat_log)
        if (!prepareMessage(editText)) return
        val text = editText.text.toString()


        val recyclerView = findViewById<RecyclerView>(R.id.recycle_view_chat_log)

        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

        val reversedRef = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()



        if (fromId == null || toId == null) return

        val chatMessage =
            ChatMessage(ref.key!!, text, toId, fromId, System.currentTimeMillis() / 1000)
        ref.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Successfully saved a message ${ref.key}")
                editText.setText("")
                recyclerView.smoothScrollToPosition(adapter.itemCount-1)
            }
        reversedRef.setValue(chatMessage)
    }

    private fun prepareMessage(editText: EditText): Boolean {
        val text = editText.text
        val trimmedText = text.trim().toString()
//        Log.d("REGEX RESULT",trimmedText)

        return if (trimmedText=="") {
            editText.setText("")
            false
        } else{
            editText.setText(trimmedText)
            true
        }
    }

    class ChatFromItem(private val text: String, private val user:User) : Item<GroupieViewHolder>() {
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

    class ChatToItem(private val text: String, private val user: User) : Item<GroupieViewHolder>() {
        override fun getLayout(): Int {
            return R.layout.chat_log_to_row
        }

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            val messageText =
                viewHolder.itemView.findViewById<TextView>(R.id.chat_log_to_message_text)

            messageText.text = text
            // load profile image to CircleImageView
            val uri = user.profileImageUrl
            val profileImage =
                viewHolder.itemView.findViewById<CircleImageView>(R.id.chat_log_to_user_image)
            Picasso.get().load(uri).into(profileImage)
        }
    }
}