package com.example.foodOrderAndTrackingApp

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val auth = Firebase.auth
        assertEquals("com.example.foodOrderAndTrackingApp", appContext.packageName)
        assertEquals(null, auth.currentUser)
    }

    @Test
    fun testSuccessfulRegistration() {
        val email = "leo@leo.com"
        Firebase.auth.signInWithEmailAndPassword(email, "123456")
            .addOnSuccessListener { task ->
                assertEquals(email, task.user!!.email)
            }
    }

    @Test
    fun testRegistrationWithExistingEmail() {
        Firebase.auth.createUserWithEmailAndPassword("leo@leo.com", "000000")
            .addOnCompleteListener { task ->
                assertEquals(false, task.isSuccessful)
            }
    }

    @Test
    fun testRegistrationWithEmptyEmailOrPassword() {
        try {
            Firebase.auth.createUserWithEmailAndPassword("", "")
        } catch (e: IllegalArgumentException) {
            assertEquals("Given String is empty or null", e.localizedMessage)
        }


    }
}