package com.mangoapp.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.mangoapp.R
import com.mangoapp.models.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.GroupieViewHolder

class ChatLogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = user?.username

        val adapter = GroupAdapter<GroupieViewHolder>()
        val recyclerViewChatLog = findViewById<RecyclerView>(R.id.recycle_view_chat_log)
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        recyclerViewChatLog.adapter = adapter

    }
}

class ChatFromItem : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_log_from_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    }
}

class ChatToItem : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_log_to_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    }
}