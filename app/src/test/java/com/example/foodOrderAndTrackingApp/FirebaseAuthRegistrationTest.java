package com.example.foodOrderAndTrackingApp;

import static org.junit.Assert.*;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;

import com.google.firebase.FirebaseApp;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;


@RunWith(RobolectricTestRunner.class)
@Config(sdk = {28}) // Specify SDK version to use for Robolectric
public class FirebaseAuthRegistrationTest {

    private FakeFirebaseAuthWrapper firebaseAuthWrapper;

    @Before
    public void setUp() {
        try {
            System.setProperty("dexmaker.dexcache", File.createTempFile("dexmaker", "").getParent());

            Context context = RuntimeEnvironment.getApplication();
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setApplicationId("1:732009116858:android:8a5804716cbb186f3fbe83") // Required for Analytics.
                    .setApiKey("AIzaSyCyOKAdEhcCMqjSdzgUXyGrafORu2") // Required for Auth.
                    .setProjectId("food-order-and-tracking-system") // Required for Firestore.
                    .build();
            FirebaseApp.initializeApp(context, options);

            firebaseAuthWrapper = new FakeFirebaseAuthWrapper();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSuccessfulRegistration() {
        String email = "leo@leo.com";
        String password = "123456";

        firebaseAuthWrapper.setShouldFail(false);
        FakeTask<AuthResult> task = firebaseAuthWrapper.registerUser(email, password);

        task.addOnCompleteListener(resultTask -> {
            assertTrue(resultTask.isSuccessful());
        });
    }

    @Test
    public void testRegistrationWithExistingUsername() {
        String email = "existing@example.com";
        String password = "password123";

        firebaseAuthWrapper.setShouldFail(true);
        firebaseAuthWrapper.setException(new FirebaseAuthException("ERROR_EMAIL_ALREADY_IN_USE", "The email address is already in use by another account."));
        FakeTask<AuthResult> task = firebaseAuthWrapper.registerUser(email, password);

        task.addOnCompleteListener(resultTask -> {
            assertFalse(resultTask.isSuccessful());
            assertTrue(resultTask.getException() instanceof FirebaseAuthException);
        });
    }

    @Test
    public void testRegistrationWithEmptyUsernameOrPassword() {
        String email = "";
        String password = "password123";

        firebaseAuthWrapper.setShouldFail(true);
        FakeTask<AuthResult> task = firebaseAuthWrapper.registerUser(email, password);

        task.addOnCompleteListener(resultTask -> {
            assertFalse(resultTask.isSuccessful());
        });
    }
}
