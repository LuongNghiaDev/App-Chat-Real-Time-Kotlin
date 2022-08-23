package com.example.appchatkotlin.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appchatkotlin.R
import com.example.appchatkotlin.activities.ChatActivity
import com.example.appchatkotlin.model.Chat
import com.example.appchatkotlin.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_user.view.*

class ChatAdapter(private val context:Context, private val chatList: ArrayList<Chat>)
    :RecyclerView.Adapter<ChatAdapter.MyHolder>(){

    private val MESSAGE_TYPE_LEFT = 0
    private val MESSAGE_TYPE_RIGHT = 1
    var firebaseUser: FirebaseUser? = null

    class MyHolder(view: View): RecyclerView.ViewHolder(view) {
        val txtMessage:TextView = view.findViewById(R.id.tvMessage)
        val image:CircleImageView = view.findViewById(R.id.userImage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.MyHolder {
        if(viewType == MESSAGE_TYPE_RIGHT) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_right, parent, false)
            return MyHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_left, parent, false)
            return MyHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ChatAdapter.MyHolder, position: Int) {
        val chat = chatList[position]
        holder.txtMessage.text = chat.message

    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        if(chatList[position].senderId == firebaseUser!!.uid) {
            return MESSAGE_TYPE_RIGHT
        } else {
            return MESSAGE_TYPE_LEFT
        }
    }

}