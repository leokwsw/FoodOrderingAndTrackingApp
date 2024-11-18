package com.example.foodOrderAndTrackingApp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private var email: String = ""
    private var password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth

        val loginBtn = findViewById<MaterialButton>(R.id.login_button)

        findViewById<MaterialButton>(R.id.signup_link).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        findViewById<AppCompatEditText>(R.id.email_input).apply {
            hint = "Email"
            addTextChangedListener {
                email = it.toString()
                loginBtn.isEnabled =
                    email.isNotEmpty() && password.isNotEmpty()
            }
        }

        findViewById<AppCompatEditText>(R.id.password_input).apply {
            hint = "Password"
            addTextChangedListener {
                password = it.toString()
                loginBtn.isEnabled =
                    email.isNotEmpty() && password.isNotEmpty()
            }
        }

        findViewById<MaterialButton>(R.id.login_button).setOnClickListener {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this, ManagementFoodActivity::class.java))
                    } else {
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_LONG).show()
                    }
                }
        }

    }
}