package com.example.foodOrderAndTrackingApp.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.foodOrderAndTrackingApp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;
    private lateinit var db: FirebaseFirestore

    private val TAG = "RegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        findViewById<Button>(R.id.register_button).setOnClickListener {
            val userName = findViewById<EditText>(R.id.username).text
            val email = findViewById<EditText>(R.id.email).text
            val password = findViewById<EditText>(R.id.password).text
            val confPassword = findViewById<EditText>(R.id.confirm_password).text

            if (userName.isEmpty() || email.isEmpty() || password.isEmpty() || confPassword.isEmpty()) {
                Toast.makeText(this, "please fill all fill in all the blank", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            if (password.toString() != confPassword.toString()) {
                Toast.makeText(this, "Password not match", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        Log.d(TAG, user!!.uid)
                        db.collection("user").document(user.uid).set(
                            hashMapOf(
                                "user_id" to user.uid,
                                "user_name" to userName.toString(),
                                "email" to email.toString(),
                                "role" to "User"
                            )
                        ).addOnSuccessListener {
                            Log.d(TAG, "DocumentSnapshot successfully written!")
                        }.addOnFailureListener { e ->
                            Log.w(TAG, "Error writing document", e)
                        }

                        this.finish()

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }

        }

    }
}