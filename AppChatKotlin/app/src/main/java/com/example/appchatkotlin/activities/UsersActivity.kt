package com.example.appchatkotlin.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appchatkotlin.R
import com.example.appchatkotlin.adapter.UserAdapter
import com.example.appchatkotlin.firebase.FireBaseService
import com.example.appchatkotlin.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_users.*


class UsersActivity : AppCompatActivity() {

    var userList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        /*FireBaseService.sharedPref = getSharedPreferences("sharedPref",Context.MODE_PRIVATE)
        val deviceToken = FirebaseMessaging.getInstance().token.toString()
        FireBaseService.token = deviceToken*/

        userRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        imgBack.setOnClickListener {
            onBackPressed()
        }

        imgProfile.setOnClickListener {
            val intent = Intent(this@UsersActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

        getUsersList()
    }

    fun getUsersList() {
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Users")

        /*var userId = firebase.uid
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/$userId")*/

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                /*val currentUser = snapshot.getValue(User::class.java)
                if (currentUser!!.profileImage == ""){
                    imgProfile.setImageResource(R.drawable.profile_image)
                }else{
                    Glide.with(this@UsersActivity).load(currentUser.profileImage).into(imgProfile)
                }*/

                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val user = dataSnapShot.getValue(User::class.java)

                    if (!user!!.userId.equals(firebase.uid)) {

                        userList.add(user)
                    }
                }

                val userAdapter = UserAdapter(this@UsersActivity, userList)

                userRecyclerView.adapter = userAdapter
            }

        })
    }
}