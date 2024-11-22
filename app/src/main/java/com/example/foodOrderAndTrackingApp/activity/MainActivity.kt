package com.example.foodOrderAndTrackingApp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodOrderAndTrackingApp.R
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        auth = Firebase.auth

        if (auth.currentUser != null) {
            startActivity(Intent(this, ManagementFoodActivity::class.java).apply {
                this.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })
        }

        val db = Firebase.firestore

        db.collection("user").count().get(AggregateSource.SERVER)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val snapshot = task.result
                    Log.d("RunTime", "count : ${snapshot.count}")

                    if (snapshot.count == 0L) {
                        auth.createUserWithEmailAndPassword(
                            "admin@admin.com",
                            "admin12345"
                        ).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser

                                db.collection("user").document(user!!.uid).set(
                                    hashMapOf(
                                        "user_id" to user!!.uid,
                                        "user_name" to "Admin",
                                        "email" to "admin@admin.com",
                                        "role" to "Admin"
                                    )
                                ).addOnSuccessListener {
                                    Log.d("RunTime", "DocumentSnapshot successfully written!")
                                    auth.signOut()
                                }.addOnFailureListener { e ->
                                    Log.w("RunTime", "Error writing document", e)
                                }
                            }
                        }

                    }


                } else {
                    Log.d("RunTime", "Count failed : ${task.exception}")
                }
            }

        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<MaterialButton>(R.id.btn_register).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.btn_login).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }


}