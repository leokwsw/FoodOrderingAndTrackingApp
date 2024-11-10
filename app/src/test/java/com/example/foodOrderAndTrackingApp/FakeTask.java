package com.example.foodOrderAndTrackingApp;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.Executor;

public class FakeTask<T> extends Task<T> {
    private final boolean successful;
    private final Exception exception;

    FakeTask(boolean successful, Exception exception) {
        this.successful = successful;
        this.exception = exception;
    }

    @Override
    public boolean isSuccessful() {
        return successful;
    }

    @Override
    public T getResult() {
        if (!successful && exception != null) {
            throw new RuntimeException(exception);
        }
        return null; // Can be replaced with an appropriate result if needed
    }

    @Override
    public <X extends Throwable> T getResult(@NonNull Class<X> aClass) throws X {
        return null;
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public boolean isComplete() {
        return false;
    }

    @NonNull
    @Override
    public Task<T> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
        return null;
    }

    @NonNull
    @Override
    public Task<T> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
        return null;
    }

    @NonNull
    @Override
    public Task<T> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
        return null;
    }

    @NonNull
    @Override
    public Task<T> addOnSuccessListener(@NonNull OnSuccessListener<? super T> onSuccessListener) {
        return null;
    }

    @NonNull
    @Override
    public Task<T> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super T> onSuccessListener) {
        return null;
    }

    @NonNull
    @Override
    public Task<T> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super T> onSuccessListener) {
        return null;
    }

    @Override
    public Exception getException() {
        return exception;
    }

    // Other overridden methods can be no-op or throw UnsupportedOperationException
}