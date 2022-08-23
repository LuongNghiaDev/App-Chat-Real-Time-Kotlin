package com.example.appchatkotlin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.appchatkotlin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.etEmail
import kotlinx.android.synthetic.main.activity_sign_up.etPassword

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        btnSignUp.setOnClickListener {
            val username = etName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val confirmPass = etConfirmPassword.text.toString()

            when {
                TextUtils.isEmpty(username) -> {
                    Toast.makeText(this, "Username can't empty", Toast.LENGTH_LONG).show()
                }
                TextUtils.isEmpty(email) -> {
                    Toast.makeText(this, "Email can't empty", Toast.LENGTH_LONG).show()
                }
                TextUtils.isEmpty(password) -> {
                    Toast.makeText(this, "Password can't empty", Toast.LENGTH_LONG).show()
                }
                TextUtils.isEmpty(confirmPass) -> {
                    Toast.makeText(this, "Confirm Password can't empty", Toast.LENGTH_LONG).show()
                }
                password != confirmPass -> {
                    Toast.makeText(this, "Password not match", Toast.LENGTH_LONG).show()
                }
                else -> {
                    registerUser(username, email, password)
                }
            }
        }

        txtBtnLogin.setOnClickListener {
            val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
            startActivity(intent)
        }

    }

    private fun registerUser(username: String,email:String, password:String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if(it.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    val userId: String = user!!.uid

                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)

                    var hashMap: HashMap<String, String> = HashMap()
                    hashMap.put("userId",userId)
                    hashMap.put("username",username)
                    hashMap.put("profileImage", "")

                    databaseReference.setValue(hashMap).addOnCompleteListener(this) {
                        if(it.isSuccessful) {
                            etName.setText("")
                            etEmail.setText("")
                            etPassword.setText("")
                            etConfirmPassword.setText("")
                            Toast.makeText(this, "Register successfully", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@SignUpActivity, UsersActivity::class.java)
                            startActivity(intent)
                        }
                    }

                }
            }
    }


}