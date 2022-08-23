package com.example.appchatkotlin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.appchatkotlin.R
import com.example.appchatkotlin.RetrofitInstance
import com.example.appchatkotlin.adapter.ChatAdapter
import com.example.appchatkotlin.model.Chat
import com.example.appchatkotlin.model.NotificationData
import com.example.appchatkotlin.model.PushNotification
import com.example.appchatkotlin.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_users.*
import kotlinx.android.synthetic.main.activity_users.imgBack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {

    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null

    var chatList = ArrayList<Chat>()
    var topic = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        imgBack.setOnClickListener {
            onBackPressed()
        }

        chatRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        var intent = getIntent()
        var userId = intent.getStringExtra("userId")
        var username = intent.getStringExtra("username")

        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId!!)

        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val user = snapshot.getValue(User::class.java)
                tvUserName.text = user!!.username
                /*if (user.profileImage == "") {
                    imgProfileChat.setImageResource(R.drawable.profile_image)
                } else {
                    Glide.with(this@ChatActivity).load(user.profileImage).into(imgProfileChat)
                }*/
            }
        })

        btnSendMessage.setOnClickListener {
            var message:String = etMessage.text.toString()

            if(TextUtils.isEmpty(message)) {
                Toast.makeText(this,"Message can't empty", Toast.LENGTH_LONG).show()
                etMessage.setText("")
            } else {
                sendMessage(firebaseUser!!.uid, userId, message)
                etMessage.setText("")
                /*topic = "/topics/$userId"
                PushNotification(NotificationData(username!!, message),
                    topic).also {
                    sendNotification(it)
                }*/
            }
        }

        readMessage(firebaseUser!!.uid, userId)
    }

    private fun sendMessage(senderId: String, receiverId:String, message:String) {
        var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference()

        var hashMap:HashMap<String, String> = HashMap()
        hashMap.put("senderId", senderId)
        hashMap.put("receiverId", receiverId)
        hashMap.put("message", message)

        reference!!.child("Chat").push().setValue(hashMap)

    }

    fun readMessage(senderId: String, receiverId: String) {
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Chat")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val chat = dataSnapShot.getValue(Chat::class.java)

                    if (chat!!.senderId.equals(senderId) && chat!!.receiverId.equals(receiverId) ||
                        chat!!.senderId.equals(receiverId) && chat!!.receiverId.equals(senderId)
                    ) {
                        chatList.add(chat)
                    }
                }

                val chatAdapter = ChatAdapter(this@ChatActivity, chatList)

                chatRecyclerView.adapter = chatAdapter
            }
        })
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Toast.makeText(this@ChatActivity, "Response ${Gson().toJson(response)}", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@ChatActivity, response.errorBody().toString(), Toast.LENGTH_LONG).show()
            }
        }catch (e:Exception) {
            Toast.makeText(this@ChatActivity, e.message, Toast.LENGTH_LONG).show()
        }
    }

}