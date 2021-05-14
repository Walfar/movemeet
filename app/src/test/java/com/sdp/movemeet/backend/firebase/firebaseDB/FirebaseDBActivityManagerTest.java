package com.sdp.movemeet.backend.firebase.firebaseDB;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.models.ActivityTest;
import com.sdp.movemeet.backend.serialization.ActivitySerializer;
import com.sdp.movemeet.backend.serialization.BackendSerializer;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Executor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FirebaseDBActivityManagerTest {

    FirebaseDatabase db;
    FirebaseDBManager<Activity> fam;
    BackendSerializer<Activity> serializer;

    DatabaseReference dbRef;
    DataSnapshot getSnap;

    Activity activity;
    String path;

    Task<Void> addTask = new Task<Void>() {
        @Override
        public boolean isComplete() {
            return true;
        }

        @Override
        public boolean isSuccessful() {
            return true;
        }

        @Override
        public boolean isCanceled() {
            return false;
        }

        @Override
        public Void getResult() {
            return null;
        }

        @Override
        public <X extends Throwable> Void getResult(@NonNull Class<X> aClass) throws X {
            return null;
        }

        @Nullable
        @Override
        public Exception getException() {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnSuccessListener(@NonNull OnSuccessListener<? super Void> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super Void> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnSuccessListener(@NonNull android.app.Activity activity, @NonNull OnSuccessListener<? super Void> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnFailureListener(@NonNull android.app.Activity activity, @NonNull OnFailureListener onFailureListener) {
            return null;
        }
    };
    Task<DataSnapshot> getTask = new Task<DataSnapshot>() {
        @Override
        public boolean isComplete() {
            return true;
        }

        @Override
        public boolean isSuccessful() {
            return false;
        }

        @Override
        public boolean isCanceled() {
            return false;
        }

        @Override
        public DataSnapshot getResult() {
            return getSnap;
        }

        @Override
        public <X extends Throwable> DataSnapshot getResult(@NonNull Class<X> aClass) throws X {
            return null;
        }

        @Nullable
        @Override
        public Exception getException() {
            return null;
        }

        @NonNull
        @Override
        public Task<DataSnapshot> addOnSuccessListener(@NonNull OnSuccessListener<? super DataSnapshot> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DataSnapshot> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super DataSnapshot> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DataSnapshot> addOnSuccessListener(@NonNull android.app.Activity activity, @NonNull OnSuccessListener<? super DataSnapshot> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DataSnapshot> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DataSnapshot> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DataSnapshot> addOnFailureListener(@NonNull android.app.Activity activity, @NonNull OnFailureListener onFailureListener) {
            return null;
        }
    };
    Task<Void> deleteTask = new Task<Void>() {

        @Override
        public boolean isComplete() {
            return true;
        }

        @Override
        public boolean isSuccessful() {
            return true;
        }

        @Override
        public boolean isCanceled() {
            return false;
        }

        @Override
        public Void getResult() {
            return null;
        }

        @Override
        public <X extends Throwable> Void getResult(@NonNull Class<X> aClass) throws X {
            return null;
        }

        @Nullable
        @Override
        public Exception getException() {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnSuccessListener(@NonNull OnSuccessListener<? super Void> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super Void> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnSuccessListener(@NonNull android.app.Activity activity, @NonNull OnSuccessListener<? super Void> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnFailureListener(@NonNull android.app.Activity activity, @NonNull OnFailureListener onFailureListener) {
            return null;
        }
    };

    @Before
    public void setup() {
        db = mock(FirebaseDatabase.class);
        dbRef = mock(DatabaseReference.class);
        getSnap = mock(DataSnapshot.class);
        serializer = new ActivitySerializer();
        fam = new FirebaseDBActivityManager(db, serializer);

        activity = ActivityTest.createFakeActivity();
        path = "child/child";

        when(db.getReference(any())).thenReturn(dbRef);
        when(dbRef.get()).thenReturn(getTask);
        when(dbRef.removeValue()).thenReturn(deleteTask);
        when(dbRef.push()).thenReturn(dbRef);
        when(dbRef.setValue(any())).thenReturn(addTask);
    }

    @Test
    public void constructorThrowsIllegalArgumentExceptionOnNullParameter() {
        assertThrows(IllegalArgumentException.class, () -> {
            new FirebaseDBActivityManager(null, serializer);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new FirebaseDBActivityManager(db, null);
        });
    }

    @Test
    public void addThrowsIllegalArgumentExceptionOnNullParameterOrEmptyPath() {
        assertThrows(IllegalArgumentException.class, () -> {
            fam.add(activity, null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            fam.add(null, path);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            fam.add(activity, "");
        });
    }

    @Test
    public void addReturnsCorrectTask() {
        assertEquals(addTask, fam.add(activity, path));
    }

    @Test
    public void deleteThrowsIllegalArgumentExceptionOnNullOrEmptyPath() {
        assertThrows(IllegalArgumentException.class, () -> {
            fam.delete(null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            fam.delete( "");
        });
    }

    @Test
    public void deleteReturnsCorrectTask() {
        assertEquals(deleteTask, fam.delete(path));
    }

    @Test
    public void getThrowsIllegalArgumentExceptionOnNullOrEmptyPath() {
        assertThrows(IllegalArgumentException.class, () -> {
            fam.get(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            fam.get("");
        });
    }

    @Test
    public void getReturnsCorrectTask() {
        assertEquals(getSnap, fam.get(path).getResult());
    }

    @Test
    public void searchIsUnsupported() {
        assertThrows(UnsupportedOperationException.class, () -> {
            fam.search("", activity);
        });
    }
}
