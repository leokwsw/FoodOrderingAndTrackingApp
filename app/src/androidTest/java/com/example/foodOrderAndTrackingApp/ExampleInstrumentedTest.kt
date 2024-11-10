package com.example.foodOrderAndTrackingApp

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private lateinit var auth: FirebaseAuth;

    @Before
    fun before() {
        auth = Firebase.auth
    }

    @Test
    fun testSuccessfulRegistration() {
        val email = "leo@leo.com"
        auth.createUserWithEmailAndPassword(email, "123456")
            .addOnSuccessListener { task ->
                assertEquals(email, task.user!!.email)
            }
    }

    @Test
    fun testRegistrationWithExistingEmail() {
        auth.createUserWithEmailAndPassword("leo@leo.com", "123456")
            .addOnSuccessListener { task ->
                assertEquals(null, task.user)
            }
    }

    @Test
    fun testRegistrationWithEmptyEmailOrPassword() {
        auth.createUserWithEmailAndPassword("", "AAAA")
            .addOnSuccessListener { task ->
                assertNotEquals(null, task.user)
            }
    }
}