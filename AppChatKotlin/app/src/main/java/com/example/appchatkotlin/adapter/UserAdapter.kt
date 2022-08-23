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
import com.example.appchatkotlin.model.User
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter(private val context:Context, private val userList: ArrayList<User>)
    :RecyclerView.Adapter<UserAdapter.MyHolder>(){

    class MyHolder(view: View): RecyclerView.ViewHolder(view) {
        val txtName:TextView = view.findViewById(R.id.userName)
        val txtTemp:TextView = view.findViewById(R.id.temp)
        val image:CircleImageView = view.findViewById(R.id.userImage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: UserAdapter.MyHolder, position: Int) {
        val user = userList[position]
        holder.txtName.text = user.username
        Glide.with(context).load(user.profileImage).placeholder(R.drawable.profile_image).into(holder.image)

        holder.itemView.layoutUser.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("userId", user.userId)
            intent.putExtra("username", user.username)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }


}