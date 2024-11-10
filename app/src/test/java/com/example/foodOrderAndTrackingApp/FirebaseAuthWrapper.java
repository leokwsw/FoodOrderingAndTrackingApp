package com.example.foodOrderAndTrackingApp;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthWrapper {
    private FirebaseAuth firebaseAuth;

    public FirebaseAuthWrapper() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> registerUser(String email, String password) {
        return firebaseAuth.createUserWithEmailAndPassword(email, password);
    }
}