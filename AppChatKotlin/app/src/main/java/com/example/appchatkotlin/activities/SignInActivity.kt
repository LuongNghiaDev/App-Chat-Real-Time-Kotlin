package com.example.appchatkotlin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.appchatkotlin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_in.etEmail
import kotlinx.android.synthetic.main.activity_sign_in.etPassword

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()
        firebaseUser = auth.currentUser!!

        if(firebaseUser != null) {
            val intent = Intent(this@SignInActivity, UsersActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            when {
                TextUtils.isEmpty(email) -> {
                    Toast.makeText(this, "Email can't empty", Toast.LENGTH_LONG).show()
                }
                TextUtils.isEmpty(password) -> {
                    Toast.makeText(this, "Password can't empty", Toast.LENGTH_LONG).show()
                }
                else -> {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) {
                            if(it.isSuccessful){
                                etEmail.setText("")
                                etPassword.setText("")
                                val intent = Intent(this@SignInActivity, UsersActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "Email or password invalid", Toast.LENGTH_LONG).show()
                            }
                        }
                }
            }
        }

        txtBtnSignUp.setOnClickListener {
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

    }
}