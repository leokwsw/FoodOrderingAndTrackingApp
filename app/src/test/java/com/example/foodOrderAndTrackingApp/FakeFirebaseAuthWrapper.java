package com.example.foodOrderAndTrackingApp;

import static org.mockito.Mockito.when;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import org.mockito.Mockito;

public class FakeFirebaseAuthWrapper extends FirebaseAuthWrapper {
    private boolean shouldFail;
    private Exception exception;

    public void setShouldFail(boolean shouldFail) {
        this.shouldFail = shouldFail;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public FakeTask<AuthResult> registerUser(String email, String password) {
        FakeTask<AuthResult> mockTask = Mockito.mock(FakeTask.class);
        if (shouldFail) {
            when(mockTask.isSuccessful()).thenReturn(false);
            when(mockTask.getException()).thenReturn(exception);
        } else {
            when(mockTask.isSuccessful()).thenReturn(true);
        }
        return mockTask;
    }
}
