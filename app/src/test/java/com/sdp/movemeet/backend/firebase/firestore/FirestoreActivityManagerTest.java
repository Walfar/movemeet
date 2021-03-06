package com.sdp.movemeet.backend.firebase.firestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.providers.BackendInstanceProvider;
import com.sdp.movemeet.backend.serialization.ActivitySerializer;
import com.sdp.movemeet.backend.serialization.BackendSerializer;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.models.ActivityTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.Executor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FirestoreActivityManagerTest {

    FirebaseFirestore db;
    Activity activity;
    BackendSerializer<Activity> serializer;
    BackendManager<Activity> activityManager;

    DocumentReference docRef = mock(DocumentReference.class);
    CollectionReference colRef = mock(CollectionReference.class);
    DocumentSnapshot docSnap = mock(DocumentSnapshot.class);
    Query query = mock(Query.class);

    Task<Void> addTask = mock(Task.class);
    Task<Void> deleteTask = mock(Task.class);
    Task<QuerySnapshot> searchTask = mock(Task.class);
    Task<DocumentSnapshot> getTask;

    @Before
    public void setup() {
        activity = ActivityTest.createFakeActivity();
        serializer = new ActivitySerializer();

        db = mock(FirebaseFirestore.class);

        when(db.document(any())).thenReturn(docRef);
        when(db.collection(any())).thenReturn(colRef);

        // Search
        when(colRef.document()).thenReturn(docRef);
        when(colRef.whereEqualTo((String) any(), any())).thenReturn(query);
        when(query.get()).thenReturn(searchTask);

        // Delete
        when(docRef.delete()).thenReturn(deleteTask);

        // Get
        Map<String, Object> data = serializer.serialize(activity);
        when(docSnap.getData()).thenReturn(data);

        getTask = new Task<DocumentSnapshot>() {
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
            public DocumentSnapshot getResult() {
                return docSnap;
            }

            @Override
            public <X extends Throwable> DocumentSnapshot getResult(@NonNull Class<X> aClass) throws X {
                return null;
            }

            @Nullable
            @Override
            public Exception getException() {
                return null;
            }

            @NonNull
            @Override
            public Task<DocumentSnapshot> addOnSuccessListener(@NonNull OnSuccessListener<? super DocumentSnapshot> onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<DocumentSnapshot> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super DocumentSnapshot> onSuccessListener) {
                return null;
            }


            @NonNull
            @Override
            public Task<DocumentSnapshot> addOnSuccessListener(@NonNull android.app.Activity activity, @NonNull OnSuccessListener<? super DocumentSnapshot> onSuccessListener) {
                return null;
            }


            @NonNull
            @Override
            public Task<DocumentSnapshot> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<DocumentSnapshot> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<DocumentSnapshot> addOnFailureListener(@NonNull android.app.Activity activity, @NonNull OnFailureListener onFailureListener) {
                return null;
            }
        };

        when(docRef.get()).thenReturn(getTask);

        // Add
        when(docRef.set(any())).thenReturn(addTask);

        BackendInstanceProvider.firestore = db;

        activityManager = new FirestoreActivityManager(
                FirestoreActivityManager.ACTIVITIES_COLLECTION,
                serializer);
    }

    @Test
    public void constructorThrowsIllegalArgumentExceptionOnNullParameter() {

        assertThrows(IllegalArgumentException.class, () -> {
            new FirestoreActivityManager(null, serializer);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new FirestoreActivityManager(FirestoreActivityManager.ACTIVITIES_COLLECTION, null);
        });
    }

    @Test
    public void addThrowsIllegalArgumentExceptionOnNullObject() {
        assertThrows(IllegalArgumentException.class, () -> {
            activityManager.add(null, "");
        });
    }

    /*@Test
    public void addReturnsCorrectTask() {
        assertEquals(addTask, activityManager.add(activity, ""));
    } */

    @Test
    public void deleteThrowsIllegalArgumentExceptionOnNullOrEmptyPath() {
        assertThrows(IllegalArgumentException.class, () -> {
            activityManager.delete(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            activityManager.delete("");
        });
    }

    @Test
    public void deleteReturnsCorrectTask() {
        assertEquals(deleteTask, activityManager.delete("path"));
    }

    @Test
    public void searchThrowsIllegalArgumentExceptionOnNullParameter() {
        assertThrows(IllegalArgumentException.class, () -> {
            activityManager.search(null, 6);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            activityManager.search("field", null);
        });
    }

    @Test
    public void searchReturnsCorrectTask() {
        assertEquals(searchTask, activityManager.search("field", 6));
    }

    @Test
    public void getThrowsIllegalArgumentExceptionOnNullOrEmptyPath() {
        assertThrows(IllegalArgumentException.class, () -> {
            activityManager.get(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            activityManager.get("");
        });
    }

    @Test
    public void getReturnsCorrectTask() {
        assertEquals(getTask, activityManager.get("path"));
    }

    @After
    public void tearDown() {
        //BackendInstanceProvider.firestore = FirebaseFirestore.getInstance();
    }
}